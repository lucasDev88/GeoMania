package br.com.geomania.expedition

import kotlinx.serialization.Serializable

@Serializable
data class MapPlace(val id: String, val nome: String, val tipo: String, val x: Float, val y: Float)
@Serializable
data class MapRoad(val de: String, val para: String)
@Serializable
data class MapClue(val local: String, val titulo: String, val texto: String)
@Serializable
data class MapMission(val id: String, val titulo: String, val objetivo: String, val instrucao: String,
    val pistas: List<MapClue>, val essenciais: List<String>, val rotas: List<List<String>>,
    val dica: String, val conclusao: String, val fontes: List<Int>)
@Serializable
data class ExpeditionContent(val introducao: String, val lugares: List<MapPlace>,
    val caminhos: List<MapRoad>, val missoes: List<MapMission>) {
    fun validate() {
        val ids = lugares.map { it.id }.toSet()
        require(ids.size == lugares.size && "cidade" in ids)
        require(lugares.all { it.nome.isNotBlank() && it.x in .08f.. .92f && it.y in .08f.. .92f })
        require(caminhos.all { it.de in ids && it.para in ids && it.de != it.para })
        require(missoes.size == 3 && missoes.map { it.id }.distinct().size == 3)
        require(ids.all { target -> shortestPath("cidade", target) != null })
        missoes.forEach { m ->
            require(m.pistas.map { it.local }.toSet() == ids && m.pistas.size == ids.size)
            require(m.pistas.all { it.texto.isNotBlank() && it.titulo.isNotBlank() })
            require(m.essenciais.isNotEmpty() && m.essenciais.all { it in ids })
            require(m.rotas.isNotEmpty() && m.rotas.all { route ->
                route.size >= 2 && route.distinct().size == route.size && route.all { it in ids && it in m.essenciais }
            })
        }
    }
    fun shortestPath(from: String, to: String): List<String>? {
        if (lugares.none { it.id == from } || lugares.none { it.id == to }) return null
        val queue = ArrayDeque<List<String>>()
        val seen = mutableSetOf(from)
        queue.add(listOf(from))
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (path.last() == to) return path
            caminhos.forEach { road ->
                val next = when (path.last()) { road.de -> road.para; road.para -> road.de; else -> null }
                if (next != null && seen.add(next)) queue.add(path + next)
            }
        }
        return null
    }
}
