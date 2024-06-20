package presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import domain.model.order.Orders
import domain.model.products.Products
import presentation.screens.dashboard.aggregateSalesDataByCategory

@Composable
fun DashboardChart(title: String, orders: List<Orders>,products: List<Products>, modifier: Modifier = Modifier) {
    val categorySalesPercentage = aggregateSalesDataByCategory(orders,products)
        .mapValues { (it.value / orders.sumOf { order -> order.totalPrice }) * 100 }

    val xAxisData = listOf("0", "18", "28", "55", "75", "100")
    val xAxisDataPoints = xAxisData.size

    val testLineParameters: List<LineParameters> = categorySalesPercentage.entries.mapIndexed { index, entry ->
        LineParameters(
            label = entry.key,
            data = List(xAxisDataPoints) { entry.value * (0.5 + 0.5 * it / (xAxisDataPoints - 1)) },
            lineColor = when (index) {
                0 -> Color(0xFF22A699)
                1 -> Color(0xFFF2BE22)
                2 -> Color(0xFFF29727)
                3 -> Color(0xFFF24C3D)
                4 -> Color(0xFFF24C9D)
                else -> Color.Gray
            },
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        )
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "View All", fontSize = 13.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))

            LineChart(
                modifier = Modifier.fillMaxWidth()
                    .height(300.dp),
                linesParameters = testLineParameters,
                isGrid = true,
                gridColor = Color.Blue,
                xAxisData = xAxisData,
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                ),
                xAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 14,
                oneLineChart = false,
                gridOrientation = GridOrientation.VERTICAL
            )
        }
    }
}