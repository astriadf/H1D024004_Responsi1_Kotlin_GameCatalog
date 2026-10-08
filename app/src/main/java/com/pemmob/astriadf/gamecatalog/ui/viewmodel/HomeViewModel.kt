package com.pemmob.astriadf.gamecatalog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.astriadf.gamecatalog.data.model.Game
import com.pemmob.astriadf.gamecatalog.data.remote.RawgApiService
import com.pemmob.astriadf.gamecatalog.data.repository.GameRepository
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

// State yang digunakan Home Screen.
data class HomeUiState(
    val searchQuery: String = "",
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ViewModel untuk mengelola data Home Screen.
class HomeViewModel : ViewModel() {

    private val repository = GameRepository(
        RawgApiService.create()
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    // Job untuk membatalkan pencarian sebelumnya ketika pengguna masih mengetik kata pencarian baru.
    private var searchJob: Job? = null

    init {
        // Saat Home pertama kali dibuka, tampilkan daftar game populer berdasarkan rating.
        loadGames("")
    }

    // Dipanggil setiap kali isi search bar berubah.
    fun onSearchQueryChange(query: String) {

        _uiState.update {
            it.copy(
                searchQuery = query,
                errorMessage = null
            )
        }

        // Membatalkan request pencarian sebelumnya.
        searchJob?.cancel()

        // Delay agar tidak memanggil API untuk setiap huruf secara cepat ketika pengguna sedang mengetik.
        searchJob = viewModelScope.launch {
            delay(500)
            loadGames(query)
        }
    }

    // Mengambil data game dari Repository.
    private fun loadGames(search: String) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val games = repository.getGames(search)

                _uiState.update {
                    it.copy(
                        games = games,
                        isLoading = false
                    )
                }

            } catch (e: IOException) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Tidak dapat terhubung ke internet."
                    )
                }

            } catch (e: HttpException) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Gagal mengambil data. Periksa API Key RAWG."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                            ?: "Terjadi kesalahan."
                    )
                }
            }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}