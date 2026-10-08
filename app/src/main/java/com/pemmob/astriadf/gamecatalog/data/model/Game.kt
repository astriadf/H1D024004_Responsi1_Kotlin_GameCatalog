package com.pemmob.astriadf.gamecatalog.data.model

// Model data untuk game yang ditampilkan di daftar Home.
data class Game(
    val id: Int,
    val name: String,
    val rating: Double,
    val released: String?
)