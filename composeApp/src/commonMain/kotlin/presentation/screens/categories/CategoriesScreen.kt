package presentation.screens.categories

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.model.categories.Categories
import domain.usecase.UiState
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject
import presentation.screens.add.AddProduct
import presentation.screens.product.ProductGridScreen
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL

class CategoriesScreen(
    private val categories: List<Categories>
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<MainViewModel>()
        val navigator = LocalNavigator.current
        var allCategories by remember { mutableStateOf(emptyList<Categories>()) }

        LaunchedEffect(Unit){
            viewModel.getCategories()
        }
        val categories by viewModel.categories.collectAsState()
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
                            text = "Categories",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF007BFF)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ElevatedButton(
                        onClick = {
                            navigator?.push(AddCategories())
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
                        Text("Add Categories")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                CategoryGrid(allCategories)
            }
        }
    }
}
@Composable
fun CategoryCard(category: Categories) {
    val navigator = LocalNavigator.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1.8f)
            .clickable { /* Handle click */ },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            ) {
                val image: Resource<Painter> = asyncPainterResource(BASE_URL + category.imageUrl)
                KamelImage(
                    resource = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
                Text(
                    text = category.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    IconButton(onClick = { navigator?.push(EditCategories(category)) },
                        modifier = Modifier.size(20.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGrid(categories: List<Categories>) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth()
            .height(800.dp),
        columns = GridCells.Adaptive(minSize = 200.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(categories) { category ->
            CategoryCard(category)
        }
    }
}
@Composable
fun SearchAndFilterBar(
    onSearch: (String) -> Unit,
    onFilter: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = onSearch,
            label = { Text("Search Categories") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = false,
            onCheckedChange = onFilter,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text("Show Only Visible")
    }
}