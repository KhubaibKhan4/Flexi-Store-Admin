package presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.order.Orders
import domain.model.products.Products
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import utils.Constant.BASE_URL

@Composable
fun RecentOrdersCard(modifier: Modifier, orders: List<Orders>,products: List<Products>) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Recent Orders", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            orders.forEach { order ->
                val product = products.find { it.id == order.productIds }
                if (product != null) {
                    val image : Resource<Painter> = asyncPainterResource(BASE_URL+product.imageUrl)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KamelImage(
                            resource = image,
                            contentAlignment = Alignment.Center,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(28.dp)
                                .clip(CircleShape)
                                .border(width = 1.dp, color = Color.LightGray, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Column {
                            Text(text = product.name.take(20), fontSize = 14.sp)
                            Text(text = "#${order.trackingId.take(8)}", fontSize = 12.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "\$${order.totalPrice}", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (order.orderProgress=="On Progress") "Pending" else if (order.orderProgress == "On The Way") "On The Way" else "Completed",
                            fontSize = 12.sp,
                            color = if (order.orderProgress == "Completed") Color.Green else Color.Red,
                            modifier = Modifier
                                .background(
                                    color = if (order.orderProgress == "On The Way") Color.Green.copy(alpha = 0.1f) else Color.Red.copy(
                                        alpha = 0.1f
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}