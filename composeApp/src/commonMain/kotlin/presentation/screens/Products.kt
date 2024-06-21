package presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.model.products.Products
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import utils.Constant.BASE_URL

@Composable
fun ProductContent(productList: List<Products>, isCompact: Boolean) {
    if (isCompact) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(8.dp)
        ) {
            ProductGridScreen(productList)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp)
        ) {
            ProductGridScreen(productList)
        }
    }
}

@Composable
fun ProductGridScreen(productList: List<Products>) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Category") }
    var sortOrder by remember { mutableStateOf("Last added") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    val filteredProductList = productList.filter { product ->
        (selectedCategory == "Category" || product.categoryTitle == selectedCategory) &&
                (searchQuery.isEmpty() || product.name.contains(searchQuery, ignoreCase = true))
    }.sortedWith(
        when (sortOrder) {
            "Price: Low to High" -> compareBy { it.price }
            "Price: High to Low" -> compareByDescending { it.price }
            else -> compareByDescending { it.createdAt }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                        imageVector = Icons.Default.Search,
                        contentDescription = null
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
            Spacer(modifier = Modifier.weight(2.65f))
            Box(
                modifier = Modifier.weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { categoryExpanded = !categoryExpanded }
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(selectedCategory)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (categoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                ) {
                    DropdownMenuItem(onClick = {
                        selectedCategory = "Category"
                        categoryExpanded = false
                    }, text = { Text("Category") })
                    DropdownMenuItem(onClick = {
                        selectedCategory = "Category 1"
                        categoryExpanded = false
                    }, text = { Text("Category 1") })
                    DropdownMenuItem(onClick = {
                        selectedCategory = "Category 2"
                        categoryExpanded = false
                    }, text = { Text("Category 2") })
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier.weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { sortExpanded = !sortExpanded }
                    .padding(8.dp)
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
                    onDismissRequest = { sortExpanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        sortOrder = "Last added"
                        sortExpanded = false
                    },
                        text = { Text("Last added") })
                    DropdownMenuItem(onClick = {
                        sortOrder = "Price: Low to High"
                        sortExpanded = false
                    },
                        text = { Text("Price: Low to High") })
                    DropdownMenuItem(onClick = {
                        sortOrder = "Price: High to Low"
                        sortExpanded = false
                    }, text = { Text("Price: High to Low") })
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        ProductGrid(filteredProductList)
    }
}

@Composable
fun ProductGrid(productList: List<Products>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(215.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(productList) { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(product: Products) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp)
                ) {
                    OutlinedCard(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
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
                        elevation = CardDefaults.cardElevation(4.dp)
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