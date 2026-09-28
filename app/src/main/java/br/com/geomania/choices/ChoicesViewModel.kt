package br.com.geomania.choices

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChoicesViewModel(val content: EarthContent) : ViewModel() {
    private val engine = ChoicesEngine(content)
    private val _state = MutableStateFlow(ChoicesState())
    val state = _state.asStateFlow()
    fun start(phase: Int) { _state.update { engine.start(it, phase) } }
    fun choose(option: Int) { _state.update { engine.choose(it, option) } }
    fun advance() { _state.update(engine::advance) }
    fun overview() { _state.update(engine::overview) }
}
