package br.com.geomania.game

import br.com.geomania.choices.*
import br.com.geomania.data.Fonte
import br.com.geomania.ui.conclusion
import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ChoicesEngineTest {
    private val assets = listOf(File("src/main/assets"), File("app/src/main/assets")).first { it.isDirectory }
    private val content = Json.decodeFromString<EarthContent>(File(assets, "earth_choices.json").readText())
    private val engine = ChoicesEngine(content)

    @Test fun actualContentHasSixteenDecisionsFortyEightOptionsAndValidSources() {
        content.validate()
        assertEquals(16, content.fases.sumOf { it.decisoes.size })
        assertEquals(48, content.fases.sumOf { it.decisoes.sumOf { d -> d.opcoes.size } })
        val ids = Json.decodeFromString<List<Fonte>>(File(assets, "sources.json").readText()).map { it.id }.toSet()
        assertTrue(content.fases.all { ids.containsAll(it.fontes) })
    }
    @Test fun industrialExampleAppliesOnceAndRequiresExplanationBeforeContinuing() {
        val initial = engine.start(ChoicesState(), 2)
        assertEquals(initial, engine.advance(initial))
        val chosen = engine.choose(initial, 0)
        assertEquals(Indicators(80,60,60,55), chosen.indicators)
        assertEquals(chosen, engine.choose(chosen, 1))
        assertEquals(1, chosen.history.size)
        assertEquals(1, engine.advance(chosen).decision)
    }
    @Test fun all324PathsFinishWithinBoundsWithHistoryAndDistinctOutcomes() {
        content.fases.indices.forEach { phase ->
            val endings = mutableSetOf<Indicators>()
            repeat(81) { code ->
                var digits = code
                var state = engine.start(ChoicesState(), phase)
                repeat(4) {
                    state = engine.choose(state, digits % 3)
                    digits /= 3
                    assertTrue(state.indicators.values().all { it in 0..100 })
                    state = engine.advance(state)
                }
                assertTrue(state.finished)
                assertEquals(4, state.history.size)
                assertEquals(state.indicators, state.results[phase]!!.indicators)
                assertEquals(state, engine.choose(state, 0))
                assertEquals(state, engine.advance(state))
                endings += state.indicators
            }
            assertTrue(endings.size > 1)
        }
    }
    @Test fun newPhasesResetIndicatorsAndRetainOtherResults() {
        var state = ChoicesState()
        repeat(4) { phase ->
            state = engine.start(state, phase)
            assertEquals(content.fases[phase].iniciais, state.indicators)
            repeat(4) { state = engine.advance(engine.choose(state, 0)) }
        }
        assertEquals(4, state.results.size)
        state = engine.start(engine.overview(state), 1)
        assertEquals(3, state.results.size)
        assertFalse(state.results.containsKey(1))
        assertTrue(state.history.isEmpty())
        assertEquals(0, state.decision)
    }
    @Test fun invalidInputAndOverviewCannotApplyEffects() {
        val empty = ChoicesState()
        assertEquals(empty, engine.choose(empty, 0))
        assertEquals(empty, engine.start(empty, -1))
        val initial = engine.start(empty, 0)
        assertEquals(initial, engine.choose(initial, 3))
        assertEquals(initial, engine.choose(initial, -1))
    }
    @Test fun clampedEffectsAndConclusionReflectActualValues() {
        assertEquals(Indicators(100,0,100,0), Indicators(95,2,99,1).apply(Indicators(20,-10,5,-8)))
        val initial = Indicators(100,30,50,80)
        val final = Indicators(80,60,60,55)
        assertTrue(conclusion(final, initial).contains("consumindo"))
        assertTrue(conclusion(final, initial).contains("Natureza diminuiu 20"))
        assertTrue(conclusion(initial, initial).contains("Produção terminou no valor inicial"))
    }
}
