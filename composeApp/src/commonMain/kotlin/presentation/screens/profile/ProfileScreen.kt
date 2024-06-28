package presentation.screens.profile

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import domain.model.users.Users
import domain.usecase.UiState
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject
import presentation.screens.components.ErrorScreen
import presentation.screens.components.LoadingScreen
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<MainViewModel>()
        var usersData by remember { mutableStateOf<Users?>(null) }

        LaunchedEffect(Unit) {
            viewModel.getUserById(144)
        }

        val userState by viewModel.user.collectAsState()

        when (userState) {
            is UiState.ERROR -> {
                val error = (userState as UiState.ERROR).throwable
                ErrorScreen(error.message.toString(), onRetry = { viewModel.getUserById(144) })
            }

            UiState.LOADING -> {
                LoadingScreen()
            }

            is UiState.SUCCESS -> {
                val response = (userState as UiState.SUCCESS).response
                usersData = response
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                usersData?.let { admin ->
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Profile settings",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                                ProfileForm(admin)
                            }
                            ProfileImage(admin.profileImage)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SaveChangesButton()
                        Spacer(modifier = Modifier.height(16.dp))
                        AccountActions()
                    }
                }
            }
        }
    }

    @Composable
    fun ProfileForm(admin: Users) {
        Column {
            TextField(
                value = admin.username,
                onValueChange = {},
                label = { Text("User name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = admin.fullName,
                onValueChange = {},
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = admin.email,
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = admin.phoneNumber,
                onValueChange = {},
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = admin.address,
                onValueChange = {},
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = admin.city,
                onValueChange = {},
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }

    @Composable
    fun ProfileImage(profileImageUrl: String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val image: Resource<Painter> = asyncPainterResource(BASE_URL + profileImageUrl)
            KamelImage(
                resource = image,
                contentDescription = "Profile Image",
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {}) {
                Text("Change Image")
            }
        }
    }

    @Composable
    fun SaveChangesButton() {
        ElevatedButton(
            onClick = {  },
            modifier = Modifier
        ) {
            Text("Save changes")
        }
    }

    @Composable
    fun AccountActions() {
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Password", fontWeight = FontWeight.Bold)
                ElevatedButton(onClick = { }) {
                    Text("Change")
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Remove account", fontWeight = FontWeight.Bold)
                ElevatedButton(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Deactivate")
                }
            }
        }
    }
}