import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jio.sdksampleapp.R
import java.io.File
import kotlin.math.ceil

@Composable
fun CustomAlertDialogWithImage(imagePath: String, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Text(text = "Image from SD Card")
        },
        text = {
            val imageBitmap = rememberImageBitmapFromPath(imagePath, 200, 200)
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.sample_background),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = { onClose() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Close")
            }
        }
    )
}

@Composable
fun rememberImageBitmapFromPath(path: String, maxWidth: Int, maxHeight: Int): ImageBitmap? {
    val file = File(path)
    if (file.exists()) {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)
            val imageHeight: Int = outHeight
            val imageWidth: Int = outWidth
            var scaleFactor = 1

            if (imageWidth > maxWidth || imageHeight > maxHeight) {
                val heightRatio: Double = (imageHeight.toDouble() / maxHeight.toDouble()).coerceAtLeast(1.0)
                val widthRatio: Double = (imageWidth.toDouble() / maxWidth.toDouble()).coerceAtLeast(1.0)
                scaleFactor = ceil(heightRatio.coerceAtLeast(widthRatio)).toInt()
            }

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        val bitmap = BitmapFactory.decodeFile(path, options)

        if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
            val aspectRatio: Float = bitmap.width.toFloat() / bitmap.height.toFloat()
            val newWidth = if (bitmap.width > bitmap.height) maxWidth else (maxHeight * aspectRatio).toInt()
            val newHeight = if (bitmap.height > bitmap.width) maxHeight else (maxWidth / aspectRatio).toInt()

            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            bitmap.recycle()

            return scaledBitmap.asImageBitmap()
        }

        return bitmap.asImageBitmap()
    }
    return null
}
