package fhnw.emoba.nutritrack.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fhnw.emoba.nutritrack.data.model.Product
import fhnw.emoba.nutritrack.ui.state.UiState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    val listState = rememberLazyListState()

    LaunchedEffect(viewModel.uiState) {
        if (viewModel.uiState is UiState.Success) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lebensmittel suchen", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.query,
                    onValueChange = viewModel::onQueryChange,
                    label = { Text("z.B. Haferflocken") },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.search() }),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        when (val state = viewModel.uiState) {
            is UiState.Idle -> {}
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("Suche läuft...")
                    }
                }
            }

            is UiState.Empty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Keine Produkte gefunden.")
                }
            }

            is UiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Fehler: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }

            is UiState.Success -> {
                LazyColumn(state = listState) {
                    items(
                        state.data,
                        key = { it.name ?: it.hashCode().toString() }) { product ->
                        ProductListItem(
                            product = product,
                            onClick = { viewModel.selectProduct(product) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductListItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!product.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
        } else {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("?", style = MaterialTheme.typography.headlineMedium)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name ?: "Unbekannt",
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = product.brand ?: "",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            product.nutrition?.proteins?.let {
                Text(
                    text = "Protein: ${it}g / 100g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
