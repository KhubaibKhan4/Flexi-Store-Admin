import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CurrencyBitcoin
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.StackedLineChart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import flexi_store_admin.composeapp.generated.resources.Res
import flexi_store_admin.composeapp.generated.resources.avatar_1
import flexi_store_admin.composeapp.generated.resources.avatar_2
import flexi_store_admin.composeapp.generated.resources.avatar_3
import flexi_store_admin.composeapp.generated.resources.avatar_4
import org.jetbrains.compose.resources.painterResource
import presentation.screens.components.CustomTopAppBar
import presentation.screens.components.SidebarMenu

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App() {
    val windowSizeClass = calculateWindowSizeClass()
    MaterialTheme {
        Dashboard(windowSizeClass)
    }
}

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
fun MenuItem(
    name: String,
    icon: ImageVector,
    subMenuItems: List<String> = emptyList(),
    selectedMenuItem: String,
    onMenuItemClick: (String) -> Unit,
) {
    val backgroundColor by animateColorAsState(if (selectedMenuItem == name) Color(0xFF007BFF) else Color.Transparent)
    val contentColor by animateColorAsState(if (selectedMenuItem == name) Color.White else Color.Black)
    val fontWeight = if (selectedMenuItem == name) FontWeight.Bold else FontWeight.Normal
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .clickable {
                    onMenuItemClick(name)
                    if (subMenuItems.isNotEmpty()) expanded = !expanded
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = name, tint = contentColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(name, color = contentColor, fontWeight = fontWeight)
            if (subMenuItems.isNotEmpty()) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = contentColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        if (expanded) {
            subMenuItems.forEach { subMenuItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                        .clickable { onMenuItemClick(subMenuItem) }
                ) {
                    Text(subMenuItem, color = Color.Gray)
                }
            }
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

@Composable
fun DashboardCard(
    title: String,
    value: String,
    percentage: String,
    modifier: Modifier,
    icon: ImageVector,
    background: Color,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = title, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(text = percentage, fontSize = 14.sp, color = background)
                    Icon(
                        imageVector = Icons.Outlined.StackedLineChart,
                        contentDescription = null,
                        tint = background
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(color = background, shape = CircleShape)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

data class DataPoint(val x: Float, val y: Float)

@Composable
fun DashboardChart(title: String, modifier: Modifier = Modifier) {
    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "Sales",
            data = listOf(70.0, 00.0, 50.33, 40.0, 100.500, 50.0),
            lineColor = Color.Gray,
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        ),
        LineParameters(
            label = "Visitors",
            data = listOf(60.0, 80.6, 40.33, 86.232, 88.0, 90.0),
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Products",
            data = listOf(1.0, 40.0, 11.33, 55.23, 1.0, 100.0),
            lineColor = Color(0xFF81BE88),
            lineType = LineType.CURVED_LINE,
            lineShadow = false,
        )
    )
    val dataPoints = listOf(
        DataPoint(0f, 100f),
        DataPoint(1f, 120f),
        DataPoint(2f, 90f),
        DataPoint(3f, 160f),
        DataPoint(4f, 130f),
        DataPoint(5f, 180f)
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            LineChart(
                modifier = Modifier.fillMaxWidth()
                    .height(300.dp),
                linesParameters = testLineParameters,
                isGrid = true,
                gridColor = Color.Blue,
                xAxisData = listOf("0", "15", "25", "50", "75", "100"),
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                ),
                xAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 14,
                oneLineChart = false,
                gridOrientation = GridOrientation.VERTICAL
            )
        }
    }
}

@Composable
fun DashboardPieChart(title: String, modifier: Modifier) {
    val data = listOf(
        "Grocery" to 30f,
        "Women" to 25f,
        "Men" to 20f,
        "" to 15f,
        "" to 10f
    )
    val colors = listOf(Color.Green, Color.Blue, Color.Red, Color.Yellow, Color.Gray)
    val total = data.sumOf { it.second.toDouble() }.toFloat()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            val testPieChartData: List<PieChartData> = listOf(
                PieChartData(
                    partName = "Grocery",
                    data = 500.0,
                    color = Color(0xFF22A699),
                ),
                PieChartData(
                    partName = "Women",
                    data = 700.0,
                    color = Color(0xFFF2BE22),
                ),
                PieChartData(
                    partName = "Men",
                    data = 500.0,
                    color = Color(0xFFF29727),
                ),
                PieChartData(
                    partName = "Kids",
                    data = 100.0,
                    color = Color(0xFFF24C3D),
                ),
                PieChartData(
                    partName = "Other",
                    data = 125.0,
                    color = Color(0xFFF24C9D),
                ),
            )

            PieChart(
                modifier = Modifier.height(300.dp),
                pieChartData = testPieChartData,
                ratioLineColor = Color.LightGray,
                textRatioStyle = TextStyle(color = Color.Gray),
            )
        }
    }
}

@Composable
fun TransactionsCard(modifier: Modifier) {
    val transactions = listOf(
        "Esther Howard" to "\$302.33" to Res.drawable.avatar_1,
        "Robert Fox" to "-\$192.63" to Res.drawable.avatar_2,
        "Brooklyn Simmons" to "\$602.50" to Res.drawable.avatar_3,
        "Cameron Williamson" to "\$602.00" to Res.drawable.avatar_4
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Transactions", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            transactions.forEach { (name, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray, shape = CircleShape)
                    ) {
                        Image(
                            painter = painterResource(amount),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = name.first, fontSize = 14.sp)
                        Text(
                            text = name.second, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                            color = if (name.second.contains("-")) Color.Red else Color.Green
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecentOrdersCard(modifier: Modifier) {
    val orders = listOf(
        Triple("Samsung TV 32\"", "#KY6133", "$368") to "Active",
        Triple("Apple Macbook Pro 17\"", "#KY5126", "$1400") to "Disabled",
        Triple("Iphone 11 Pro", "#KY1999", "$999") to "Active",
        Triple("ASUS Gaming laptop", "#KY1623", "$1500") to "Disabled",
        Triple("Lenovo PC", "#KY1623", "$1230") to "Active",
        Triple("OPPO Folding Phone", "#KY6126", "$1400") to "Active",
        Triple("Bag Pack", "#KY1666", "$66") to "Active"
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Recent Orders", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            orders.forEach { (order, status) ->
                val (item, id, price) = order
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = item, fontSize = 14.sp)
                        Text(text = id, fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = price, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status,
                        fontSize = 14.sp,
                        color = if (status == "Active") Color.Green else Color.Red,
                        modifier = Modifier
                            .background(
                                color = if (status == "Active") Color.Green.copy(alpha = 0.1f) else Color.Red.copy(
                                    alpha = 0.1f
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TrafficSourceCard(modifier: Modifier) {
    val sources = listOf(
        "Facebook" to 77,
        "Youtube" to 35,
        "Instagram" to 59,
        "Twitter" to 23,
        "Others" to 46
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Traffic Source", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            sources.forEach { (source, percentage) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = source, fontSize = 14.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "$percentage%", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = percentage / 100f,
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}