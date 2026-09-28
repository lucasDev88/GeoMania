package br.com.geomania.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ContentRepository(private val context: Context) {
    private inline fun <reified T> read(name: String): T =
        context.assets.open(name).bufferedReader().use { Json.decodeFromString<T>(it.readText()) }

    suspend fun load(): Conteudo = withContext(Dispatchers.IO) {
        Conteudo(read("periods.json"), read("resources.json"), read("game_pairs.json"), read("sources.json"), read("learning.json"), read("earth_choices.json"))
            .also { content ->
                content.escolhas.validate()
                require(content.escolhas.fases.all { phase -> phase.fontes.all { id -> content.fontes.any { it.id == id } } })
                require(content.pares.size == 6)
                require(content.pares.map { it.pairId }.distinct().size == 6)
                require(content.pares.all { pair ->
                    content.periodos.any { it.id == pair.periodoId } &&
                        content.recursos.any { it.id == pair.recursoId }
                })
            }
    }
}
