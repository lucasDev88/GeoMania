package br.com.geomania.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import br.com.geomania.game.ResultStore

private val Context.preferences by preferencesDataStore("geomania")
data class Estatisticas(val recorde: Int = 0, val partidas: Int = 0)
class PreferencesRepository(context: Context) : ResultStore {
    private val store = context.applicationContext.preferences
    private val best = intPreferencesKey("recorde")
    private val games = intPreferencesKey("partidas")
    private val last = stringPreferencesKey("ultima_partida")
    val stats = store.data.map { Estatisticas(it[best] ?: 0, it[games] ?: 0) }
    override suspend fun saveResult(session: String, score: Int) {
        store.edit {
            if (it[last] != session) {
                it[best] = maxOf(it[best] ?: 0, score)
                it[games] = (it[games] ?: 0) + 1
                it[last] = session
            }
        }
    }
}
