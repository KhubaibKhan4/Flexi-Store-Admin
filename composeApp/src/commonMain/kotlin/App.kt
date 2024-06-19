import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import flexi_store_admin.composeapp.generated.resources.Res
import flexi_store_admin.composeapp.generated.resources.avatar
import org.jetbrains.compose.resources.painterResource

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

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        CustomTopAppBar(windowSizeClass)
        Row(modifier = Modifier.fillMaxSize()) {
            SidebarMenu(isCompact, selectedMenuItem) { selectedMenuItem = it }
            DashboardContent(isCompact, selectedMenuItem)
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
fun SidebarMenu(isCompact: Boolean, selectedMenuItem: String, onMenuItemClick: (String) -> Unit) {
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
            MenuItem("Dashboard", Icons.Default.Dashboard, selectedMenuItem = selectedMenuItem, onMenuItemClick = onMenuItemClick)
            MenuItem("Products", Icons.Default.ShoppingCart, listOf("Add Product", "Product List"), selectedMenuItem, onMenuItemClick)
            MenuItem("Categories", Icons.Default.Category, listOf("Add Category", "Category List"), selectedMenuItem, onMenuItemClick)
            MenuItem("Orders", Icons.Default.Receipt, listOf("New Orders", "Completed Orders"), selectedMenuItem, onMenuItemClick)
            MenuItem("Reviews", Icons.Default.Star, selectedMenuItem = selectedMenuItem, onMenuItemClick = onMenuItemClick)
            MenuItem("Coupons", Icons.Default.LocalOffer, selectedMenuItem = selectedMenuItem, onMenuItemClick = onMenuItemClick)
            MenuItem("Profile", Icons.Default.Person, selectedMenuItem = selectedMenuItem, onMenuItemClick = onMenuItemClick)
            MenuItem("Shop Settings", Icons.Default.Settings, selectedMenuItem = selectedMenuItem, onMenuItemClick = onMenuItemClick)
            MenuItem("Pages", Icons.Default.Pages, listOf("About Us", "Contact Us"), selectedMenuItem, onMenuItemClick)
        }
    }
}

@Composable
fun MenuItem(
    name: String,
    icon: ImageVector,
    subMenuItems: List<String> = emptyList(),
    selectedMenuItem: String,
    onMenuItemClick: (String) -> Unit
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
        "Dashboard" -> DashboardMainContent(isCompact)
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardMainContent(isCompact: Boolean) {
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
                maxItemsInEachRow = columns
            ) {
                DashboardCard(title = "Orders Received", value = "400", percentage = "15%")
                DashboardCard(title = "Average Daily Sales", value = "\$6433", percentage = "25%")
                DashboardCard(title = "New Customers This Month", value = "8.9K", percentage = "18%")
                DashboardCard(title = "Pending Orders", value = "563", percentage = "10%")
                DashboardChart(title = "Sales Statistics")
                DashboardPieChart(title = "Most Selling Category")
                TransactionsCard()
                RecentOrdersCard()
                TrafficSourceCard()
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, percentage: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = percentage, fontSize = 14.sp, color = Color.Green)
        }
    }
}

@Composable
fun DashboardChart(title: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 4f
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
    val total = data.sumOf { it.second.toDouble() }.toFloat()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                var startAngle = 0f
                data.forEachIndexed { index, (_, value) ->
                    val sweepAngle = (value / total) * 360f
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
}

@Composable
fun TransactionsCard() {
    val transactions = listOf(
        "Esther Howard" to "\$302.33",
        "Robert Fox" to "-\$192.63",
        "Brooklyn Simmons" to "\$602.50",
        "Cameron Williamson" to "\$602.00"
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = name, fontSize = 14.sp)
                        Text(text = amount, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecentOrdersCard() {
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
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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
                                color = if (status == "Active") Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
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
fun TrafficSourceCard() {
    val sources = listOf(
        "Facebook" to 77,
        "Youtube" to 35,
        "Instagram" to 59,
        "Twitter" to 23,
        "Others" to 46
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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