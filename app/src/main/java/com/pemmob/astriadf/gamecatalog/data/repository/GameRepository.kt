package com.pemmob.astriadf.gamecatalog.data.repository

import com.pemmob.astriadf.gamecatalog.BuildConfig
import com.pemmob.astriadf.gamecatalog.data.model.Game
import com.pemmob.astriadf.gamecatalog.data.model.GameDetail
import com.pemmob.astriadf.gamecatalog.data.remote.RawgApiService

// Repository menjadi penghubung antara ViewModel dan sumber data API.
class GameRepository(
    private val apiService: RawgApiService
) {

    // Mengambil daftar game dari RAWG.
    suspend fun getGames(search: String): List<Game> {

        val isSearching = search.isNotBlank()

        val response = apiService.getGames(
            apiKey = BuildConfig.RAWG_API_KEY,
            search = search.ifBlank { null },
            // Aktifkan hanya saat search agar hasil tidak mengandung game yang tidak relevan.
            searchPrecise = if (isSearching) true else null,
            // Saat search, biarkan RAWG urutkan by relevansi.
            // Saat tidak search, tampilkan game populer berdasarkan rating.
            ordering = if (isSearching) null else "-rating"
        )

        return response.results
    }

    // Mengambil detail game berdasarkan ID.
    suspend fun getGameDetail(gameId: Int): GameDetail {

        return apiService.getGameDetail(
            gameId = gameId,
            apiKey = BuildConfig.RAWG_API_KEY
        )
    }
}