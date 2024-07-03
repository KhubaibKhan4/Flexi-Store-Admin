package presentation.screens.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import domain.model.order.Orders
import domain.model.products.Products
import domain.usecase.UiState
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.statement.HttpResponse
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL
import utils.DateUtils

@Serializable
data class OrderProduct(
    val order: Orders,
    val product: Products,
)

class OrderScreen(
    private val orderList: List<Orders>,
) : Screen {
    @Composable
    override fun Content() {
        OrderScreenContent(orderList)
    }
}
@Composable
fun OrderScreenContent(orderList: List<Orders>) {
    val viewModel = koinInject<MainViewModel>()
    val combinedList = remember { mutableStateOf(emptyList<OrderProduct>()) }
    val idsList = orderList.map { it.productIds.toString().trim() }.joinToString(separator = ",")
    val productsState by viewModel.productDetail.collectAsState()
    var productList by remember { mutableStateOf(emptyList<Products>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var searchVisible by remember { mutableStateOf(false) }

    LaunchedEffect(orderList) {
        viewModel.getProductsByIds(idsList)
    }

    when (productsState) {
        is UiState.LOADING -> {
            CircularProgressIndicator()
        }

        is UiState.ERROR -> {
            val error = (productsState as UiState.ERROR).throwable
            Text("Error loading products: ${error.message}")
        }

        is UiState.SUCCESS -> {
            val products = (productsState as UiState.SUCCESS).response
            productList = products
            combinedList.value = orderList.mapNotNull { order ->
                val product = products.find { it.id == order.productIds }
                product?.let { OrderProduct(order, it) }
            }
        }
    }

    Column(modifier = Modifier.background(Color(0XFFe5f0f9)).fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Orders",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                var statusMenuExpanded by remember { mutableStateOf(false) }

                Box {
                    Text(
                        text = selectedStatus,
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Gray))
                            .padding(8.dp)
                            .clickable { statusMenuExpanded = true }
                    )
                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        val statuses = listOf(
                            "All",
                            "On Progress",
                            "On The Way",
                            "Completed",
                            "Delivered",
                            "Cancelled"
                        )
                        statuses.forEach { status ->
                            DropdownMenuItem(onClick = {
                                selectedStatus = status
                                statusMenuExpanded = false
                            }, text = {
                                Text(status)
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (searchVisible) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Gray))
                            .padding(8.dp)
                            .weight(1f)
                            .padding(end = 8.dp),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text("Search...", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.clickable { searchVisible = true }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 340.dp),
            modifier = Modifier.fillMaxWidth().height(900.dp)
        ) {
            items(combinedList.value.filter {
                (selectedStatus == "All" || it.order.orderProgress == selectedStatus) &&
                        (searchQuery.isEmpty() || it.product.name.contains(
                            searchQuery,
                            ignoreCase = true
                        ))
            }) { orderProduct ->
                OrderProductItem(orderProduct)
            }
        }
    }
}

@Composable
fun OrderProductList(orderProducts: List<OrderProduct>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxWidth().height(900.dp)
    ) {
        items(orderProducts) { orderProduct ->
            OrderProductItem(orderProduct = orderProduct)
        }
    }
}

@Composable
fun OrderProductItem(
    orderProduct: OrderProduct,
    viewModel: MainViewModel = koinInject(),
) {
    val product = orderProduct.product
    var order by remember { mutableStateOf(orderProduct.order) }
    var showEditDialog by remember { mutableStateOf(false) }
    var serverResponse by remember { mutableStateOf<HttpResponse?>(null) }
    val updateOrderState by viewModel.updateOrder.collectAsState()

    val (progress, progressLabel) = when (order.orderProgress) {
        "On Progress" -> 0.25f to "Order Received"
        "On The Way" -> 0.5f to "On The Way"
        "Completed" -> 1f to "Delivered"
        "Delivered" -> 1f to "Delivered"
        "Cancelled" -> 0f to "Cancelled"
        else -> 0f to "Unknown"
    }

    if (showEditDialog) {
        EditOrderDialog(
            order = order,
            onDismiss = { showEditDialog = false },
            onSave = { updatedOrder ->
                viewModel.updateOrderById(updatedOrder.id.toLong(), updatedOrder.orderProgress)
                order = updatedOrder
            }
        )
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val image: Resource<Painter> = asyncPainterResource(BASE_URL + product.imageUrl)
            KamelImage(
                resource = image,
                contentDescription = product.name,
                modifier = Modifier.size(100.dp).padding(end = 16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.align(Alignment.CenterVertically).weight(1f)) {
                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "Price: \$${product.price}", color = Color.Gray, fontSize = 16.sp)
                Text(
                    text = "Order Date: ${product.createdAt}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(text = "Order ID: ${order.id}", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = when (progress) {
                        0f -> Color.Red
                        0.25f -> Color.Yellow
                        0.5f -> Color.Blue
                        1f -> Color.Green
                        else -> Color.Gray
                    },
                )
                Text(text = progressLabel, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedButton(
                    onClick = { showEditDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Order")
                }
            }
        }
    }

    when (updateOrderState) {
        is UiState.LOADING -> {
            // Handle loading state if needed
        }

        is UiState.SUCCESS -> {
            val response = (updateOrderState as UiState.SUCCESS).response
            serverResponse = response
            println("RESPONSE: $response")
        }

        is UiState.ERROR -> {
            val error = (updateOrderState as UiState.ERROR).throwable
            Text("Error updating order: ${error.message}")
            println("ERROR: $error")
        }
    }
}

@Composable
fun EditOrderDialog(
    order: Orders,
    onDismiss: () -> Unit,
    onSave: (Orders) -> Unit,
) {
    var progressStatus by remember { mutableStateOf(order.orderProgress) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Edit Order", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Order ID: ${order.id}", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Progress Status", color = Color.Gray, fontSize = 14.sp)

                Box {
                    Text(
                        text = progressStatus,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(8.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            progressStatus = "On Progress"; expanded = false
                        },
                            text = { Text("On Progress") })
                        DropdownMenuItem(onClick = {
                            progressStatus = "On The Way"; expanded = false
                        },
                            text = {
                                Text("On The Way")
                            })
                        DropdownMenuItem(onClick = {
                            progressStatus = "Completed"; expanded = false
                        },
                            text = {
                                Text("Delivered")
                            })
                        DropdownMenuItem(onClick = {
                            progressStatus = "Cancelled"; expanded = false
                        },
                            text = {
                                Text("Cancelled")
                            })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onSave(order.copy(orderProgress = progressStatus))
                            onDismiss()
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}