package id.pahlevikun.overlaying

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import id.pahlevikun.overlaying.generator.ImageGenerator

class OverlayBuilder(private val context: Context) {

    private var isSaveImage: Boolean = false
    private var imageName: String? = null
    private var backgroundProperties: ImageProperties? = null
    private var foregroundProperties: ImageProperties? = null
    private var cornerSize: Float = 0f
    private var quality: Int = 100

    fun setBackground(backgroundProperties: ImageProperties): OverlayBuilder {
        this.backgroundProperties = backgroundProperties
        return this
    }

    fun setOverlay(foregroundProperties: ImageProperties): OverlayBuilder {
        this.foregroundProperties = foregroundProperties
        return this
    }

    fun setRoundedCorner(cornerSize: Float): OverlayBuilder {
        this.cornerSize = cornerSize
        return this
    }

    fun setQuality(quality: Int): OverlayBuilder {
        this.quality = quality
        return this
    }

    fun saveImage(name: String? = null): OverlayBuilder {
        this.isSaveImage = true
        this.imageName = name
        return this
    }

    fun buildAsBitmap(): Bitmap? {
        return null
    }

    fun takeScreenShot(view: View): Bitmap {
        return ImageGenerator.takeScreenShot(view)
    }
}