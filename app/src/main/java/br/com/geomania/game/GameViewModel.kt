package br.com.geomania.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.geomania.data.GamePair
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel(pairs: List<GamePair>, private val preferences: ResultStore) : ViewModel() {
    private val engine = GameEngine(pairs)
    private val _state = MutableStateFlow(engine.newGame())
    val state = _state.asStateFlow()
    private val _saveStatus = MutableStateFlow("")
    val saveStatus = _saveStatus.asStateFlow()
    private var session = UUID.randomUUID().toString()
    private var mismatchJob: Job? = null
    private var active = false
    private var saving = false

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _state.update { if (active && it.started && !it.isFinished && it.explanation == null)
                    it.copy(elapsedSeconds = it.elapsedSeconds + 1) else it }
            }
        }
    }
    fun setActive(value: Boolean) { active = value }
    fun select(id: Int) {
        val before = _state.value
        val next = engine.select(before, id)
        if (before == next) return
        _state.value = next
        if (next.isFinished) save()
        else if (next.selected.size == 2 && next.explanation == null) {
            mismatchJob = viewModelScope.launch {
                delay(1300)
                _state.update(engine::concealMismatch)
            }
        }
    }
    fun dismissExplanation() { _state.update(engine::dismissExplanation) }
    fun restart() {
        if (saving) return
        mismatchJob?.cancel()
        session = UUID.randomUUID().toString()
        _saveStatus.value = ""
        _state.value = engine.newGame()
    }
    fun save() {
        if (saving || !_state.value.isFinished) return
        val result = _state.value.score
        val resultSession = session
        saving = true
        _saveStatus.value = "Salvando resultado…"
        viewModelScope.launch {
            try {
                preferences.saveResult(resultSession, result)
                _saveStatus.value = "Resultado salvo no aparelho."
            } catch (e: java.io.IOException) {
                _saveStatus.value = "Não foi possível salvar. Toque para tentar novamente."
            } finally { saving = false }
        }
    }
}
