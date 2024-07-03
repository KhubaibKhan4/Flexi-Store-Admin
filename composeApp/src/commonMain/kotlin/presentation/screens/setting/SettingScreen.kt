package presentation.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .background(Color(0XFFe5f0f9))
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Setting",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EA)
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingOption(
                icon = Icons.Default.Person,
                title = "Account",
                subtitle = "Manage your account"
            )
            HorizontalDivider()
            SettingOption(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Customize notifications"
            )
            Divider()
            SettingOption(
                icon = Icons.Default.Security,
                title = "Privacy",
                subtitle = "Privacy settings"
            )
            Divider()
            SettingOption(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "Select language"
            )
            Divider()
            SettingOption(
                icon = Icons.Default.Help,
                title = "Help & Support",
                subtitle = "Get help and support"
            )
        }
    }
}

@Composable
fun SettingOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF6200EA),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF333333)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF777777)
            )
        }
    }
}