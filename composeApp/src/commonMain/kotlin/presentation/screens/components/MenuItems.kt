package presentation.screens.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MenuItem(
    name: String,
    icon: ImageVector,
    subMenuItems: List<String> = emptyList(),
    selectedMenuItem: String,
    onMenuItemClick: (String) -> Unit,
    expanded: Boolean
) {
    val backgroundColor by animateColorAsState(if (selectedMenuItem == name) Color(0xFF007BFF) else Color.Transparent)
    val contentColor by animateColorAsState(if (selectedMenuItem == name) Color.White else Color.Black)
    val fontWeight = if (selectedMenuItem == name) FontWeight.Bold else FontWeight.Normal
    var subMenuExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .clickable {
                    onMenuItemClick(name)
                    if (subMenuItems.isNotEmpty()) subMenuExpanded = !subMenuExpanded
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = name, tint = contentColor)
            if (expanded) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(name, color = contentColor, fontWeight = fontWeight)
                if (subMenuItems.isNotEmpty()) {
                    Icon(
                        imageVector = if (subMenuExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (subMenuExpanded) "Collapse" else "Expand",
                        tint = contentColor,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }

        if (subMenuExpanded && expanded) {
            subMenuItems.forEach { subMenuItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                        .clickable { onMenuItemClick(subMenuItem) }
                ) {
                    Text(subMenuItem, color = Color.Gray)
                }
            }
        }
    }
}