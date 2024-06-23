package presentation.screens.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.model.products.Products
import domain.usecase.UiState
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.koin.compose.koinInject
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class EditProduct(
    private val product: Products,
) : Screen {
    @Composable
    override fun Content() {
        EditProductScreen(product)
    }
}

@Composable
fun EditProductScreen(
    product: Products,
    viewModel: MainViewModel = koinInject(),
) {
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
    var totalStack by remember { mutableStateOf(product.totalStack.toString()) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var loading by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val productImageUrl = product.imageUrl
    val navigator = LocalNavigator.current
    val formattedDateTime = Clock.System.now()

    val updateState by viewModel.updateProduct.collectAsState()
    LaunchedEffect(updateState) {
        when (updateState) {
            is UiState.ERROR -> {
                loading = false
                responseMessage = (updateState as UiState.ERROR).throwable.message
            }
            UiState.LOADING -> {
                loading = true
                responseMessage = null
            }
            is UiState.SUCCESS -> {
                loading = false
                responseMessage = "Product updated successfully!"
            }
        }
    }

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Pick a Product Image"
    ) { platformFile ->
        scope.launch {
            imageBytes = platformFile?.readBytes()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(
                text = "Edit Products",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The most important feature in the product editing section is the product adding part. When adding products here, do not ignore to fill in all the required fields completely and follow the products adding rules.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (imageBytes != null) {
                Image(
                    contentDescription = "image",
                    bitmap = Image.Companion.makeFromEncoded(imageBytes!!).toComposeImageBitmap()
                )
                val imageBitmap = imageBytes
                imageBitmap?.let {
                    Image(
                        contentDescription = "image",
                        bitmap = Image.Companion.makeFromEncoded(imageBytes!!).toComposeImageBitmap(),
                        modifier = Modifier
                            .size(128.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                }

            } else {
                val image: Resource<Painter> = asyncPainterResource(BASE_URL + productImageUrl)
                KamelImage(
                    resource = image,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(4.dp)
                    .clickable {
                        launcher.launch()
                    }
                    .align(Alignment.BottomEnd)
            )
        }

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
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = totalStack,
                    onValueChange = { totalStack = it },
                    label = { Text("Total Stack") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            ElevatedButton(
                onClick = {
                    scope.launch {
                        viewModel.updateProduct(
                            id = product.id.toLong(),
                            name = productName,
                            description = description,
                            price = price.toLongOrNull(),
                            categoryId = product.categoryId.toLong(),
                            categoryTitle = category,
                            imageBytes = imageBytes,
                            created_at = productDate,
                            updated_at = formattedDateTime.toString(),
                            total_stack = totalStack.toIntOrNull()?.toLong(),
                            brand = brand,
                            weight = product.weight,
                            dimensions = product.dimensions,
                            isAvailable = product.isAvailable,
                            discountPrice = discountPrice.toLongOrNull(),
                            promotionDescription = product.promotionDescription,
                            averageRating = product.averageRating,
                            isFeature = product.isFeatured,
                            manufacturer = product.manufacturer,
                            colors = product.colors
                        )
                    }
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
                    navigator?.pop()
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Cancel")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        responseMessage?.let {
            Text(
                text = it,
                color = if (updateState is UiState.SUCCESS) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}