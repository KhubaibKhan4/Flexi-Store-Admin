package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.order.Orders
import domain.model.products.Products
import domain.repository.Repository
import domain.usecase.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
) :ViewModel(){

    private val _allOrders: MutableStateFlow<UiState<List<Orders>>> = MutableStateFlow(UiState.LOADING)
    val allOrders  = _allOrders.asStateFlow()

    private val _productDetail: MutableStateFlow<UiState<List<Products>>> = MutableStateFlow(UiState.LOADING)
    val productDetail  = _productDetail.asStateFlow()

    private val _allProducts: MutableStateFlow<UiState<List<Products>>> = MutableStateFlow(UiState.LOADING)
    val allProducts  = _allProducts.asStateFlow()

    fun getAllOrders(){
        viewModelScope.launch {
            _allOrders.value = UiState.LOADING
            try {
                val response = repository.getAllOrders()
                _allOrders.value = UiState.SUCCESS(response)
            }catch (e: Exception){
                _allOrders.value = UiState.ERROR(e)
            }
        }
    }
    fun getProductsByIds(ids: String){
        viewModelScope.launch {
            _productDetail.value = UiState.LOADING
            try {
                val response = repository.getProductsByMultipleIds(ids)
                _productDetail.value = UiState.SUCCESS(response)
            }catch (e: Exception){
                _productDetail.value = UiState.ERROR(e)
            }
        }
    }
    fun getAllProducts(){
        viewModelScope.launch {
            _productDetail.value = UiState.LOADING
            try {
                val response = repository.getAllProducts()
                _productDetail.value = UiState.SUCCESS(response)
            }catch (e: Exception){
                _productDetail.value = UiState.ERROR(e)
            }
        }
    }

}