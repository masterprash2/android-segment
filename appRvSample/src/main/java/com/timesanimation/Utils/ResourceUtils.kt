package com.timesanimation.Utils

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils

/**
 * @author Yogesh Kumar on 7/5/18
 */
object ResourceUtils {
    private var mContext: Context? = null
    @JvmStatic
    fun initialize(context: Context?) {
        mContext = context
    }

    fun getString(id: Int): String {
        return mContext!!.resources.getString(id)
    }

    fun getDrawable(id: Int): Drawable {
        return mContext!!.resources.getDrawable(id)
    }

    fun getString(id: Int, number: Int): String {
        return mContext!!.resources.getString(id, number)
    }

    fun getString(id: Int, vararg formatArgs: Any?): String {
        return mContext!!.resources.getString(id, *formatArgs)
    }

    fun getBoolean(id: Int): Boolean {
        return mContext!!.resources.getBoolean(id)
    }

    fun getColor(color: Int): Int {
        return mContext!!.resources.getColor(color)
    }

    fun getColorWithAlpha(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        return ColorUtils.setAlphaComponent(mContext!!.resources.getColor(color), (alpha * 255f).toInt())
    }

    fun getDimensionPixelOffset(dimen: Int): Int {
        return mContext!!.resources.getDimensionPixelOffset(dimen)
    }

    fun getDimensionPixelSize(dimen: Int): Int {
        return mContext!!.resources.getDimensionPixelSize(dimen)
    }

    val displayMetrics: DisplayMetrics
        get() = mContext!!.resources.displayMetrics

    fun getDimension(dimen: Int): Float {
        return mContext!!.resources.getDimension(dimen)
    }

    fun getIdentifier(name: String?, defType: String?, defPackage: String?): Int {
        return mContext!!.resources.getIdentifier(name, defType, defPackage)
    }

    val resource: Resources
        get() = mContext!!.resources

    val assetManager: AssetManager
        get() = mContext!!.assets

    fun getImageSpan(bitmap: Bitmap?): ImageSpan {
        return ImageSpan(mContext, bitmap)
    }

    val configuration: Configuration
        get() = mContext!!.resources.configuration
}