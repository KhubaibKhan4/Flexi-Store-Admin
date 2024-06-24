package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.order.Orders
import domain.model.products.Products
import domain.repository.Repository
import domain.usecase.UiState
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository,
) : ViewModel() {

    private val _allOrders: MutableStateFlow<UiState<List<Orders>>> =
        MutableStateFlow(UiState.LOADING)
    val allOrders = _allOrders.asStateFlow()

    private val _productDetail: MutableStateFlow<UiState<List<Products>>> =
        MutableStateFlow(UiState.LOADING)
    val productDetail = _productDetail.asStateFlow()

    private val _allProducts: MutableStateFlow<UiState<List<Products>>> =
        MutableStateFlow(UiState.LOADING)
    val allProducts = _allProducts.asStateFlow()

    private val _updateProduct: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val updateProduct = _updateProduct.asStateFlow()

    private val _createProduct: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val createProduct = _createProduct.asStateFlow()
    fun getAllOrders() {
        viewModelScope.launch {
            _allOrders.value = UiState.LOADING
            try {
                val response = repository.getAllOrders()
                _allOrders.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _allOrders.value = UiState.ERROR(e)
            }
        }
    }

    fun getProductsByIds(ids: String) {
        viewModelScope.launch {
            _productDetail.value = UiState.LOADING
            try {
                val response = repository.getProductsByMultipleIds(ids)
                _productDetail.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _productDetail.value = UiState.ERROR(e)
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _allProducts.value = UiState.LOADING
            try {
                val response = repository.getAllProducts()
                _allProducts.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _allProducts.value = UiState.ERROR(e)
            }
        }
    }

    fun updateProduct(
        id: Long,
        name: String?,
        description: String?,
        price: Long?,
        categoryId: Long?,
        categoryTitle: String?,
        imageBytes: ByteArray?,
        created_at: String?,
        updated_at: String?,
        total_stack: Long?,
        brand: String?,
        weight: Double?,
        dimensions: String?,
        isAvailable: Boolean?,
        discountPrice: Long?,
        promotionDescription: String?,
        averageRating: Double?,
        isFeature: Boolean?,
        manufacturer: String?,
        colors: String?,
    ) {
        viewModelScope.launch {
            _updateProduct.value = UiState.LOADING
            try {
                val response = repository.updateProductById(
                    id,
                    name,
                    description,
                    price,
                    categoryId,
                    categoryTitle,
                    imageBytes,
                    created_at,
                    updated_at,
                    total_stack,
                    brand,
                    weight,
                    dimensions,
                    isAvailable,
                    discountPrice,
                    promotionDescription,
                    averageRating,
                    isFeature,
                    manufacturer,
                    colors
                )
                _updateProduct.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _updateProduct.value = UiState.ERROR(e)
            }
        }
    }

    fun createProduct(
        name: String,
        description: String,
        price: Long,
        categoryId: Long,
        categoryTitle: String,
        imageBytes: ByteArray,
        createdAt: String,
        updatedAt: String,
        totalStack: Long,
        brand: String,
        weight: Double,
        dimensions: String,
        isAvailable: Boolean,
        discountPrice: Long,
        promotionDescription: String,
        averageRating: Double,
        isFeature: Boolean,
        manufacturer: String,
        colors: String,
    ) {
        viewModelScope.launch {
            _createProduct.value = UiState.LOADING
            try {
                val response = repository.createProduct(name, description, price, categoryId, categoryTitle, imageBytes, createdAt, updatedAt, totalStack, brand, weight, dimensions, isAvailable, discountPrice, promotionDescription, averageRating, isFeature, manufacturer, colors)
                _createProduct.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _createProduct.value = UiState.ERROR(e)
            }
        }
    }

}