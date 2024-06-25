import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Pages
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import di.appModule
import domain.model.categories.Categories
import domain.model.order.Orders
import domain.model.products.Products
import domain.usecase.UiState
import kotlinx.coroutines.delay
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import presentation.screens.categories.CategoriesScreen
import presentation.screens.components.ErrorScreen
import presentation.screens.components.LoadingScreen
import presentation.screens.coupons.CouponsScreen
import presentation.screens.dashboard.DashboardScreen
import presentation.screens.navigation.rails.items.NavigationItem
import presentation.screens.navigation.sidebar.SidebarMenu
import presentation.screens.order.OrderScreen
import presentation.screens.pages.PagesScreen
import presentation.screens.product.ProductsScreen
import presentation.screens.profile.ProfileScreen
import presentation.screens.reviews.ReviewsScreen
import presentation.screens.setting.SettingScreen
import presentation.viewmodel.MainViewModel

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(appModule)
        }
    ) {
        MaterialTheme {
            AppContent()
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppContent(viewModel: MainViewModel = koinInject()) {

    var orderList by remember { mutableStateOf(emptyList<Orders>()) }
    var productList by remember { mutableStateOf(emptyList<Products>()) }
    var allProductList by remember { mutableStateOf(emptyList<Products>()) }
    var allCategories by remember { mutableStateOf(emptyList<Categories>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
        viewModel.getCategories()
        delay(3000)
        viewModel.getAllOrders()
    }


    val orderState by viewModel.allOrders.collectAsState()
    val productsState by viewModel.productDetail.collectAsState()
    val allProState by viewModel.allProducts.collectAsState()
    val categories by viewModel.categories.collectAsState()
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
            val idsList =
                orders.map { it.productIds.toString().trim() }.joinToString(separator = ",")
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

    when (allProState) {
        is UiState.LOADING -> {
            CircularProgressIndicator()
        }

        is UiState.ERROR -> {
            val error = (allProState as UiState.ERROR).throwable
            Text("Error loading products: ${error.message}")
        }

        is UiState.SUCCESS -> {
            val products = (allProState as UiState.SUCCESS).response
            allProductList = products
            println("ALL PRODUCT: $allProductList")
        }
    }

    when (categories) {
        is UiState.LOADING -> {
            CircularProgressIndicator()
        }

        is UiState.ERROR -> {
            val error = (categories as UiState.ERROR).throwable
            Text("Error loading products: ${error.message}")
        }

        is UiState.SUCCESS -> {
            val categoriesList = (categories as UiState.SUCCESS).response
            allCategories = categoriesList
        }
    }
    val items = listOf(
        NavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Default.Dashboard,
            unselectedIcon = Icons.Outlined.Dashboard,
            hasNews = false
        ),
        NavigationItem(
            title = "Products",
            selectedIcon = Icons.Default.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            hasNews = true,
            badgeCount = 0
        ),
        NavigationItem(
            title = "Categories",
            selectedIcon = Icons.Default.Category,
            unselectedIcon = Icons.Outlined.Category,
            hasNews = false,
        ),
        NavigationItem(
            title = "Orders",
            selectedIcon = Icons.Default.Receipt,
            unselectedIcon = Icons.Outlined.Receipt,
            hasNews = false,
        ),
        NavigationItem(
            title = "Reviews",
            selectedIcon = Icons.Default.Star,
            unselectedIcon = Icons.Outlined.Star,
            hasNews = false,
        ),
        NavigationItem(
            title = "Coupons",
            selectedIcon = Icons.Default.LocalOffer,
            unselectedIcon = Icons.Outlined.LocalOffer,
            hasNews = false,
        ),
        NavigationItem(
            title = "Profile",
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Outlined.Person,
            hasNews = false,
        ),
        NavigationItem(
            title = "Shop Settings",
            selectedIcon = Icons.Default.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            hasNews = false,
        ),
        NavigationItem(
            title = "Pages",
            selectedIcon = Icons.Default.Pages,
            unselectedIcon = Icons.Outlined.Pages,
            hasNews = false,
        ),
    )
    val windowClass = calculateWindowSizeClass()
    val showNavigationRail = windowClass.widthSizeClass != WindowWidthSizeClass.Compact
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            if (!showNavigationRail) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.ime),
                    containerColor = Color.Black,
                    contentColor = contentColorFor(Color.Red),
                    tonalElevation = 8.dp
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = { selectedItemIndex = index },
                            icon = {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (showNavigationRail) {
                SidebarMenu(
                    items = items,
                    selectedItemIndex = selectedItemIndex,
                    onMenuItemClick = { index ->
                        selectedItemIndex = index
                    },
                    initialExpandedState = false
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = if (showNavigationRail) 0.dp else 0.dp)
            ) {
                when (selectedItemIndex) {
                    0 -> Navigator(
                        DashboardScreen()
                    )

                    1 -> Navigator(
                        ProductsScreen(
                            product = allProductList,
                            isCompact = showNavigationRail
                        )
                    )

                    2 -> Navigator(CategoriesScreen(allCategories))
                    3 -> Navigator(OrderScreen())
                    4 -> Navigator(ReviewsScreen())
                    5 -> Navigator(CouponsScreen())
                    6 -> Navigator(ProfileScreen())
                    7 -> Navigator(SettingScreen())
                    8 -> Navigator(PagesScreen())
                }
            }
        }
    }
}


@Composable
fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        modifier = Modifier.fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp)),
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        icon = {
            tab.options.icon?.let { painter ->
                Icon(
                    painter,
                    contentDescription = tab.options.title,
                    tint = if (tabNavigator.current == tab) Color.Red else Color.White
                )
            }
        },
        label = {
            tab.options.title.let { title ->
                Text(
                    title,
                    fontSize = 12.sp,
                    color = if (tabNavigator.current == tab) Color.Red else Color.White
                )
            }
        },
        enabled = true,
        alwaysShowLabel = true,
        interactionSource = MutableInteractionSource(),
        colors = NavigationBarItemColors(
            selectedTextColor = Color.Red,
            selectedIconColor = Color.Red,
            unselectedTextColor = Color.Black,
            unselectedIconColor = Color.Black,
            selectedIndicatorColor = Color.Transparent,
            disabledIconColor = Color.Black,
            disabledTextColor = Color.Black
        )
    )
}