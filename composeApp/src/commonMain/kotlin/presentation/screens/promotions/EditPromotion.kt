package presentation.screens.promotions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.model.promotions.Promotion
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import org.koin.compose.koinInject
import presentation.viewmodel.MainViewModel
import utils.Constant.BASE_URL

class EditPromotion(
    private val promotion: Promotion,
) : Screen {
    @Composable
    override fun Content() {
        EditPromotionScreen(promotion)
    }
}

@Composable
fun EditPromotionScreen(promotion: Promotion, viewModel: MainViewModel = koinInject()) {
    val navigator = LocalNavigator.currentOrThrow
    var title by remember { mutableStateOf(promotion.title) }
    var description by remember { mutableStateOf(promotion.description) }
    var startDate by remember { mutableStateOf(promotion.startDate.toString()) }
    var endDate by remember { mutableStateOf(promotion.endDate.toString()) }
    var enable by remember { mutableStateOf(promotion.enabled) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }

    Column(
        modifier = Modifier
            .background(Color(0XFFe5f0f9))
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable {
                    navigator.pop()
                }
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        PromotionImageSelection(
            imageBytes,
            onImageSelected = {  imageBytes = it },
            imageUrl = promotion.imageUrl
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (imageBytes != null) {
                    viewModel.updatePromotionById(
                        promotion.id.toLong(),
                        title,
                        description,
                        startDate,
                        endDate,
                        enable.toString(),
                        imageBytes!!
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Promotion")
        }
    }
}

@Composable
fun PromotionImageSelection(
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
    imageUrl: String,
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
                    bitmap = Image.makeFromEncoded(imageBytes).toComposeImageBitmap(),
                    contentDescription = "Category Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
               if(imageUrl.isEmpty()){
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
               }else{
                   val image: Resource<Painter> = asyncPainterResource(BASE_URL+imageUrl)
                   KamelImage(
                       resource = image,
                       contentDescription = "Category Image",
                       modifier = Modifier
                           .fillMaxWidth()
                           .clip(RoundedCornerShape(8.dp))
                           .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                       contentScale = ContentScale.Crop
                   )
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