package io.agaghd.agaghdnews.utils

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author agaghd
 * time 2019/7/28
 * desc 图片变换
 *
 */
class RoundRectCrop(radius: Int) : BitmapTransformation() {

    var mRadius: Int = radius

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val toResuse = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val result = toResuse
        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, toTransform.width.toFloat(), toTransform.height.toFloat())
        canvas.drawRoundRect(rectF, mRadius.toFloat(), mRadius.toFloat(), paint)
        return result
    }
}