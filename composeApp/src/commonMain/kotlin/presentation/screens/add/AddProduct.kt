package presentation.screens.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.model.categories.Categories
import domain.usecase.UiState
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import org.koin.compose.koinInject
import presentation.screens.components.ErrorScreen
import presentation.viewmodel.MainViewModel

class AddProduct(
    private val categories: List<Categories>,
) : Screen {
    @Composable
    override fun Content() {
        AddProductContent(categories=categories)
    }
}


@Composable
fun AddProductContent(
    viewModel: MainViewModel = koinInject(),
    categories: List<Categories>,
) {
    val navigator = LocalNavigator.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var categoryTitle by remember { mutableStateOf("") }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var createdAt by remember { mutableStateOf("") }
    var updatedAt by remember { mutableStateOf("") }
    var totalStack by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var dimensions by remember { mutableStateOf("") }
    var isAvailable by remember { mutableStateOf(false) }
    var discountPrice by remember { mutableStateOf("") }
    var promotionDescription by remember { mutableStateOf("") }
    var averageRating by remember { mutableStateOf("") }
    var isFeature by remember { mutableStateOf(false) }
    var manufacturer by remember { mutableStateOf("") }
    var colors by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var serverResponse by remember { mutableStateOf<HttpResponse?>(null) }
    val scope = rememberCoroutineScope()

    val createState by viewModel.createProduct.collectAsState()
    when (createState) {
        is UiState.ERROR -> {
            val error = (createState as UiState.ERROR).throwable
            ErrorScreen(
                errorMessage = error.message.toString(),
                onRetry = {

                }
            )
            isLoading = false
        }

        UiState.LOADING -> {

        }

        is UiState.SUCCESS -> {
            val response = (createState as UiState.SUCCESS).response
            serverResponse = response
            isLoading = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Arrow Back",
                modifier = Modifier.clickable {
                    navigator?.pop()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add New Product",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ProductImageSection(imageBytes) { newBytes -> imageBytes = newBytes }
                GeneralInformationSection(
                    name = name,
                    onNameChange = { name = it },
                    categoryTitle = categoryTitle,
                    onCategoryTitleChange = { categoryTitle = it },
                    brand = brand,
                    onBrandChange = { brand = it },
                    description = description,
                    onDescriptionChange = { description = it }
                )
                CategoryDropdown(
                    categories = categories,
                    onCategorySelected = { selectedCategory ->
                        categoryId = selectedCategory.id.toString()
                        categoryTitle = selectedCategory.name
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                PriceInformationSection(
                    price = price,
                    onPriceChange = { price = it },
                    discountPrice = discountPrice,
                    onDiscountPriceChange = { discountPrice = it }
                )
                StockInformationSection(
                    totalStack = totalStack,
                    onTotalStackChange = { totalStack = it },
                    weight = weight,
                    onWeightChange = { weight = it },
                    dimensions = dimensions,
                    onDimensionsChange = { dimensions = it }
                )
                AdditionalInformationSection(
                    createdAt = createdAt,
                    onCreatedAtChange = { createdAt = it },
                    updatedAt = updatedAt,
                    onUpdatedAtChange = { updatedAt = it },
                    promotionDescription = promotionDescription,
                    onPromotionDescriptionChange = { promotionDescription = it },
                    averageRating = averageRating,
                    onAverageRatingChange = { averageRating = it },
                    isFeature = isFeature,
                    onIsFeatureChange = { isFeature = it },
                    isAvailable = isAvailable,
                    onIsAvailableChange = { isAvailable = it },
                    manufacturer = manufacturer,
                    onManufacturerChange = { manufacturer = it },
                    colors = colors,
                    onColorsChange = { colors = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* Handle Save to Draft */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Save to Draft")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                imageBytes?.let {
                    viewModel.createProduct(
                        name,
                        description,
                        price.toLong(),
                        categoryId.toLong(),
                        categoryTitle,
                        it,
                        createdAt,
                        updatedAt,
                        totalStack.toLong(),
                        brand,
                        weight.toDouble(),
                        dimensions,
                        isAvailable,
                        discountPrice.toLong(),
                        promotionDescription,
                        averageRating.toDouble(),
                        isFeature,
                        manufacturer,
                        colors
                    )
                }
                isLoading = true
            }) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Publish Now")
                    Spacer(modifier = Modifier.width(8.dp))
                    AnimatedVisibility(isLoading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        if (serverResponse?.isActive == true) {
            if (serverResponse?.status?.value == 200 && serverResponse?.status?.value == 201) {
                Text(
                    text = "Published Successfully...."
                )
                scope.launch {
                    delay(2000)
                    navigator?.pop()
                }
            } else {
                Text(
                    text = "Failed to Publish...."
                )
            }
        }
    }
}

data class Category(val id: Int, val name: String)

@Composable
fun CategoryDropdown(categories: List<Categories>, onCategorySelected: (Categories) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Select Category") }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(6.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedText,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    selectedText = category.name
                    onCategorySelected(category)
                    expanded = false
                },
                    text = {
                        Text(text = category.name)
                    })
            }
        }
    }
}


@Composable
fun ProductImageSection(
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Pick a Product Image"
    ) { platformFile ->
        scope.launch {
            val bytes = platformFile?.readBytes()
            if (bytes != null) {
                onImageSelected(bytes)
            }
        }
    }
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Product Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                if (imageBytes != null) {
                    val imageBitmap = imageBytes
                    imageBitmap?.let {
                        Image(
                            bitmap = Image.makeFromEncoded(imageBytes).toComposeImageBitmap(),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxWidth(),
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(30.dp)
                        .background(color = Color.LightGray, shape = CircleShape)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd)
                        .clickable {
                            launcher.launch()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun GeneralInformationSection(
    name: String,
    onNameChange: (String) -> Unit,
    categoryTitle: String,
    onCategoryTitleChange: (String) -> Unit,
    brand: String,
    onBrandChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "General Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = categoryTitle,
                onValueChange = onCategoryTitleChange,
                label = { Text("Product Type") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = brand,
                onValueChange = onBrandChange,
                label = { Text("Product Brand") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PriceInformationSection(
    price: String,
    onPriceChange: (String) -> Unit,
    discountPrice: String,
    onDiscountPriceChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Price Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = discountPrice,
                onValueChange = onDiscountPriceChange,
                label = { Text("Discount Price") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StockInformationSection(
    totalStack: String,
    onTotalStackChange: (String) -> Unit,
    weight: String,
    onWeightChange: (String) -> Unit,
    dimensions: String,
    onDimensionsChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Stock Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = totalStack,
                onValueChange = onTotalStackChange,
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("Weight") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dimensions,
                onValueChange = onDimensionsChange,
                label = { Text("Dimensions") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AdditionalInformationSection(
    createdAt: String,
    onCreatedAtChange: (String) -> Unit,
    updatedAt: String,
    onUpdatedAtChange: (String) -> Unit,
    promotionDescription: String,
    onPromotionDescriptionChange: (String) -> Unit,
    averageRating: String,
    onAverageRatingChange: (String) -> Unit,
    isFeature: Boolean,
    onIsFeatureChange: (Boolean) -> Unit,
    isAvailable: Boolean,
    onIsAvailableChange: (Boolean) -> Unit,
    manufacturer: String,
    onManufacturerChange: (String) -> Unit,
    colors: String,
    onColorsChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Additional Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = createdAt,
                onValueChange = onCreatedAtChange,
                label = { Text("Created At") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar"
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = updatedAt,
                onValueChange = onUpdatedAtChange,
                label = { Text("Updated At") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar"
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = promotionDescription,
                onValueChange = onPromotionDescriptionChange,
                label = { Text("Promotion Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = averageRating,
                onValueChange = onAverageRatingChange,
                label = { Text("Average Rating") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isFeature,
                    onCheckedChange = onIsFeatureChange
                )
                Text(text = "Is Feature")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAvailable,
                    onCheckedChange = onIsAvailableChange
                )
                Text(text = "Is Available")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = manufacturer,
                onValueChange = onManufacturerChange,
                label = { Text("Manufacturer") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = colors,
                onValueChange = onColorsChange,
                label = { Text("Colors") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}