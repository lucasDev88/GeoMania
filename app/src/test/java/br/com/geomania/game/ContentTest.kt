package br.com.geomania.game

import br.com.geomania.data.*
import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ContentTest {
    private val assets = listOf(File("src/main/assets"), File("app/src/main/assets")).first { it.isDirectory }
    private inline fun <reified T> read(name: String): T = Json.decodeFromString(File(assets, name).readText())

    @Test fun packagedContentDeserializesAndReferencesExistingEntries() {
        val periods = read<List<Periodo>>("periods.json")
        val resources = read<List<Recurso>>("resources.json")
        val pairs = read<List<GamePair>>("game_pairs.json")
        val sources = read<List<Fonte>>("sources.json")
        val learning = read<Learning>("learning.json")
        assertEquals(7, periods.size)
        assertTrue(periods.all { it.resumo.isNotBlank() && it.pergunta.isNotBlank() })
        assertEquals(6, resources.size)
        assertEquals(6, pairs.size)
        assertTrue(learning.aviso.contains("coexistem"))
        val ids = sources.map { it.id }.toSet()
        assertTrue(periods.all { ids.containsAll(it.fontes) })
        assertTrue(resources.all { ids.containsAll(it.fontes) })
        assertTrue(pairs.all { p -> periods.any { it.id == p.periodoId } && resources.any { it.id == p.recursoId } })
        assertEquals(12, GameEngine(pairs).newGame().cards.size)
    }
}
