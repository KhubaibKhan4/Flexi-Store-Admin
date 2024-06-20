package presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import flexi_store_admin.composeapp.generated.resources.Res
import flexi_store_admin.composeapp.generated.resources.avatar_1
import flexi_store_admin.composeapp.generated.resources.avatar_2
import flexi_store_admin.composeapp.generated.resources.avatar_3
import flexi_store_admin.composeapp.generated.resources.avatar_4
import org.jetbrains.compose.resources.painterResource

@Composable
fun TransactionsCard(modifier: Modifier) {
    val transactions = listOf(
        "Esther Howard" to "\$302.33" to Res.drawable.avatar_1,
        "Robert Fox" to "-\$192.63" to Res.drawable.avatar_2,
        "Brooklyn Simmons" to "\$602.50" to Res.drawable.avatar_3,
        "Cameron Williamson" to "\$602.00" to Res.drawable.avatar_4
    )

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
            Text(text = "Transactions", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            transactions.forEach { (name, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Gray, shape = CircleShape)
                    ) {
                        Image(
                            painter = painterResource(amount),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = name.first, fontSize = 14.sp)
                        Text(
                            text = name.second, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                            color = if (name.second.contains("-")) Color.Red else Color.Green
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
