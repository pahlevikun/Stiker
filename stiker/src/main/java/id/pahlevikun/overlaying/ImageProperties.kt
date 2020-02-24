package id.pahlevikun.overlaying

import android.graphics.Bitmap
import androidx.annotation.IntDef

class ImageProperties constructor(internal val bitmap: Bitmap) {
    internal var width: Int =
        DEFAULT_WIDTH
    internal var height: Int =
        DEFAULT_HEIGHT
    internal var keepScale: Boolean = true
    @Position
    internal var position: Int =
        Position.TOP_LEFT

    fun setWidth(width: Int): ImageProperties {
        this.width = width
        return this
    }

    fun setHeight(height: Int): ImageProperties {
        this.height = height
        return this
    }

    fun setPosition(@Position position: Int): ImageProperties {
        this.position = position
        return this
    }

    fun keepScale(isKeepScale: Boolean): ImageProperties {
        this.keepScale = isKeepScale
        return this
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        Position.TOP_LEFT,
        Position.TOP_RIGHT,
        Position.BOTTOM_LEFT,
        Position.BOTTOM_RIGHT,
        Position.CENTER
    )
    annotation class Position {
        companion object {
            const val TOP_LEFT = 0
            const val TOP_RIGHT = 1
            const val BOTTOM_LEFT = 2
            const val BOTTOM_RIGHT = 3
            const val CENTER = 4
        }
    }

    companion object {
        private const val DEFAULT_WIDTH = 500
        private const val DEFAULT_HEIGHT = 500
    }
}