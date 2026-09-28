package br.com.geomania.game

import br.com.geomania.data.GamePair
import kotlin.random.Random
import org.junit.Assert.*
import org.junit.Test

class GameEngineTest {
    private val pairs = (1..6).map { GamePair("$it", "Recurso $it", "Uso $it", "Explicação $it", "periodo", "recurso") }
    private val engine = GameEngine(pairs, Random(23))

    @Test fun deckHasTwoCardsPerPairAndUniqueIds() {
        val state = engine.newGame()
        assertEquals(12, state.cards.size)
        assertEquals(12, state.cards.map { it.id }.toSet().size)
        assertTrue(state.cards.groupBy { it.pairId }.values.all { it.size == 2 })
    }
    @Test fun repeatedAndUnknownClicksDoNotCountAsAttempts() {
        val state = engine.select(engine.newGame(), 0)
        assertEquals(state, engine.select(state, 0))
        assertEquals(state, engine.select(state, 100))
        assertEquals(0, state.attempts)
    }
    @Test fun mismatchBlocksThirdCardUntilConcealed() {
        var state = engine.select(engine.select(engine.newGame(), 0), 2)
        assertEquals(1, state.attempts)
        assertTrue(state.blocked)
        assertEquals(state, engine.select(state, 4))
        state = engine.concealMismatch(state)
        assertTrue(state.selected.isEmpty())
        assertFalse(state.blocked)
        assertTrue(state.matchedPairs.isEmpty())
    }
    @Test fun correctPairRemainsMatchedAndExplainsConnection() {
        var state = engine.select(engine.select(engine.newGame(), 0), 1)
        assertEquals(setOf("1"), state.matchedPairs)
        assertEquals("Explicação 1", state.explanation)
        assertEquals(state, engine.concealMismatch(state))
        state = engine.dismissExplanation(state)
        assertEquals(state, engine.select(state, 0))
    }
    @Test fun sixPairsFinishExactlyOnceAndRestartClearsState() {
        var state = engine.newGame()
        repeat(6) { i ->
            state = engine.select(engine.select(state, i * 2), i * 2 + 1)
            state = engine.dismissExplanation(state)
        }
        assertTrue(state.isFinished)
        assertEquals(6, state.attempts)
        assertEquals(1000, state.score)
        assertEquals(state, engine.select(state, 0))
        assertEquals(0, engine.newGame().attempts)
        assertFalse(engine.newGame().isFinished)
    }
    @Test fun scoreUsesDocumentFormulaAndFloor() {
        assertEquals(855, GameState(attempts = 7, elapsedSeconds = 60).score)
        assertEquals(100, GameState(attempts = 200, elapsedSeconds = 1000).score)
    }
}
