package br.com.geomania.choices

import kotlinx.serialization.Serializable

@Serializable
data class Indicators(val natureza: Int, val producao: Int, val sociedade: Int, val recursos: Int) {
    fun apply(delta: Indicators) = Indicators(
        (natureza + delta.natureza).coerceIn(0, 100), (producao + delta.producao).coerceIn(0, 100),
        (sociedade + delta.sociedade).coerceIn(0, 100), (recursos + delta.recursos).coerceIn(0, 100)
    )
    operator fun minus(other: Indicators) = Indicators(natureza - other.natureza, producao - other.producao,
        sociedade - other.sociedade, recursos - other.recursos)
    fun values() = listOf(natureza, producao, sociedade, recursos)
}
@Serializable
data class EarthOption(val titulo: String, val efeitos: Indicators, val explicacao: String)
@Serializable
data class EarthDecision(val id: String, val situacao: String, val opcoes: List<EarthOption>)
@Serializable
data class EarthPhase(val id: String, val titulo: String, val contexto: String, val iniciais: Indicators,
    val decisoes: List<EarthDecision>, val reflexao: String, val fontes: List<Int>)
@Serializable
data class EarthContent(val introducao: String, val indicadores: String, val fases: List<EarthPhase>) {
    fun validate() {
        require(fases.size == 4 && fases.map { it.id }.distinct().size == 4)
        require(fases.flatMap { it.decisoes }.map { it.id }.distinct().size == 16)
        fases.forEach { phase ->
            require(phase.iniciais.values().all { it in 0..100 })
            require(phase.decisoes.size == 4 && phase.fontes.isNotEmpty())
            phase.decisoes.forEach { decision ->
                require(decision.situacao.isNotBlank() && decision.opcoes.size == 3)
                require(decision.opcoes.map { it.titulo }.distinct().size == 3)
                decision.opcoes.forEach { option ->
                    require(option.titulo.isNotBlank() && option.explicacao.isNotBlank())
                    require(option.efeitos.values().all { it in -100..100 })
                    require(option.efeitos.values().any { it < 0 } && option.efeitos.values().any { it > 0 })
                }
            }
        }
    }
}
