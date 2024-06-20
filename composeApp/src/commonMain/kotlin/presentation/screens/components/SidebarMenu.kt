package presentation.screens.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SidebarMenu(isCompact: Boolean, selectedMenuItem: String, onMenuItemClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(true) }

    val expandIcon = if (expanded) Icons.Default.ArrowBack else Icons.Default.ArrowForward
    val sidebarWidth by animateDpAsState(targetValue = if (expanded) 200.dp else 60.dp)

    Column(
        modifier = Modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .background(Color(0xFFf1f4f9))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .animateContentSize(
                    tween(
                        durationMillis = 300,
                        delayMillis = 400,
                        easing = LinearOutSlowInEasing
                    )
                )
                .align(Alignment.End)
        ) {
            Icon(expandIcon, contentDescription = "Expand/Collapse Sidebar")
        }

        androidx.compose.animation.AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(1000, 50, easing = LinearEasing)),
            exit = fadeOut(tween(1000, 50, easing = LinearEasing))
        ) {
            Column {
                MenuItem(
                    "Dashboard",
                    Icons.Default.Dashboard,
                    selectedMenuItem = selectedMenuItem,
                    onMenuItemClick = onMenuItemClick
                )
                MenuItem(
                    "Products",
                    Icons.Default.ShoppingCart,
                    listOf("Add Product", "Product List"),
                    selectedMenuItem,
                    onMenuItemClick
                )
                MenuItem(
                    "Categories",
                    Icons.Default.Category,
                    listOf("Add Category", "Category List"),
                    selectedMenuItem,
                    onMenuItemClick
                )
                MenuItem(
                    "Orders",
                    Icons.Default.Receipt,
                    listOf("New Orders", "Completed Orders"),
                    selectedMenuItem,
                    onMenuItemClick
                )
                MenuItem(
                    "Reviews",
                    Icons.Default.Star,
                    selectedMenuItem = selectedMenuItem,
                    onMenuItemClick = onMenuItemClick
                )
                MenuItem(
                    "Coupons",
                    Icons.Default.LocalOffer,
                    selectedMenuItem = selectedMenuItem,
                    onMenuItemClick = onMenuItemClick
                )
                MenuItem(
                    "Profile",
                    Icons.Default.Person,
                    selectedMenuItem = selectedMenuItem,
                    onMenuItemClick = onMenuItemClick
                )
                MenuItem(
                    "Shop Settings",
                    Icons.Default.Settings,
                    selectedMenuItem = selectedMenuItem,
                    onMenuItemClick = onMenuItemClick
                )
                MenuItem(
                    "Pages",
                    Icons.Default.Pages,
                    listOf("About Us", "Contact Us"),
                    selectedMenuItem,
                    onMenuItemClick
                )
            }
        }
    }
}