package presentation.screens.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import domain.model.order.Orders
import domain.model.products.Products
import presentation.screens.dashboard.aggregateSalesDataByCategory

@Composable
fun DashboardPieChart(
    title: String,
    orders: List<Orders>,
    products: List<Products>,
    modifier: Modifier = Modifier
) {
    val categorySalesPercentage = aggregateSalesDataByCategory(orders,products).mapValues { (it.value / orders.sumOf { it.totalPrice }) * 100 }
    val colors = listOf(Color.Green, Color.Blue, Color.Red, Color.Yellow, Color.Gray)

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
            Text(text = title, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            val pieChartData: List<PieChartData> = categorySalesPercentage.entries.mapIndexed { index, entry ->
                PieChartData(
                    partName = entry.key,
                    data = entry.value,
                    color = colors.getOrElse(index) { Color.Gray }
                )
            }

            PieChart(
                modifier = Modifier.height(300.dp),
                pieChartData = pieChartData,
                ratioLineColor = Color.LightGray,
                textRatioStyle = TextStyle(color = Color.Gray),
            )
        }
    }
}