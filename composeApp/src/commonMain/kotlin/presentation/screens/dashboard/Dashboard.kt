package presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CurrencyBitcoin
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.order.Orders
import domain.model.products.Products
import domain.usecase.UiState
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import presentation.screens.ProductContent
import presentation.screens.components.CustomTopAppBar
import presentation.screens.components.DashboardCard
import presentation.screens.components.DashboardChart
import presentation.screens.components.DashboardPieChart
import presentation.screens.components.ErrorScreen
import presentation.screens.components.LoadingScreen
import presentation.screens.components.RecentOrdersCard
import presentation.screens.components.SidebarMenu
import presentation.screens.components.TrafficSourceCard
import presentation.screens.components.TransactionsCard
import presentation.viewmodel.MainViewModel

@Composable
fun Dashboard(
    windowSizeClass: WindowSizeClass,
    viewModel: MainViewModel = koinInject(),
) {
    var selectedMenuItem by remember { mutableStateOf("Dashboard") }
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFe6f0f9))) {
        CustomTopAppBar(windowSizeClass)
        Row(modifier = Modifier.fillMaxSize()) {
            SidebarMenu(isCompact, selectedMenuItem) { selectedMenuItem = it }
            DashboardContent(isCompact, selectedMenuItem, viewModel)
        }
    }
}

@Composable
fun DashboardContent(isCompact: Boolean, selectedMenuItem: String, viewModel: MainViewModel) {
    var orderList by remember { mutableStateOf(emptyList<Orders>()) }
    var productList by remember { mutableStateOf(emptyList<Products>()) }

    LaunchedEffect(Unit) {
        delay(3000)
        viewModel.getAllOrders()
    }


    val orderState by viewModel.allOrders.collectAsState()
    val productsState by viewModel.productDetail.collectAsState()
    when (orderState) {
        is UiState.ERROR -> {
            val error = (orderState as UiState.ERROR).throwable
            ErrorScreen(errorMessage = error.message ?: "Unknown error", onRetry = {
                viewModel.getAllOrders()
            })
        }

        UiState.LOADING -> {
            LoadingScreen()
        }

        is UiState.SUCCESS -> {
            val orders = (orderState as UiState.SUCCESS).response
            orderList = orders
            val idsList = orders.map { it.productIds.toString().trim() }.joinToString(separator = ",")
            println("PRODUCT: $idsList")
            LaunchedEffect(orderList) {
                viewModel.getProductsByIds(idsList)
                println("PRODUCT: $idsList")
            }
        }
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
            println("PRODUCT: $productList")
        }
    }
    when (selectedMenuItem) {
        "Dashboard" -> DashboardMainContent(viewModel,productList,orderList)
        "Products" -> ProductContent(productList,isCompact)
        "Categories" -> Text("Categories Screen")
        "Orders" -> Text("Orders Screen")
        "Reviews" -> Text("Reviews Screen")
        "Coupons" -> Text("Coupons Screen")
        "Profile" -> Text("Profile Screen")
        "Shop Settings" -> Text("Shop Settings Screen")
        "Pages" -> Text("Pages Screen")
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DashboardMainContent(viewModel: MainViewModel, productList: List<Products>, orderList: List<Orders>) {
    val isCompact = calculateWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Compact
    val columns = if (isCompact) 1 else 2
    val totalSales = orderList.sumOf { it.totalPrice }
    val averageDailySales = if (orderList.isNotEmpty()) totalSales / orderList.size else 0
    val newCustomers = orderList.count { it.orderProgress == "On Progress" }
    val pendingOrders = orderList.count { it.orderProgress == "On The Way" }

    fun parseDate(dateString: String): LocalDate {
        val parts = dateString.split(" ")
        val month = when (parts[1]) {
            "Jan" -> 1
            "Feb" -> 2
            "Mar" -> 3
            "Apr" -> 4
            "May" -> 5
            "Jun" -> 6
            "Jul" -> 7
            "Aug" -> 8
            "Sep" -> 9
            "Oct" -> 10
            "Nov" -> 11
            "Dec" -> 12
            else -> 1
        }
        val day = parts[2].toInt()
        val year = parts[5].toInt()
        return LocalDate(year, month, day)
    }

    fun filterOrdersByDate(
        orders: List<Orders>,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<Orders> {
        return orders.filter {
            val orderDate = parseDate(it.orderDate)
            orderDate in startDate..endDate
        }
    }

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val currentStartDate = today.minus(1, DateTimeUnit.MONTH)
    val previousStartDate = currentStartDate.minus(1, DateTimeUnit.MONTH)
    val previousEndDate = currentStartDate.minus(1, DateTimeUnit.DAY)

    val currentOrders = filterOrdersByDate(orderList, currentStartDate, today)
    val previousOrders = filterOrdersByDate(orderList, previousStartDate, previousEndDate)

    val previousTotalSales = previousOrders.sumOf { it.totalPrice }
    val previousNewCustomers = previousOrders.count { it.orderProgress == "On Progress" }
    val previousPendingOrders = previousOrders.count { it.orderProgress == "On The Way" }

    val salesPercentageChange =
        if (previousTotalSales != 0) ((totalSales - previousTotalSales).toDouble() / previousTotalSales * 100).toInt() else 0
    val newCustomersPercentageChange =
        if (previousNewCustomers != 0) ((newCustomers - previousNewCustomers).toDouble() / previousNewCustomers * 100).toInt() else 0
    val pendingOrdersPercentageChange =
        if (previousPendingOrders != 0) ((pendingOrders - previousPendingOrders).toDouble() / previousPendingOrders * 100).toInt() else 0


    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Dashboard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007BFF)
                    )
                    Text(
                        "Welcome to your dashboard",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ElevatedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0XFF0a8af9),
                        contentColor = Color.White
                    )
                ) {
                    Text("Add Product")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "Orders Received",
                        value = orderList.size.toString(),
                        percentage = "15%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Checklist,
                        background = Color(0XFF51cd85)
                    )
                    DashboardCard(
                        title = "Average Daily Sales",
                        value = "\$$averageDailySales",
                        percentage = "${salesPercentageChange}%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.CurrencyBitcoin,
                        background = Color(0XFF723bde)
                    )
                    DashboardCard(
                        title = "New Customers This Month",
                        value = "$newCustomers",
                        percentage = "${newCustomersPercentageChange}%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.ShoppingBag,
                        background = Color(0XFF3e93f6)
                    )
                    DashboardCard(
                        title = "Pending Orders",
                        value = "$pendingOrders",
                        percentage = "${pendingOrdersPercentageChange}%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Pending,
                        background = Color(0XFFfe9603)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardChart(
                    title = "Sales Statistics",
                    modifier = Modifier.weight(1f),
                    orders = orderList,
                    products = productList
                )
                DashboardPieChart(
                    title = "Most Selling Category",
                    modifier = Modifier.weight(1f),
                    orders = orderList,
                    products = productList
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransactionsCard(modifier = Modifier.weight(1f))
                RecentOrdersCard(modifier = Modifier.weight(1f),orderList,productList)
                TrafficSourceCard(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
fun aggregateSalesDataByCategory(
    orders: List<Orders>,
    products: List<Products>
): Map<String, Double> {
    val categoryMap = products.associate { it.id.toString() to it.categoryTitle }

    val categorySales = mutableMapOf<String, Double>().withDefault { 0.0 }

    orders.forEach { order ->
        val categoryTitle = categoryMap[order.productIds.toString()]
        if (categoryTitle != null) {
            categorySales[categoryTitle] = categorySales.getValue(categoryTitle) + order.totalPrice.toDouble()
        }
    }

    return categorySales
}
