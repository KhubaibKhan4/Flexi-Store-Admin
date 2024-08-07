package presentation.screens.promotions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import domain.model.promotions.Promotion
import domain.usecase.UiState
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import presentation.screens.components.ErrorScreen
import presentation.screens.components.LoadingScreen
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL

class PromotionScreen : Screen {
    @Composable
    override fun Content() {
        PromotionContent()
    }
}

@Composable
fun PromotionContent(viewModel: MainViewModel = koinInject()) {
    val navigator = LocalNavigator.current
    var promotionList by remember { mutableStateOf(emptyList<Promotion>()) }
    var searchQuery by remember { mutableStateOf("") }
    var deleteMessage by remember { mutableStateOf("") }
    var enabledFilter by remember { mutableStateOf("All") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getPromotions()
    }

    val promotionState by viewModel.promotions.collectAsState()
    when (promotionState) {
        is UiState.ERROR -> {
            val error = (promotionState as UiState.ERROR).throwable
            ErrorScreen(error.message.toString(), onRetry = {})
        }

        UiState.LOADING -> {
            LoadingScreen()
        }

        is UiState.SUCCESS -> {
            val promotions = (promotionState as UiState.SUCCESS).response
            promotionList = promotions
        }
    }
    var serverResponse by remember { mutableStateOf<HttpResponse?>(null) }
    val deletePromo by viewModel.deletePromo.collectAsState()
    when (deletePromo) {
        is UiState.ERROR -> {
            val error = (deletePromo as UiState.ERROR).throwable
            ErrorScreen(error.message.toString(), onRetry = {})
        }

        UiState.LOADING -> {

        }

        is UiState.SUCCESS -> {
            val response = (deletePromo as UiState.SUCCESS).response
            serverResponse = response
            if (serverResponse?.status?.value == 200 || serverResponse?.status?.value == 201 || serverResponse?.status?.value == 202 || serverResponse?.status?.value == 203) {
                deleteMessage = "Deleted Successfully...."
                Text(
                    text = deleteMessage
                )
                LaunchedEffect(serverResponse){
                    viewModel.getPromotions()
                }
                scope.launch {
                    delay(2000)
                    deleteMessage = ""
                }
            }
        }
    }

    Column(modifier = Modifier .background(Color(0XFFe5f0f9)).fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Promotions",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                var expanded by remember { mutableStateOf(false) }

                ElevatedButton(
                    onClick = {
                        navigator?.push(AddPromotion())
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0XFF0a8af9),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Promotions")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Gray))
                        .padding(8.dp)
                        .clickable { expanded = true }
                ) {
                    Text(text = enabledFilter)
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        val options = listOf("All", "Enabled", "Disabled")
                        options.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    enabledFilter = option
                                    expanded = false
                                },
                                text = { Text(text = option) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Gray))
                        .padding(8.dp)
                        .width(200.dp),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text("Search by title...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (promotionList.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 220.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(promotionList.filter {
                    (enabledFilter == "All" || (enabledFilter == "Enabled" && it.enabled) || (enabledFilter == "Disabled" && !it.enabled)) &&
                            it.title.contains(searchQuery, ignoreCase = true)
                }) { promotion ->
                    PromotionItem(promotion, onEditClick = { selectedPromotion ->
                        navigator?.push(EditPromotion(selectedPromotion))
                    },
                        onDeleteClick = { selectedPromotion ->
                            viewModel.deletePromotion(selectedPromotion.id.toLong())
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No promotions found")
            }
        }
        if (serverResponse?.isActive == true) {
            if (serverResponse?.status?.value == 200 || serverResponse?.status?.value == 201 || serverResponse?.status?.value == 202 || serverResponse?.status?.value == 203) {
                Text(
                    text = "Deleted Successfully...."
                )
                scope.launch {
                    delay(2000)
                }
            } else {
                Text(
                    text = "Failed to Delete...."
                )
            }
        }
    }
}

@Composable
fun PromotionItem(
    promotion: Promotion, onEditClick: (Promotion) -> Unit,
    onDeleteClick: (Promotion) -> Unit,
) {
    val viewModel: MainViewModel = koinInject()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val image: Resource<Painter> = asyncPainterResource(BASE_URL + promotion.imageUrl)
        KamelImage(
            resource = image,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = "Promotion Image"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = promotion.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = promotion.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onEditClick(promotion) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text(text = "Edit", color = Color.White)
            }
            Button(
                onClick = {
                    onDeleteClick(promotion)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }
    }
}