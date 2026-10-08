package com.pemmob.astriadf.gamecatalog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.astriadf.gamecatalog.data.model.GameDetail
import com.pemmob.astriadf.gamecatalog.data.remote.RawgApiService
import com.pemmob.astriadf.gamecatalog.data.repository.GameRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

// State untuk halaman detail.
data class GameDetailUiState(
    val game: GameDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ViewModel untuk mengambil dan mengelola detail game.
class GameDetailViewModel : ViewModel() {

    private val repository = GameRepository(
        RawgApiService.create()
    )

    private val _uiState = MutableStateFlow(
        GameDetailUiState()
    )

    val uiState = _uiState.asStateFlow()

    // Memuat detail berdasarkan ID game.
    fun loadGame(gameId: Int) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val game = repository.getGameDetail(gameId)

                _uiState.update {
                    it.copy(
                        game = game,
                        isLoading = false
                    )
                }

            } catch (e: IOException) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Tidak dapat terhubung ke internet."
                    )
                }

            } catch (e: HttpException) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Gagal mengambil detail game."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            e.message ?: "Terjadi kesalahan."
                    )
                }
            }
        }
    }
}