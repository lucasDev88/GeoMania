package br.com.geomania.game

import br.com.geomania.data.Fonte
import br.com.geomania.expedition.*
import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ExpeditionEngineTest {
    private val assets = listOf(File("src/main/assets"), File("app/src/main/assets")).first { it.isDirectory }
    private val content = Json.decodeFromString<ExpeditionContent>(File(assets, "expedition.json").readText())
    private val engine = ExpeditionEngine(content)
    private fun visit(state: ExpeditionState, id: String): ExpeditionState {
        var s = engine.travel(state, id)
        while (s.moving.isNotEmpty()) s = engine.step(s)
        return s
    }
    private fun evidence(index: Int): ExpeditionState {
        var s = engine.start(ExpeditionState(), index)
        content.missoes[index].essenciais.forEach { s = visit(s, it) }
        return s
    }
    private fun mark(state: ExpeditionState, route: List<String>): ExpeditionState {
        var s = if (state.marking) state else engine.markMode(state)
        route.forEach { s = engine.mark(s, it) }
        return s
    }
    @Test fun completeContentHas24CluesAndValidReferences() {
        content.validate()
        assertEquals(8, content.lugares.size)
        assertEquals(24, content.missoes.sumOf { it.pistas.size })
        val sources = Json.decodeFromString<List<Fonte>>(File(assets, "sources.json").readText()).map { it.id }
        assertTrue(content.missoes.all { sources.containsAll(it.fontes) })
    }
    @Test fun everyPairOfLocationsHasTraversableRoads() {
        content.lugares.forEach { a -> content.lugares.forEach { b ->
            val path = content.shortestPath(a.id, b.id)!!
            assertEquals(a.id, path.first()); assertEquals(b.id, path.last())
            assertTrue(path.zipWithNext().all { (x,y) -> content.caminhos.any { setOf(it.de,it.para) == setOf(x,y) } })
        } }
        assertNull(content.shortestPath("missing", "cidade"))
    }
    @Test fun travelCollectsOnlyDestinationAndBlocksCompetingCommands() {
        val start = engine.start(ExpeditionState(), 0)
        var s = engine.travel(start, "nascente")
        assertEquals(s, engine.travel(s, "fabrica"))
        assertEquals(s, engine.markMode(s))
        s = engine.step(s)
        assertEquals(start.visited, s.visited)
        while (s.moving.isNotEmpty()) s = engine.step(s)
        assertEquals(setOf("cidade", "nascente"), s.visited)
        assertEquals("nascente", s.position)
    }
    @Test fun cannotMarkUnvisitedLocationsAndRepeatedMarkUndoesFollowingNodes() {
        var s = engine.markMode(engine.start(ExpeditionState(), 0))
        assertEquals(s, engine.mark(s, "mina"))
        s = mark(evidence(0), listOf("plantacao", "rio", "cidade"))
        assertEquals(listOf("plantacao"), engine.mark(s, "rio").route)
    }
    @Test fun riverRequiresFactoryEvidenceEvenWithCorrectConnection() {
        var s = engine.start(ExpeditionState(), 0)
        content.missoes[0].rotas.first().forEach { s = visit(s, it) }
        s = engine.check(mark(s, content.missoes[0].rotas.first()))
        assertFalse(s.solved); assertTrue(s.feedback.contains("faltam"))
    }
    @Test fun wrongOrderCanBeCorrectedWithoutLosingClues() {
        val route = content.missoes[2].rotas.first()
        val wrong = engine.check(mark(evidence(2), route.reversed()))
        assertFalse(wrong.solved)
        val correct = engine.check(mark(engine.clear(wrong), route))
        assertTrue(correct.solved); assertEquals(wrong.visited, correct.visited)
    }
    @Test fun allMissionsCompleteAndSolvedStateCannotChange() {
        content.missoes.indices.forEach { index ->
            val s = engine.check(mark(evidence(index), content.missoes[index].rotas.first()))
            assertTrue(s.solved); assertEquals(setOf(index), s.completed)
            assertEquals(s, engine.check(s)); assertEquals(s, engine.clear(s))
            assertEquals(s, engine.travel(s, "rio")); assertEquals(s, engine.markMode(s))
            assertEquals(s, engine.mark(s, "cidade"))
        }
    }
    @Test fun campaignCompletionSurvivesOverviewAndReplay() {
        var campaign = ExpeditionState()
        content.missoes.indices.forEach { index ->
            var s = engine.start(campaign, index)
            content.missoes[index].essenciais.forEach { s = visit(s, it) }
            campaign = engine.overview(engine.check(mark(s, content.missoes[index].rotas.first())))
            assertNull(campaign.mission); assertTrue(campaign.visited.isEmpty())
        }
        assertEquals(setOf(0,1,2), campaign.completed)
        assertEquals(campaign.completed, engine.start(campaign, 0).completed)
        assertEquals(campaign, engine.start(campaign, -1))
    }
}
