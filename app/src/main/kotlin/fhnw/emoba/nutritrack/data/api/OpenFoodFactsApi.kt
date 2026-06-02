package fhnw.emoba.nutritrack.data.api

import fhnw.emoba.nutritrack.data.model.Product
import fhnw.emoba.nutritrack.data.model.ProductSearchResponse
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") query: String,
        @Query("search_simple") simple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("fields") fields: String = "product_name,brands,nutriments,image_url,serving_size"
    ): ProductSearchResponse

    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): ProductDetailResponse
}

@Serializable
data class ProductDetailResponse(
    val product: Product? = null,
    val status: Int = 0
)