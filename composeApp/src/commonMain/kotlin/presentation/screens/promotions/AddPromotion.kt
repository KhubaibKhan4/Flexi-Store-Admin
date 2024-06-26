package presentation.screens.promotions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.usecase.UiState
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import presentation.screens.components.ErrorScreen
import presentation.viewmodel.MainViewModel

class AddPromotion : Screen {
    @Composable
    override fun Content() {
        AddPromotionScreen()
    }
}

@Composable
fun AddPromotionScreen(viewModel: MainViewModel = koinInject()) {
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var enable by remember { mutableStateOf(true) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var serverResponse by remember { mutableStateOf<HttpResponse?>(null) }
    val createState by viewModel.createPromo.collectAsState()
    when (createState) {
        is UiState.ERROR -> {
            val error = (createState as UiState.ERROR).throwable
            ErrorScreen(
                errorMessage = error.message.toString(),
                onRetry = {
                }
            )

        }

        UiState.LOADING -> {

        }

        is UiState.SUCCESS -> {
            val response = (createState as UiState.SUCCESS).response
            serverResponse = response
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier.clickable {
                    navigator.pop()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Checkbox(
                checked = enable,
                onCheckedChange = { enable = it }
            )
            Text("Enable Promotion")
        }

        Spacer(modifier = Modifier.height(16.dp))

        PromotionImageSelection(imageBytes, onImageSelected = { imageBytes = it }, imageUrl = "")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (imageBytes != null) {
                    imageBytes?.let {
                        viewModel.createPromo(
                            title = title,
                            description = description,
                            startDate = startDate,
                            endDate = endDate,
                            enable = enable,
                            image = it
                        )
                    }

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Promotion")
        }
        if (serverResponse?.isActive == true) {
            if (serverResponse?.status?.value == 200 || serverResponse?.status?.value == 201 || serverResponse?.status?.value == 202 || serverResponse?.status?.value == 203) {
                Text(
                    text = "Published Successfully...."
                )
                scope.launch {
                    delay(2000)
                    navigator?.pop()
                }
            } else {
                Text(
                    text = "Failed to Publish...."
                )
            }
        }
    }
}
