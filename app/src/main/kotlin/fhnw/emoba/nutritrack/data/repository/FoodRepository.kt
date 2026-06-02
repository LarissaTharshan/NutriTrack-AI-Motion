package fhnw.emoba.nutritrack.data.repository

import fhnw.emoba.nutritrack.data.api.RetrofitInstance
import fhnw.emoba.nutritrack.data.model.Product
import fhnw.emoba.nutritrack.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository {
    private val api = RetrofitInstance.api

    suspend fun searchProducts(query: String): UiState<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.searchProducts(query)
                if (response.products.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(response.products)
                }
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Unbekannter Fehler")
            }
        }
}
