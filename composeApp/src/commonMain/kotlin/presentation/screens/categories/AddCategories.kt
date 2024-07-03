package presentation.screens.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.usecase.UiState
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import org.koin.compose.koinInject
import presentation.screens.components.ErrorScreen
import presentation.viewmodel.MainViewModel

class AddCategories : Screen {
    @Composable
    override fun Content() {
        AddNewCategoryScreen()
    }
}

@Composable
fun AddNewCategoryScreen(
    viewModel: MainViewModel = koinInject()
) {
    val navigator = LocalNavigator.current
    var categoryName by remember { mutableStateOf("") }
    var categoryDescription by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(true) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var serverResponse by remember { mutableStateOf<HttpResponse?>(null) }
    val categories by viewModel.createCategory.collectAsState()

    when (categories) {
        is UiState.ERROR -> {
            val error = (categories as UiState.ERROR).throwable
            ErrorScreen(error.message.toString(), onRetry = {})
        }
        UiState.LOADING -> {
            // Show loading state
        }
        is UiState.SUCCESS -> {
            serverResponse = (categories as UiState.SUCCESS).response
        }
    }

    Column(
        modifier = Modifier
            .background(Color(0XFFe5f0f9))
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Arrow Back",
                modifier = Modifier.clickable {
                    navigator?.pop()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add New Category",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductImageSection(imageBytes) { newBytes -> imageBytes = newBytes }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = categoryDescription,
                onValueChange = { categoryDescription = it },
                label = { Text("Category Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                maxLines = 3
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Visible")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isVisible,
                onCheckedChange = { isVisible = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Save to Draft")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                imageBytes?.let {
                    viewModel.createCategories(
                        name = categoryName,
                        description = categoryDescription,
                        isVisible = isVisible,
                        imageBytes = it
                    )
                }
                isLoading = true
            }) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Publish Now")
                    Spacer(modifier = Modifier.width(8.dp))
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(16.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Server Response Handling
        if (serverResponse?.isActive == true) {
            if (serverResponse?.status?.value == 200 || serverResponse?.status?.value == 201) {
                Text(
                    text = "Published Successfully....",
                    color = Color.Green
                )
                scope.launch {
                    delay(2000)
                    navigator?.pop()
                }
            } else {
                Text(
                    text = "Failed to Publish....",
                    color = Color.Red
                )
            }
        }
    }
}


@Composable
fun ProductImageSection(
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit
) {
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
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            imageBytes?.let {
                Image(
                    bitmap = org.jetbrains.skia.Image.makeFromEncoded(imageBytes).toComposeImageBitmap(),
                    contentDescription = "Category Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .clickable { launcher.launch() }
                ) {
                    Text("Select Image", color = Color.Gray)
                }
            }

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(30.dp)
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
                    contentDescription = null
                )
            }
        }
    }
}
