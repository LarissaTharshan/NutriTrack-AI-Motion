package fhnw.emoba.nutritrack.ui.screens.search

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fhnw.emoba.nutritrack.data.model.Product
import fhnw.emoba.nutritrack.data.repository.FoodRepository
import fhnw.emoba.nutritrack.ui.state.UiState
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodRepository()

    var query by mutableStateOf("")
    var uiState by mutableStateOf<UiState<List<Product>>>(UiState.Idle)
    var selectedProduct by mutableStateOf<Product?>(null)

    fun onQueryChange(newQuery: String) {
        if (newQuery.length <= 100) query = newQuery
    }

    fun search() {
        if (query.isBlank()) return
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = repository.searchProducts(query)
        }
    }

    fun selectProduct(product: Product) {
        selectedProduct = product
    }
}
