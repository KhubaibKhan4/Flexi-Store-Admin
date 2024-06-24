package presentation.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class AddProduct : Screen {
    @Composable
    override fun Content() {
        AddProductContent()
    }
}


@Composable
fun AddProductContent() {
    val navigator = LocalNavigator.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var categoryTitle by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
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
                ProductImageSection(imageUrl) { newUrl -> imageUrl = newUrl }
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
                    categoryId = categoryId,
                    onCategoryIdChange = { categoryId = it },
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
            Button(onClick = { /* Handle Scan to Fill Form */ }) {
                Text("Publish Now")
            }
        }
    }
}

@Composable
fun ProductImageSection(imageUrl: String, onImageUrlChange: (String) -> Unit) {
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
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Product Image",
                    modifier = Modifier.fillMaxWidth(),
                    tint = Color.White
                )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(30.dp)
                        .background(color = Color.LightGray, shape = CircleShape)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd)
                        .clickable {

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
    categoryId: String,
    onCategoryIdChange: (String) -> Unit,
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
                value = categoryId,
                onValueChange = onCategoryIdChange,
                label = { Text("Category ID") },
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