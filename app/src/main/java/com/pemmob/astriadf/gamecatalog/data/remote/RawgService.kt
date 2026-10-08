package com.pemmob.astriadf.gamecatalog.data.remote

import com.pemmob.astriadf.gamecatalog.data.model.GameDetail
import com.pemmob.astriadf.gamecatalog.data.model.GameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Interface Retrofit yang berisi seluruh endpoint RAWG
interface RawgApiService {

    // Mengambil daftar game.
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("search") search: String? = null,
        // Menonaktifkan fuzzy search agar hanya mencocokkan nama game secara presisi.
        @Query("search_precise") searchPrecise: Boolean? = null,
        @Query("page_size") pageSize: Int = 20,
        @Query("ordering") ordering: String? = "-rating"
    ): GameResponse

    // Mengambil detail satu game berdasarkan ID.
    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GameDetail

    companion object {

        // Membuat instance Retrofit yang digunakan aplikasi.
        fun create(): RawgApiService {
            return retrofit2.Retrofit.Builder()
                .baseUrl("https://api.rawg.io/api/")
                .addConverterFactory(
                    retrofit2.converter.gson.GsonConverterFactory.create()
                )
                .build()
                .create(RawgApiService::class.java)
        }
    }
}