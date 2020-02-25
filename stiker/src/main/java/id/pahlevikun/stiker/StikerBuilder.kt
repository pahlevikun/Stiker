package id.pahlevikun.stiker

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import id.pahlevikun.stiker.generator.ImageGenerator

class StikerBuilder internal constructor(private val context: Context) {

    private var isSaveImage: Boolean = false
    private var imageName: String? = null
    private var backgroundProperties: StikerProperties? = null
    private var foregroundProperties: StikerProperties? = null
    private var cornerSize: Float = 0f
    private var quality: Int = 100

    fun setBackground(backgroundProperties: StikerProperties): StikerBuilder {
        this.backgroundProperties = backgroundProperties
        return this
    }

    fun putStiker(foregroundProperties: StikerProperties): StikerBuilder {
        this.foregroundProperties = foregroundProperties
        return this
    }

    fun setRoundedCorner(cornerSize: Float): StikerBuilder {
        this.cornerSize = cornerSize
        return this
    }

    fun setQuality(quality: Int): StikerBuilder {
        this.quality = quality
        return this
    }

    fun saveImage(name: String? = null): StikerBuilder {
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