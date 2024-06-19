import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        CustomTopAppBar()
        Row(modifier = Modifier.fillMaxSize()) {
            if (!isCompact) {
                Sidebar(selectedMenuItem, onMenuItemClick = { selectedMenuItem = it })
            }
            DashboardContent(selectedMenuItem, isCompact)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(modifier: Modifier = Modifier) {
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
        Spacer(modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = isSearchQuery,
            onValueChange = {
                isSearchQuery = it
            },
            modifier = Modifier
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
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(35.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.Default.Image,
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
fun Sidebar(selectedMenuItem: String, onMenuItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        MenuItem("Dashboard", Icons.Default.Menu, selectedMenuItem, onMenuItemClick)
        MenuItem("Products", Icons.Default.ShoppingCart, selectedMenuItem, onMenuItemClick)
        MenuItem("Categories", Icons.Default.List, selectedMenuItem, onMenuItemClick)
        MenuItem("Orders", Icons.Default.Receipt, selectedMenuItem, onMenuItemClick)
        MenuItem("Reviews", Icons.Default.ThumbUp, selectedMenuItem, onMenuItemClick)
        MenuItem("Coupons", Icons.Default.CardGiftcard, selectedMenuItem, onMenuItemClick)
        MenuItem("Profile", Icons.Default.Person, selectedMenuItem, onMenuItemClick)
        MenuItem("Shop Settings", Icons.Default.Settings, selectedMenuItem, onMenuItemClick)
        MenuItem("Pages", Icons.Default.Pages, selectedMenuItem, onMenuItemClick)
    }
}

@Composable
fun MenuItem(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedMenuItem: String,
    onMenuItemClick: (String) -> Unit,
) {
    val backgroundColor by animateColorAsState(if (selectedMenuItem == name) Color(0xFF007BFF) else Color.Transparent)
    val contentColor by animateColorAsState(if (selectedMenuItem == name) Color.White else Color.Black)
    val fontWeight = if (selectedMenuItem == name) FontWeight.Bold else FontWeight.Normal

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onMenuItemClick(name) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = name, tint = contentColor)
        Spacer(modifier = Modifier.width(8.dp))
        Text(name, color = contentColor, fontWeight = fontWeight)
    }
}

@Composable
fun DashboardContent(selectedMenuItem: String, isCompact: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            selectedMenuItem,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when (selectedMenuItem) {
            "Dashboard" -> {
                Text("Welcome to your dashboard", modifier = Modifier.padding(bottom = 16.dp))
                ResponsiveRow {
                    StatCard("Orders Received", "400")
                    StatCard("Average Daily Sales", "$6433")
                    StatCard("New Customers This Month", "8.9K")
                    StatCard("Pending Orders", "563")
                }
                ResponsiveRow {
                    GraphCard("Sales Statics")
                    PieChartCard("Most Selling Category")
                }
                ResponsiveRow {
                    TransactionsCard()
                    RecentOrdersCard()
                    TrafficSourceCard()
                }
            }

            else -> Text("Data for $selectedMenuItem")
        }
    }
}

@Composable
fun ResponsiveRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = content
    )
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(value, color = Color(0xFF007BFF), fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

@Composable
fun GraphCard(title: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            // Placeholder for graph
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEEEEEE))
            )
        }
    }
}

@Composable
fun PieChartCard(title: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEEEEEE))
            )
        }
    }
}

@Composable
fun TransactionsCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Transactions", fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEEEEEE))
            )
        }
    }
}

@Composable
fun RecentOrdersCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Recent Orders", fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEEEEEE))
            )
        }
    }
}

@Composable
fun TrafficSourceCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Traffic Source", fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEEEEEE))
            )
        }
    }
}