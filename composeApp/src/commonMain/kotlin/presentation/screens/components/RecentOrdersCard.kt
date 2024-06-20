package presentation.screens.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
    var isViewAll by remember { mutableStateOf(false) }
    val ordersData = if (isViewAll) orders else orders.take(4)
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .animateContentSize(tween(durationMillis = 500, delayMillis = 400, easing = FastOutSlowInEasing))
            .height(if (isViewAll) IntrinsicSize.Max else IntrinsicSize.Min),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf1f4f9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recent Orders", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if(isViewAll) "View Less" else "View All",
                        fontSize = 13.sp, color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        modifier = Modifier.rotate(if(isViewAll) 90f else 270f).size(15.dp)
                            .animateContentSize(tween(durationMillis = 500, delayMillis = 400, easing = FastOutSlowInEasing))
                            .clickable {
                                isViewAll = !isViewAll
                            },
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            ordersData.forEach { order ->
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