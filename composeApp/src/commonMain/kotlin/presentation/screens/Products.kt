package presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import domain.model.products.Products

@Composable
fun ProductContent(productList: List<Products>) {
    Text("Products Screen by Product Content")
}