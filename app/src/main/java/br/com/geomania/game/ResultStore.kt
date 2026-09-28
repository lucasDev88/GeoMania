package br.com.geomania.game

interface ResultStore {
    suspend fun saveResult(session: String, score: Int)
}
