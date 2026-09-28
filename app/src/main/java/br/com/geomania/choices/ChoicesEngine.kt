package br.com.geomania.choices

data class DecisionRecord(val decision: Int, val option: Int, val before: Indicators, val after: Indicators)
data class PhaseResult(val indicators: Indicators, val history: List<DecisionRecord>)
data class ChoicesState(
    val phase: Int? = null, val decision: Int = 0,
    val indicators: Indicators = Indicators(0, 0, 0, 0),
    val selected: Int? = null, val history: List<DecisionRecord> = emptyList(),
    val results: Map<Int, PhaseResult> = emptyMap()
) { val finished get() = phase != null && decision == 4 }

class ChoicesEngine(val content: EarthContent) {
    init { content.validate() }
    fun start(state: ChoicesState, phase: Int): ChoicesState {
        if (phase !in content.fases.indices) return state
        return ChoicesState(phase = phase, indicators = content.fases[phase].iniciais, results = state.results - phase)
    }
    fun choose(state: ChoicesState, option: Int): ChoicesState {
        val phase = state.phase ?: return state
        if (state.finished || state.selected != null) return state
        val decision = content.fases[phase].decisoes[state.decision]
        val choice = decision.opcoes.getOrNull(option) ?: return state
        val after = state.indicators.apply(choice.efeitos)
        return state.copy(indicators = after, selected = option,
            history = state.history + DecisionRecord(state.decision, option, state.indicators, after))
    }
    fun advance(state: ChoicesState): ChoicesState {
        val phase = state.phase ?: return state
        if (state.selected == null || state.finished) return state
        val next = state.decision + 1
        return state.copy(decision = next, selected = null,
            results = if (next == 4) state.results + (phase to PhaseResult(state.indicators, state.history)) else state.results)
    }
    fun overview(state: ChoicesState) = ChoicesState(results = state.results)
}
