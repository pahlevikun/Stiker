package id.pahlevikun.simpleimage

import android.annotation.SuppressLint
import android.graphics.*
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.pahlevikun.overlaying.Image
import id.pahlevikun.overlaying.ImageProperties
import id.pahlevikun.overlaying.OverlayBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    private var formatter: SimpleDateFormat = SimpleDateFormat("ddMMyyyy_HHmmss")
    private var output: FileOutputStream? = null
    private var dirPath = ""
    private var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_save.setOnClickListener { doSaveImage() }
        Image.with(this)
        OverlayBuilder(this).buildAsBitmap()
    }

    private fun createImageI() {
        val bitmapPolosan =
            Bitmap.createBitmap(
                DEFAULT_BACKGROUND_WIDTH,
                DEFAULT_BACKGROUND_HEIGHT,
                Bitmap.Config.ARGB_8888
            ).apply {
                val paint = Paint().apply {
                    color = Color.parseColor(DEFAULT_COLOR)
                }
                Canvas(this).drawBitmap(
                    scaleCenterCrop(BitmapFactory.decodeResource(resources, R.drawable.belalang)),
                    0f,
                    0f,
                    paint
                )
            }
        val bitmapLogo = getResizedBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.gofood),
            DEFAULT_OVERLAY_HEIGHT,
            DEFAULT_OVERLAY_WIDTH
        )

        val a = OverlayBuilder(this)
            .setBackground(
                ImageProperties(bitmapPolosan)
                    .setHeight(500)
                    .setWidth(500)
                    .keepScale(false)
            )
            .setOverlay(
                ImageProperties(bitmapLogo)
                    .setHeight(250)
                    .setWidth(250)
                    .setPosition(ImageProperties.Position.BOTTOM_LEFT)
                    .keepScale(true)
            )
            .setRoundedCorner(2f)
            .saveImage()
            .buildAsBitmap()

        val screenShot = OverlayBuilder(this).takeScreenShot(button_save)
    }

    private fun doSaveImage() {
        val bitmapPolosan =
            Bitmap.createBitmap(
                DEFAULT_BACKGROUND_WIDTH,
                DEFAULT_BACKGROUND_HEIGHT,
                Bitmap.Config.ARGB_8888
            ).apply {
                val paint = Paint().apply {
                    color = Color.parseColor(DEFAULT_COLOR)
                }
                Canvas(this).drawBitmap(
                    scaleCenterCrop(BitmapFactory.decodeResource(resources, R.drawable.belalang)),
                    0f,
                    0f,
                    paint
                )
            }
        val bitmapLogo = getResizedBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.gofood),
            DEFAULT_OVERLAY_HEIGHT,
            DEFAULT_OVERLAY_WIDTH
        )
        val bitmapOverlay = overlayBitmap(
            bitmapPolosan,
            bitmapLogo
        )
        try {
            val date = Date()
            dirPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() +
                    File.separator + getString(R.string.app_name) + File.separator
            val newDirectory = File(dirPath)
            newDirectory.mkdirs()

            fileName = "twb_img_${formatter.format(date)}.png"

            output = FileOutputStream("$dirPath$fileName")
            bitmapOverlay.compress(Bitmap.CompressFormat.PNG, 100, output)
            output!!.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    private fun cropCenter(bmp: Bitmap): Bitmap {
        val dimension = bmp.width.coerceAtMost(bmp.height)
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension)
    }

    private fun scaleCenterCrop(
        source: Bitmap,
        newHeight: Int = DEFAULT_BACKGROUND_HEIGHT,
        newWidth: Int = DEFAULT_BACKGROUND_WIDTH
    ): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = xScale.coerceAtLeast(yScale)
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)
        return dest
    }

    private fun overlayBitmap(backgroundBitmap: Bitmap, overlayBitmap: Bitmap): Bitmap {
        val bitmap =
            Bitmap.createBitmap(
                backgroundBitmap.width,
                backgroundBitmap.height,
                backgroundBitmap.config
            )
        Canvas(bitmap).apply {
            drawBitmap(backgroundBitmap, Matrix(), null)
            drawBitmap(overlayBitmap, Matrix(), null)
        }
        return bitmap
    }

    private fun scaleBitmap(bitmap: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {
        return if (maxHeight > 0 && maxWidth > 0) {
            val width = bitmap.width
            val height = bitmap.height
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
        } else {
            bitmap
        }
    }

    companion object {
        const val DEFAULT_BACKGROUND_WIDTH = 500
        const val DEFAULT_BACKGROUND_HEIGHT = 500
        const val DEFAULT_OVERLAY_WIDTH = 80
        const val DEFAULT_OVERLAY_HEIGHT = 250
        const val DEFAULT_COLOR = "#E8E6E8"
        const val DEFAULT_RADIUS = 8f
    }
}
