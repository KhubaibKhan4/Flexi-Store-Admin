import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import flexi_store_admin.composeapp.generated.resources.Res
import flexi_store_admin.composeapp.generated.resources.avatar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        CustomTopAppBar(windowSizeClass)
        Row(modifier = Modifier.fillMaxSize()) {
            SidebarMenu(isCompact)
            DashboardContent(isCompact)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(windowSizeClass: WindowSizeClass) {
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    var isSearchQuery by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(6.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Flexi-Store",
            color = Color(0xFF007BFF),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )
        Spacer(modifier = Modifier.width(120.dp))
        AnimatedVisibility(
            visible = !isCompact,
        ) {
            OutlinedTextField(
                value = isSearchQuery,
                onValueChange = {
                    isSearchQuery = it
                },
                modifier = Modifier
                    .padding(14.dp)
                    .weight(1f)
                    .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(6.dp)),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Image Search"
                    )
                },
                placeholder = {
                    Text(text = "Search Here ...")
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        val item = 4
        BadgedBox(badge = {
            if (item > 0) {
                Badge {
                    Text(text = item.toString())
                }
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.NotificationsActive,
                contentDescription = "Notifications"
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier.size(35.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.avatar),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Green, CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun SidebarMenu(isCompact: Boolean) {
    var expanded by remember { mutableStateOf(true) }

    val expandIcon = if (expanded) Icons.Default.ArrowBack else Icons.Default.ArrowForward
    val sidebarWidth by animateDpAsState(targetValue = if (expanded) 200.dp else 60.dp)

    Column(
        modifier = Modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(expandIcon, contentDescription = "Expand/Collapse Sidebar")
        }

        if (expanded) {
            MenuItem("Dashboard", Icons.Default.Dashboard)
            MenuItem("Products", Icons.Default.ShoppingCart, listOf("Add Product", "Product List"))
            MenuItem("Categories", Icons.Default.Category, listOf("Add Category", "Category List"))
            MenuItem("Orders", Icons.Default.Receipt, listOf("New Orders", "Completed Orders"))
            MenuItem("Reviews", Icons.Default.Star)
            MenuItem("Coupons", Icons.Default.LocalOffer)
            MenuItem("Profile", Icons.Default.Person)
            MenuItem("Shop Settings", Icons.Default.Settings)
            MenuItem("Pages", Icons.Default.Pages, listOf("About Us", "Contact Us"))
        }
    }
}

@Composable
fun MenuWithSubMenu(
    name: String,
    icon: ImageVector,
    selectedMenuItem: String,
    onMenuItemClick: (String) -> Unit,
    subMenuItems: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(if (selectedMenuItem == name) Color(0xFF007BFF) else Color.Transparent)
    val contentColor by animateColorAsState(if (selectedMenuItem == name) Color.White else Color.Black)
    val fontWeight = if (selectedMenuItem == name) FontWeight.Bold else FontWeight.Normal

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = name, tint = contentColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(name, color = contentColor, fontWeight = fontWeight)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = contentColor
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(animationSpec = tween(durationMillis = 300)),
            exit = shrinkHorizontally(animationSpec = tween(durationMillis = 300))
        ) {
            Column {
                subMenuItems.forEach { subMenuItem ->
                    Text(
                        text = subMenuItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 4.dp, bottom = 4.dp)
                            .clickable { onMenuItemClick(subMenuItem) }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    name: String,
    icon: ImageVector,
    subMenuItems: List<String> = emptyList(),
    selectedMenuItem: String = "Dashboard",
    onMenuItemClick: (String) -> Unit = {}
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardContent(isCompact: Boolean) {
    val columns = if (isCompact) 1 else 2

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 4
            ) {
                DashboardCard(title = "Orders Received", value = "400")
                DashboardCard(title = "Average Daily Sales", value = "\$6433")
                DashboardCard(title = "New Customers This Month", value = "8.9K")
                DashboardCard(title = "Pending Orders", value = "563")
                DashboardChart(title = "Sales Statistics")
                DashboardPieChart(title = "Most Selling Category")
                DashboardTransactions()
                DashboardRecentOrders()
                DashboardTrafficSource()
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun DashboardChart(title: String) {
    val data = listOf(
        Pair("Jan", 30),
        Pair("Feb", 20),
        Pair("Mar", 40),
        Pair("Apr", 50),
        Pair("May", 60),
        Pair("Jun", 70),
        Pair("Jul", 80),
        Pair("Aug", 90),
        Pair("Sep", 100),
        Pair("Oct", 110),
        Pair("Nov", 120),
        Pair("Dec", 130),
    )
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val maxValue = data.maxOf { it.second }
            val barWidth = size.width / data.size
            data.forEachIndexed { index, (label, value) ->
                val barHeight = (value.toFloat() / maxValue) * size.height
                drawRect(
                    color = Color(0xFF007BFF),
                    topLeft = Offset(x = index * barWidth, y = size.height - barHeight),
                    size = Size(width = barWidth, height = barHeight)
                )
            }
        }
    }
}

@Composable
fun DashboardPieChart(title: String) {
    val data = listOf(
        "Grocery" to 30f,
        "Women" to 25f,
        "Men" to 20f,
        "Kids" to 15f,
        "Other" to 10f
    )
    val colors = listOf(Color.Green, Color.Blue, Color.Red, Color.Yellow, Color.Gray)
    val total = data.sumOf { it.second.toDouble() }

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            var startAngle = 0f
            data.forEachIndexed { index, (category, value) ->
                val sweepAngle = (value / total.toFloat()) * 360f
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.minDimension, size.minDimension),
                    topLeft = Offset((size.width - size.minDimension) / 2, (size.height - size.minDimension) / 2)
                )
                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun DashboardTransactions() {
    val transactions = listOf(
        "Esther Howard" to "$302.33",
        "Robert Fox" to "-\$192.63",
        "Brooklyn Simmons" to "$602.50",
        "Cameron Williamson" to "$602.00"
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Transactions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        transactions.forEach { (name, amount) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name, fontSize = 14.sp, color = Color.Black)
                Text(text = amount, fontSize = 14.sp, color = if (amount.startsWith("-")) Color.Red else Color.Green)
            }
        }
    }
}

@Composable
fun DashboardRecentOrders() {
    val orders = listOf(
        Triple("Samsung TV 32\"", "#KY6133", "$368"),
        Triple("Apple Macbook Pro 17\"", "#KY5126", "$1400"),
        Triple("Iphone 11 Pro", "#KY6123", "$999"),
        Triple("ASUS Gaming laptop", "#KY1633", "$1562"),
        Triple("Lenovo PC", "#KY1623", "$1230"),
        Triple("OPPO Folding Phone", "#KY1632", "$866"),
        Triple("Bag Pack", "#KY1631", "$66")
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Recent Orders", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        orders.forEach { (item, productId, price) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item, fontSize = 14.sp, color = Color.Black)
                Text(text = productId, fontSize = 14.sp, color = Color.Gray)
                Text(text = price, fontSize = 14.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun DashboardTrafficSource() {
    val trafficSources = listOf(
        "Facebook" to 77,
        "YouTube" to 35,
        "Instagram" to 59,
        "Twitter" to 23,
        "Others" to 46
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Traffic Source", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        trafficSources.forEach { (source, percentage) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = source, fontSize = 14.sp, color = Color.Black)
                Text(text = "$percentage%", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}