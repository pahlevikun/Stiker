package id.pahlevikun.stiker.generator

import android.content.Context
import android.graphics.*
import android.media.ThumbnailUtils
import android.util.TypedValue
import android.view.View
import id.pahlevikun.stiker.StikerProperties

internal object ImageGenerator {

    private const val DEFAULT_COLOR = "#E8E6E8"

    internal data class Data(
        val imageName: String,
        val shouldSaveImage: Boolean = false,
        val quality: Int,
        val cornerSize: Float,
        val background: StikerProperties,
        val foreground: StikerProperties
    )

    internal fun takeScreenShot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    internal fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, width, height, matrix, false
        )
        bitmap.recycle()
        return resizedBitmap
    }

    internal fun cropCenter(bitmap: Bitmap): Bitmap {
        val dimension = bitmap.width.coerceAtMost(bitmap.height)
        return ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
    }

    internal fun scaleCenterCrop(
        bitmap: Bitmap,
        newHeight: Int,
        newWidth: Int
    ): Bitmap {
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val xScale = newWidth.toFloat() / bitmapWidth
        val yScale = newHeight.toFloat() / bitmapHeight
        val scale = xScale.coerceAtLeast(yScale)
        val scaledWidth = scale * bitmapWidth
        val scaledHeight = scale * bitmapHeight
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
        val dest = Bitmap.createBitmap(newWidth, newHeight, bitmap.config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(bitmap, null, targetRect, null)
        return dest
    }

    internal fun overlayBitmap(backgroundBitmap: Bitmap, overlayBitmap: Bitmap): Bitmap {
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

    internal fun scaleBitmap(bitmap: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {
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

    internal fun createDefaultBitmap(
        context: Context,
        width: Int,
        height: Int,
        radius: Float = 0f
    ): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            val paint = Paint().apply {
                color = Color.parseColor(DEFAULT_COLOR)
            }
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val radius = radius.toDpFloat(context)

            Canvas(this).drawRoundRect(rect, radius, radius, paint)
        }
    }

    private fun Float.toDpFloat(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this,
            context.resources.displayMetrics
        )
    }

    fun stickThem(data: Data): Bitmap? {
        val backgroundImage = with(data.background) {
            getResizedBitmap(bitmap, width, height)
        }
        val sticker = with(data.foreground) {
            getResizedBitmap(bitmap, height, width)
        }

        return overlayBitmap(backgroundImage, sticker)
    }

}