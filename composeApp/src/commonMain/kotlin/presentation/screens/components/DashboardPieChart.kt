package presentation.screens.components

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

@Composable
fun DashboardPieChart(title: String, modifier: Modifier) {
    val data = listOf(
        "Grocery" to 30f,
        "Women" to 25f,
        "Men" to 20f,
        "" to 15f,
        "" to 10f
    )
    val colors = listOf(Color.Green, Color.Blue, Color.Red, Color.Yellow, Color.Gray)
    val total = data.sumOf { it.second.toDouble() }.toFloat()

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
            val testPieChartData: List<PieChartData> = listOf(
                PieChartData(
                    partName = "Grocery",
                    data = 500.0,
                    color = Color(0xFF22A699),
                ),
                PieChartData(
                    partName = "Women",
                    data = 700.0,
                    color = Color(0xFFF2BE22),
                ),
                PieChartData(
                    partName = "Men",
                    data = 500.0,
                    color = Color(0xFFF29727),
                ),
                PieChartData(
                    partName = "Kids",
                    data = 100.0,
                    color = Color(0xFFF24C3D),
                ),
                PieChartData(
                    partName = "Other",
                    data = 125.0,
                    color = Color(0xFFF24C9D),
                ),
            )

            PieChart(
                modifier = Modifier.height(300.dp),
                pieChartData = testPieChartData,
                ratioLineColor = Color.LightGray,
                textRatioStyle = TextStyle(color = Color.Gray),
            )
        }
    }
}