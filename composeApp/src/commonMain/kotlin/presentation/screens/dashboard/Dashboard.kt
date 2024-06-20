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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
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
import presentation.screens.components.CustomTopAppBar
import presentation.screens.components.DashboardCard
import presentation.screens.components.DashboardChart
import presentation.screens.components.DashboardPieChart
import presentation.screens.components.RecentOrdersCard
import presentation.screens.components.SidebarMenu
import presentation.screens.components.TrafficSourceCard
import presentation.screens.components.TransactionsCard

@Composable
fun Dashboard(windowSizeClass: WindowSizeClass) {
    var selectedMenuItem by remember { mutableStateOf("Dashboard") }
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFe6f0f9))) {
        CustomTopAppBar(windowSizeClass)
        Row(modifier = Modifier.fillMaxSize()) {
            SidebarMenu(isCompact, selectedMenuItem) { selectedMenuItem = it }
            DashboardContent(isCompact, selectedMenuItem)
        }
    }
}

@Composable
fun DashboardContent(isCompact: Boolean, selectedMenuItem: String) {
    when (selectedMenuItem) {
        "Dashboard" -> DashboardMainContent()
        "Products" -> Text("Products Screen")
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
fun DashboardMainContent() {
    val isCompact = calculateWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Compact
    val columns = if (isCompact) 1 else 2

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
                        value = "400",
                        percentage = "15%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Checklist,
                        background = Color(0XFF51cd85)
                    )
                    DashboardCard(
                        title = "Average Daily Sales",
                        value = "\$6433",
                        percentage = "25%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.CurrencyBitcoin,
                        background = Color(0XFF723bde)
                    )
                    DashboardCard(
                        title = "New Customers This Month",
                        value = "8.9K",
                        percentage = "18%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.ShoppingBag,
                        background = Color(0XFF3e93f6)
                    )
                    DashboardCard(
                        title = "Pending Orders",
                        value = "563",
                        percentage = "10%",
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
                DashboardChart(title = "Sales Statistics", modifier = Modifier.weight(1f))
                DashboardPieChart(title = "Most Selling Category", modifier = Modifier.weight(1f))
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
                RecentOrdersCard(modifier = Modifier.weight(1f))
                TrafficSourceCard(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}