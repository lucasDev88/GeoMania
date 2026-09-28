package br.com.geomania.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.geomania.data.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val preferences = PreferencesRepository(application)
    private val _content = MutableStateFlow<Conteudo?>(null)
    val content = _content.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    val stats = preferences.stats.catch { e ->
        if (e is CancellationException) throw e
        _error.value = "Não foi possível ler os recordes salvos."
        emit(Estatisticas())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Estatisticas())
    init { load() }
    fun load() {
        _error.value = null
        viewModelScope.launch {
            try { _content.value = ContentRepository(getApplication()).load() }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { _error.value = "Não foi possível abrir o conteúdo local. Tente novamente." }
        }
    }
}
