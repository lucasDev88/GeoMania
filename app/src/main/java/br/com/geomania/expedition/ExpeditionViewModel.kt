package br.com.geomania.expedition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpeditionViewModel(val content: ExpeditionContent) : ViewModel() {
    private val engine = ExpeditionEngine(content)
    private val _state = MutableStateFlow(ExpeditionState())
    val state = _state.asStateFlow()
    private var travelJob: Job? = null
    fun start(mission: Int) { if (mission !in content.missoes.indices) return; travelJob?.cancel(); _state.update { engine.start(it, mission) } }
    fun touch(place: String) {
        if (_state.value.marking) { _state.update { engine.mark(it, place) }; return }
        val next = engine.travel(_state.value, place)
        if (next == _state.value) return
        _state.value = next
        if (next.moving.isNotEmpty()) {
            travelJob = viewModelScope.launch {
                while (_state.value.moving.isNotEmpty()) {
                    delay(500)
                    _state.update(engine::step)
                }
            }
        }
    }
    fun toggleMarking() { _state.update(engine::markMode) }
    fun clear() { _state.update(engine::clear) }
    fun check() { _state.update(engine::check) }
    fun overview() { travelJob?.cancel(); _state.update(engine::overview) }
}

