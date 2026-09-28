package br.com.geomania.data

import kotlinx.serialization.Serializable

@Serializable
data class Periodo(
    val id: String, val titulo: String, val periodo: String,
    val formaProducao: String, val recursos: List<String>,
    val apropriacao: String, val tecnica: String, val usos: List<String>,
    val transformacoes: List<String>, val fontes: List<Int>
)
@Serializable
data class Recurso(
    val id: String, val titulo: String, val descricao: String,
    val usos: List<String>, val cuidado: String, val fontes: List<Int>
)
@Serializable
data class GamePair(
    val pairId: String, val recursoOuContexto: String,
    val usoOuApropriacao: String, val explicacao: String,
    val periodoId: String, val recursoId: String
)
@Serializable
data class Fonte(val id: Int, val titulo: String, val url: String)
@Serializable
data class Learning(val tema: String, val pergunta: String, val conceito: String,
    val destaque: String, val aviso: String, val objetivo: String, val metodo: String)
data class Conteudo(
    val periodos: List<Periodo>, val recursos: List<Recurso>,
    val pares: List<GamePair>, val fontes: List<Fonte>, val learning: Learning,
    val escolhas: br.com.geomania.choices.EarthContent
)
