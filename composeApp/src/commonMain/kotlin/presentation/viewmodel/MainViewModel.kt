package presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.order.Orders
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

}