package rpt.tool.waterdiary.utils.view.custom

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import rpt.tool.waterdiary.utils.helpers.StringHelper
import java.security.MessageDigest
import kotlin.math.min

class CircleTransform(context: Context?, color: String?) : BitmapTransformation() {
    var sh: StringHelper = StringHelper(context!!, (context as Activity?)!!)

    init {
        paintBorder1 = Paint()
        if (!sh.check_blank_data(color!!)) paintBorder1.color =
            Color.parseColor(color)
        else paintBorder1.color = Color.TRANSPARENT

        paintBorder1.isAntiAlias = true
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        return circleCrop(pool, toTransform)
    }

    val id: String
        get() = javaClass.name

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    companion object {
        lateinit var paintBorder1: Paint
        private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
            if (source == null) return null

            val size =
                min(source.width.toDouble(), source.height.toDouble()).toInt()
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            // TODO this could be acquired from the pool too
            val squared = Bitmap.createBitmap(source, x, y, size, size)

            val result: Bitmap = pool.get(size, size, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(result)
            val paint = Paint()
            paint.setShader(
                BitmapShader(
                    squared,
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP
                )
            )
            paint.isAntiAlias = true
            val r = size / 2f

            val paintBorder = Paint()

            paintBorder.isAntiAlias = true

            canvas.drawCircle(r, r, r, paintBorder1)
            canvas.drawCircle(r, r, r - 1.5f, paint)

            return result
        }
    }
}
