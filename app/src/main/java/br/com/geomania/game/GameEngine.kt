package br.com.geomania.game

import br.com.geomania.data.GamePair
import kotlin.random.Random

data class GameCard(val id: Int, val pairId: String, val text: String)
data class GameState(
    val cards: List<GameCard> = emptyList(),
    val selected: List<Int> = emptyList(),
    val matchedPairs: Set<String> = emptySet(),
    val attempts: Int = 0,
    val elapsedSeconds: Int = 0,
    val explanation: String? = null,
    val started: Boolean = false
) {
    val isFinished get() = cards.isNotEmpty() && matchedPairs.size == cards.size / 2
    val score get() = maxOf(100, 1000 - maxOf(0, attempts - 6) * 25 - elapsedSeconds * 2)
    val blocked get() = selected.size == 2 || explanation != null || isFinished
}

class GameEngine(private val pairs: List<GamePair>, private val random: Random = Random.Default) {
    init { require(pairs.size == 6 && pairs.map { it.pairId }.distinct().size == 6) }
    fun newGame() = GameState(cards = pairs.flatMapIndexed { i, p ->
        listOf(GameCard(i * 2, p.pairId, p.recursoOuContexto), GameCard(i * 2 + 1, p.pairId, p.usoOuApropriacao))
    }.shuffled(random))

    fun select(state: GameState, id: Int): GameState {
        val card = state.cards.find { it.id == id } ?: return state
        if (state.blocked || id in state.selected || card.pairId in state.matchedPairs) return state
        if (state.selected.isEmpty()) return state.copy(selected = listOf(id), started = true)
        val first = state.cards.first { it.id == state.selected.first() }
        val next = state.copy(selected = state.selected + id, attempts = state.attempts + 1)
        return if (first.pairId == card.pairId) next.copy(
            matchedPairs = state.matchedPairs + card.pairId,
            explanation = pairs.first { it.pairId == card.pairId }.explicacao
        ) else next
    }
    fun concealMismatch(state: GameState) = if (state.selected.size == 2 && state.explanation == null)
        state.copy(selected = emptyList()) else state
    fun dismissExplanation(state: GameState) = state.copy(selected = emptyList(), explanation = null)
}
