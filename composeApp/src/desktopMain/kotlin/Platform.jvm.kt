import androidx.compose.ui.graphics.ImageBitmap
import java.awt.image.BufferedImage

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
fun BufferedImage.toComposeImageBitmap(): ImageBitmap {
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getRGB(0, 0, width, height, pixels, 0, width)
    return ImageBitmap(width, height).apply {
        this.readPixels(pixels)
    }
}