package presentation.screens.view

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.model.products.Products
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import utils.Constant.BASE_URL

class ProductView(
    private val product: Products
): Screen {
    @Composable
    override fun Content() {
        ProductDisplayScreen(product)
    }
}
@Composable
fun ProductDisplayScreen(product: Products) {
    val navigator = LocalNavigator.current
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Product Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                val painter: Resource<Painter> = asyncPainterResource(BASE_URL + product.imageUrl)
                KamelImage(
                    resource = painter,
                    contentDescription = "Product Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                ProductDetailItem(label = "Name", value = product.name)
                ProductDetailItem(label = "Brand", value = product.brand)
                ProductDetailItem(label = "Category", value = product.categoryTitle)
                ProductDetailItem(label = "Description", value = product.description)
                ProductDetailItem(label = "Colors", value = product.colors)
                ProductDetailItem(label = "Dimensions", value = product.dimensions)
                ProductDetailItem(label = "Weight", value = "${product.weight} kg")
                ProductDetailItem(label = "Price", value = "\$${product.price}")
                ProductDetailItem(label = "Discount Price", value = "\$${product.discountPrice}")
                ProductDetailItem(label = "Promotion", value = product.promotionDescription)
                ProductDetailItem(label = "Average Rating", value = product.averageRating.toString())
                ProductDetailItem(
                    label = "Available",
                    value = if (product.isAvailable) "Yes" else "No",
                    icon = if (product.isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    iconColor = if (product.isAvailable) Color.Green else Color.Red
                )
                ProductDetailItem(
                    label = "Featured",
                    value = if (product.isFeatured) "Yes" else "No",
                    icon = if (product.isFeatured) Icons.Default.Star else Icons.Default.StarOutline,
                    iconColor = if (product.isFeatured) Color.Yellow else Color.Gray
                )
                ProductDetailItem(label = "Manufacturer", value = product.manufacturer)
                ProductDetailItem(label = "Total Stack", value = product.totalStack.toString())
                ProductDetailItem(label = "Created At", value = product.createdAt)
                ProductDetailItem(label = "Updated At", value = product.updatedAt)
            }
        }
    }
}

@Composable
fun ProductDetailItem(label: String, value: String, icon: ImageVector? = null, iconColor: Color? = null) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label:", fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = value, color = Color.Gray)
            if (icon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = icon, contentDescription = null, tint = iconColor ?: Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
