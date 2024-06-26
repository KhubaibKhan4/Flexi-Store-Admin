package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.categories.Categories
import domain.model.order.Orders
import domain.model.products.Products
import domain.model.promotions.Promotion
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

    private val _updateCategories: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val updateCategories = _updateCategories.asStateFlow()


    private val _updateOrder: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val updateOrder = _updateCategories.asStateFlow()

    private val _createProduct: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val createProduct = _createProduct.asStateFlow()

    private val _createCategory: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val createCategory = _createCategory.asStateFlow()

    private val _categories: MutableStateFlow<UiState<List<Categories>>> =
        MutableStateFlow(UiState.LOADING)
    val categories = _categories.asStateFlow()

    private val _promotions: MutableStateFlow<UiState<List<Promotion>>> =
        MutableStateFlow(UiState.LOADING)
    val promotions = _promotions.asStateFlow()

    private val _deletePromo: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val deletePromo = _deletePromo.asStateFlow()

    private val _updatePromo: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val updatePromo = _updatePromo.asStateFlow()

    private val _createPromo: MutableStateFlow<UiState<HttpResponse>> =
        MutableStateFlow(UiState.LOADING)
    val createPromo = _createPromo.asStateFlow()
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

    fun createCategories(
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,
    ) {
        viewModelScope.launch {
            _createCategory.value = UiState.LOADING
            try {
                val response = repository.createCategories(
                    name,
                    description,
                    isVisible,
                    imageBytes,
                )
                _createCategory.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _createCategory.value = UiState.ERROR(e)
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
                val response = repository.createProduct(
                    name,
                    description,
                    price,
                    categoryId,
                    categoryTitle,
                    imageBytes,
                    createdAt,
                    updatedAt,
                    totalStack,
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
                _createProduct.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _createProduct.value = UiState.ERROR(e)
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _categories.value = UiState.LOADING
            try {
                val response = repository.getAllCategories()
                _categories.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _categories.value = UiState.ERROR(e)
            }
        }
    }

    fun updateCategoryById(
        id: Long,
        name: String,
        description: String,
        isVisible: Boolean,
        imageBytes: ByteArray,
    ) {
        viewModelScope.launch {
            _updateCategories.value = UiState.LOADING
            try {
                val response =
                    repository.updateCategoryById(id, name, description, isVisible, imageBytes)
                _updateCategories.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _updateCategories.value = UiState.ERROR(e)
            }
        }
    }

    fun updateOrderById(id: Long, orderProgress: String) {
        viewModelScope.launch {
            _updateOrder.value = UiState.LOADING
            println("VIEWMODEL: LOADING")
            try {
                val response = repository.updateOrderById(id, orderProgress)
                _updateOrder.value = UiState.SUCCESS(response)
                println("VIEWMODEL: SUCCESS: $response")
            } catch (e: Exception) {
                _updateOrder.value = UiState.ERROR(e)
                println("VIEWMODEL: ERROR: $e")
            }
        }
    }

    fun getPromotions() {
        viewModelScope.launch {
            _promotions.value = UiState.LOADING
            try {
                val response = repository.getPromotions()
                _promotions.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _promotions.value = UiState.ERROR(e)
            }
        }
    }

    fun deletePromotion(id: Long) {
        viewModelScope.launch {
            _deletePromo.value = UiState.LOADING
            try {
                val response = repository.deletePromotionById(id)
                _deletePromo.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _deletePromo.value = UiState.ERROR(e)
            }
        }
    }

    fun updatePromotionById(
        id: Long,
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        enable: String,
        imageBytes: ByteArray,
    ) {
        viewModelScope.launch {
            _updatePromo.value = UiState.LOADING
            try {
                val response = repository.updatePromotionById(
                    id,
                    title,
                    description,
                    startDate,
                    endDate,
                    enable,
                    imageBytes
                )
                _updatePromo.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _updatePromo.value = UiState.ERROR(e)
            }
        }
    }

    fun createPromo(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        enable: Boolean,
        image: ByteArray,
    ) {
        viewModelScope.launch {
            _createPromo.value = UiState.LOADING
            try {
                val response = repository.createPromotion(
                    title,
                    description,
                    startDate,
                    endDate,
                    enable,
                    image
                )
                _createPromo.value = UiState.SUCCESS(response)
            } catch (e: Exception) {
                _createPromo.value = UiState.ERROR(e)
            }
        }
    }

}