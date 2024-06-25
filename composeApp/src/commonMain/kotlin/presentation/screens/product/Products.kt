package presentation.screens.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.model.categories.Categories
import domain.model.products.Products
import domain.usecase.UiState
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject
import presentation.screens.add.AddProduct
import presentation.screens.edit.EditProduct
import presentation.screens.view.ProductView
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL

class ProductsScreen(
    private val product: List<Products>,
    private val isCompact: Boolean,
    private val categories: List<Categories>
): Screen {
    @Composable
    override fun Content() {
        ProductContent(productList =product , isCompact = isCompact,categories)
    }

}
@Composable
fun ProductContent(
    productList: List<Products>,
    isCompact: Boolean,
    categories: List<Categories>,
    viewModel: MainViewModel = koinInject()
) {
    var allProductList by remember { mutableStateOf(emptyList<Products>()) }
    LaunchedEffect(Unit){
        viewModel.getAllProducts()
    }
    val allProState by viewModel.allProducts.collectAsState()
    val navigator = LocalNavigator.current

    when (allProState) {
        is UiState.LOADING -> {
           // CircularProgressIndicator()
        }

        is UiState.ERROR -> {
            val error = (allProState as UiState.ERROR).throwable
            Text("Error loading products: ${error.message}")
        }

        is UiState.SUCCESS -> {
            val products = (allProState as UiState.SUCCESS).response
            allProductList = products
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        item {
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
                        text = "Products",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007BFF)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                ElevatedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export")
                }
                Spacer(modifier = Modifier.width(6.dp))
                ElevatedButton(
                    onClick = {
                        navigator?.push(AddProduct(categories))
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0XFF0a8af9),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Product")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ProductGridScreen(allProductList)
        }
    }
}

@Composable
fun ProductGridScreen(productList: List<Products>) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Categories") }
    var sortOrder by remember { mutableStateOf("Latest") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    val categories = productList.map { it.categoryTitle }.distinct().sorted()
    println("CATEGORIES: $categories")

    val filteredProductList = productList.filter { product ->
        (selectedCategory == "All Categories" || product.categoryTitle == selectedCategory) &&
                (searchQuery.isEmpty() || product.name.contains(searchQuery, ignoreCase = true))
    }.sortedWith(
        when (sortOrder) {
            "Latest" -> compareByDescending { it.createdAt }
            "Most Popular" -> compareByDescending { it.averageRating }
            "Cheap" -> compareBy { it.price }
            "Most Expensive" -> compareByDescending { it.price }
            "Top Rated" -> compareByDescending { it.averageRating }
            "Trending" -> compareByDescending { it.isFeatured }
            "Limited Edition" -> compareByDescending<Products> { it.isAvailable }.thenByDescending { it.totalStack }
            "Best Sellers" -> compareByDescending<Products> { it.averageRating }.thenBy { it.discountPrice }
            "Exclusive Deals" -> compareBy<Products> { !it.isFeatured }.thenBy { it.discountPrice }
            else -> compareByDescending { it.createdAt }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
                    .border(width = 1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                placeholder = {
                    Text("Search")
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (searchQuery.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            searchQuery = ""
                        }
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.weight(1.2f))
            Box(
                modifier = Modifier.weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { categoryExpanded = !categoryExpanded }
                    .padding(8.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(selectedCategory.take(16))
                    Spacer(modifier = Modifier.weight(1f).width(4.dp))
                    Icon(
                        imageVector = if (categoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.weight(1f)
                    )
                }
                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    DropdownMenuItem(onClick = {
                        selectedCategory = "All Categories"
                        categoryExpanded = false
                    }, text = { Text("All Categories") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    categories.forEach { category ->
                        DropdownMenuItem(onClick = {
                            selectedCategory = category
                            categoryExpanded = false
                        }, text = { Text(category, textAlign = TextAlign.Center) },
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier.weight(1f)
                    .wrapContentWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { sortExpanded = !sortExpanded }
                    .padding(8.dp)
                    .pointerHoverIcon(PointerIcon.Hand)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(sortOrder)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (sortExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = sortExpanded,
                    onDismissRequest = { sortExpanded = false },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    DropdownMenuItem(onClick = {
                        sortOrder = "Latest"
                        sortExpanded = false
                    }, text = { Text("Latest") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Most Popular"
                        sortExpanded = false
                    }, text = { Text("Most Popular") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Cheap"
                        sortExpanded = false
                    }, text = { Text("Cheap") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Most Expensive"
                        sortExpanded = false
                    }, text = { Text("Most Expensive") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Top Rated"
                        sortExpanded = false
                    }, text = { Text("Top Rated") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Trending"
                        sortExpanded = false
                    }, text = { Text("Trending") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Limited Edition"
                        sortExpanded = false
                    }, text = { Text("Limited Edition") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Best Sellers"
                        sortExpanded = false
                    }, text = { Text("Best Sellers") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                    DropdownMenuItem(onClick = {
                        sortOrder = "Exclusive Deals"
                        sortExpanded = false
                    }, text = { Text("Exclusive Deals") },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        PaginatedProductGrid(filteredProductList)
    }
}

@Composable
fun PaginatedProductGrid(productList: List<Products>) {
    var currentPage by remember { mutableStateOf(1) }
    val productsPerPage = 20
    val totalPages = (productList.size + productsPerPage - 1) / productsPerPage

    val startIndex = (currentPage - 1) * productsPerPage
    val endIndex = minOf(startIndex + productsPerPage, productList.size)
    val currentProductList = productList.subList(startIndex, endIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProductGrid(currentProductList)
        Spacer(modifier = Modifier.height(16.dp))
        PaginationControls(
            currentPage = currentPage,
            totalPages = totalPages,
            onPrevious = { if (currentPage > 1) currentPage-- },
            onNext = { if (currentPage < totalPages) currentPage++ },
            onPageSelected = { selectedPage -> currentPage = selectedPage }
        )
    }
}

@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onPageSelected: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (currentPage > 1) {
            OutlinedButton(
                onClick = onPrevious,
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        for (page in 1..totalPages) {
            val backgroundColor by animateColorAsState(if (page == currentPage) Color.Blue else Color.White)
            val contentColor by animateColorAsState(if (page == currentPage) Color.White else Color.Black)
            val borderColor by animateColorAsState(if (page == currentPage) Color.Blue else Color.Gray)

            OutlinedButton(
                onClick = { onPageSelected(page) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text(page.toString())
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        if (currentPage < totalPages) {
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onNext,
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun ProductGrid(productList: List<Products>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(215.dp),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(1200.dp)
    ) {
        items(productList) { product ->
            AnimatedVisibility(visible = true) {
                ProductCard(product)
            }
        }
    }
}

@Composable
fun ProductCard(product: Products) {
    val navigator = LocalNavigator.current
    var isEditProduct by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isEditProduct = !isEditProduct }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val productImages: Resource<Painter> = asyncPainterResource(BASE_URL + product.imageUrl)
            KamelImage(
                resource = productImages,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        navigator?.push(ProductView(product))
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    maxLines = 1
                )
                Text(
                    text = "\$${product.price}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    OutlinedCard(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            .clickable {
                                navigator?.push(EditProduct(product))
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit")
                        }
                    }
                    OutlinedCard(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Red
                        ),
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditProductScreen(product: Products) {
    var productName by remember { mutableStateOf(product.name) }
    var category by remember { mutableStateOf(product.categoryTitle) }
    var gender by remember { mutableStateOf("Male") }
    var brand by remember { mutableStateOf(product.brand) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var discount by remember { mutableStateOf("20%") }
    var discountPrice by remember { mutableStateOf(product.discountPrice.toString()) }
    var addSize by remember { mutableStateOf("EU-44") }
    var productDate by remember { mutableStateOf(product.createdAt) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Edit Products",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The most important feature in the product editing section is the product adding part. When adding products here, do not ignore to fill in all the required fields completely and follow the products adding rules.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = discount,
                    onValueChange = { discount = it },
                    label = { Text("Discount") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = discountPrice,
                    onValueChange = { discountPrice = it },
                    label = { Text("Discount Price") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = addSize,
                    onValueChange = { addSize = it },
                    label = { Text("Add Size") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = productDate,
                    onValueChange = { productDate = it },
                    label = { Text("Product Date") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            ElevatedButton(
                onClick = {
                    // Handle add product logic
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0XFF0a8af9),
                    contentColor = Color.White
                )
            ) {
                Text("Add Product")
            }
            Spacer(modifier = Modifier.width(8.dp))
            ElevatedButton(
                onClick = {
                    // Handle save product logic
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0XFF0a8af9),
                    contentColor = Color.White
                )
            ) {
                Text("Save Product")
            }
            Spacer(modifier = Modifier.width(8.dp))
            ElevatedButton(
                onClick = {
                    // Handle schedule product logic
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0XFF0a8af9),
                    contentColor = Color.White
                )
            ) {
                Text("Schedule")
            }
        }
    }
}
