package br.com.geomania.expedition

import kotlinx.serialization.Serializable

@Serializable
data class ExpeditionState(val mission: Int? = null, val position: String = "cidade",
    val moving: List<String> = emptyList(), val visited: Set<String> = emptySet(),
    val marking: Boolean = false, val route: List<String> = emptyList(),
    val feedback: String = "", val solved: Boolean = false, val completed: Set<Int> = emptySet())

class ExpeditionEngine(val content: ExpeditionContent) {
    init { content.validate() }
    fun start(s: ExpeditionState, mission: Int) = if (mission in content.missoes.indices)
        ExpeditionState(mission = mission, visited = setOf("cidade"), completed = s.completed) else s
    fun travel(s: ExpeditionState, destination: String): ExpeditionState {
        if (s.mission == null || s.solved || s.marking || s.moving.isNotEmpty()) return s
        val path = content.shortestPath(s.position, destination) ?: return s
        return s.copy(moving = path.drop(1), visited = if (path.size == 1) s.visited + destination else s.visited, feedback = "")
    }
    fun step(s: ExpeditionState): ExpeditionState {
        val next = s.moving.firstOrNull() ?: return s
        val left = s.moving.drop(1)
        return s.copy(position = next, moving = left,
            visited = if (left.isEmpty()) s.visited + next else s.visited)
    }
    fun markMode(s: ExpeditionState): ExpeditionState {
        if (s.mission == null || s.solved || s.moving.isNotEmpty()) return s
        return s.copy(marking = !s.marking, feedback = "")
    }
    fun mark(s: ExpeditionState, place: String): ExpeditionState {
        if (!s.marking || s.solved || s.mission == null || place !in s.visited) return s
        if (place in s.route) return s.copy(route = s.route.take(s.route.indexOf(place)), feedback = "")
        return s.copy(route = s.route + place, feedback = "")
    }
    fun clear(s: ExpeditionState) = if (s.solved) s else s.copy(route = emptyList(), feedback = "")
    fun check(s: ExpeditionState): ExpeditionState {
        val index = s.mission ?: return s
        if (s.solved || s.moving.isNotEmpty()) return s
        val m = content.missoes[index]
        if (!s.visited.containsAll(m.essenciais)) return s.copy(feedback = "Ainda faltam pistas importantes. Explore mais locais antes de concluir.")
        if (s.route.isEmpty()) return s.copy(feedback = "Marque no mapa a sequência de locais que explica o caso.")
        return if (s.route in m.rotas) s.copy(solved = true, marking = false, completed = s.completed + index,
            feedback = m.conclusao) else s.copy(feedback = "Essa conexão não combina com todas as pistas. Releia o caderno e confira a ordem dos locais. Você pode tentar novamente.")
    }
    fun overview(s: ExpeditionState) = ExpeditionState(completed = s.completed)
}
