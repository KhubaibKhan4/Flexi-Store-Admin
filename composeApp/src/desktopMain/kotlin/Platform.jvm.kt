import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun ByteArray.toImageBitmap(): ImageBitmap {
    val inputStream = this.inputStream()
    val bufferedImage: BufferedImage = ImageIO.read(inputStream)
    return bufferedImage.toComposeImageBitmap()
}

fun BufferedImage.toComposeImageBitmap(): ImageBitmap {
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getRGB(0, 0, width, height, pixels, 0, width)
    return ImageBitmap(width, height).apply {
        this.readPixels(pixels)
    }
}