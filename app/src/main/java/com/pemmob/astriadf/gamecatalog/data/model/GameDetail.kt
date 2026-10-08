package com.pemmob.astriadf.gamecatalog.data.model

// Model data untuk halaman detail game.
data class GameDetail(
    val id: Int,
    val name: String,
    val rating: Double,
    val released: String?,
    val description: String?
)