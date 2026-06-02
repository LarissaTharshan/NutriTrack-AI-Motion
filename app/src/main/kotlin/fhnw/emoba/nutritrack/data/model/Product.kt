package fhnw.emoba.nutritrack.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("product_name") val name: String? = null,
    @SerialName("brands") val brand: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("nutriments") val nutrition: NutritionInfo? = null,
    @SerialName("serving_size") val servingSize: String? = null
)

@Serializable
data class ProductSearchResponse(
    val products: List<Product> = emptyList(),
    val count: Int = 0
)