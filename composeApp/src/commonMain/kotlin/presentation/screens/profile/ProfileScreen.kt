package presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import domain.model.users.Users
import domain.usecase.UiState
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
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
        var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
        var loading by remember { mutableStateOf(false) }
        var responseMessage by remember { mutableStateOf<HttpResponse?>(null) }
        val scope = rememberCoroutineScope()

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
        LaunchedEffect(imageBytes) {
            if (imageBytes != null) {
                viewModel.updateProfileImage(144, imageBytes!!)
            }
        }
        val updateProfileState by viewModel.updateImage.collectAsState()
        when (updateProfileState) {
            is UiState.ERROR -> {
                val error = (updateProfileState as UiState.ERROR).throwable
                ErrorScreen(error.message.toString(), onRetry = {
                    if (imageBytes != null) {
                        viewModel.updateProfileImage(144, imageBytes!!)
                    }
                })
            }

            UiState.LOADING -> {}
            is UiState.SUCCESS -> {
                val response = (updateProfileState as UiState.SUCCESS).response
                responseMessage = response
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
                            ImageSelection(
                                imageBytes,
                                onImageSelected = { imageBytes = it },
                                imageUrl = usersData?.profileImage.toString(),
                                responseMessage
                            )
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
    fun SaveChangesButton() {
        ElevatedButton(
            onClick = { },
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
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Deactivate")
                }
            }
        }
    }
}

@Composable
fun ImageSelection(
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
    imageUrl: String,
    responseMessage: HttpResponse?,
) {
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Pick a Category Image"
    ) { platformFile ->
        scope.launch {
            val bytes = platformFile?.readBytes()
            if (bytes != null) {
                onImageSelected(bytes)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(140.dp),
            contentAlignment = Alignment.Center
        ) {
            imageBytes?.let {
                Image(
                    bitmap = Image.makeFromEncoded(imageBytes).toComposeImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
                if (imageUrl.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable { launcher.launch() }
                    ) {
                        Text("Select Image", color = Color.Gray)
                    }
                } else {
                    val image: Resource<Painter> = asyncPainterResource(BASE_URL + imageUrl)
                    KamelImage(
                        resource = image,
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .padding(4.dp)
                    .size(25.dp)
                    .background(color = Color.LightGray, shape = CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        launcher.launch()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if (responseMessage != null) {
            if (responseMessage.status.value == 200 ||
                responseMessage.status.value == 201 ||
                responseMessage.status.value == 202
            ) {
                message = "Profile Image Updated Successfully"

            } else {
                message = responseMessage.status.description
            }
        }

        if (message.isNotEmpty()) {
            scope.launch {
                delay(3000)
                message = ""
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )
        } else {
            scope.launch {
                delay(3000)
                message = ""
            }
            Text(
                text = message ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }

    }
}