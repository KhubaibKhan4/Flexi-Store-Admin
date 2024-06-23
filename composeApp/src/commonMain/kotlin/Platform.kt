import androidx.compose.ui.graphics.ImageBitmap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
// shared module
expect fun ByteArray.toImageBitmap(): ImageBitmap
