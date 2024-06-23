package presentation.screens.edit

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.painter.Painter
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
class EditProduct(
    private val product: Products
):Screen{
    @Composable
    override fun Content() {
        EditProductScreen(product)
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
    val productImageUrl = product.imageUrl
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
        if (productImageUrl != null) {
            val image: Resource<Painter>  = asyncPainterResource(BASE_URL + productImageUrl)
            KamelImage(
                resource = image,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            ) {
                Text("No Image", modifier = Modifier.align(Alignment.Center))
            }
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
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
    }
}
