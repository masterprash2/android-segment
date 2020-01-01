package com.timesanimation.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL

/**
 * @author Yogesh Kumar on 7/5/18
 */
object Utils {
    /**
     * Checks whether a list is empty and null or not
     *
     * @param list
     * @return
     */
    fun isNullOrEmpty(list: List<*>?): Boolean {
        return if (list != null && !list.isEmpty()) false else true
    }

    /**
     * Checks internet connectivity which is used before making any network call
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Utility method for hiding keyboard
     *
     * @param act
     */
    fun hideKeyboard(act: Activity?) {
        if (act != null && act.currentFocus != null) {
            val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(act.currentFocus.windowToken, 0)
            }
        }
    }

    /**
     * Utility method for showing keyboard
     *
     * @param act
     */
    fun showKeyboard(act: Activity?) {
        if (act != null && act.currentFocus != null) {
            val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, 0)
        }
    }

    /**
     * Used to generate a bitmap by fetching it from
     *
     * @param url       and the compressing it to save memory space
     * @param url
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    fun decodeSampledBitmapFromResource(url: URL, resId: Rect?, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), resId, options)
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(url.openConnection().getInputStream(), resId, options)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Helper method to calculate sample bitmap size
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize > reqHeight
                    && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * Binder method helper to set view height
     *
     * @param view
     * @param height
     */
    fun setLayoutHeight(view: View?, height: Int) {
        if (view == null) return
        val layoutParams = view.layoutParams
        if (layoutParams != null && layoutParams.height != height) {
            layoutParams.height = height
            view.layoutParams = layoutParams
        }
    }

    /**
     * Binder method helper to set view width
     *
     * @param view
     * @param width
     */
    fun setWidth(view: View, width: Float) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(width.toInt(), view.height)
        } else {
            params.width = width.toInt()
        }
        view.layoutParams = params
    }

    /**
     * This methods is used to convert bitmap into byte array because we cannot store bitmap
     * in room database
     * @param bitmap
     * @return
     */
    fun getBytArrayFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        //bitmap.recycle();
        return stream.toByteArray()
    }

    /**
     * This method is used to convert byte array into bitmap
     * @param bitmapByteArray
     * @return
     */
    fun getBitmapFromByteArray(bitmapByteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.size)
    }

    @JvmStatic
    fun getScreenHeight(context: Activity): Int {
        val displaymetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.heightPixels
    }

    fun getScreenWidth(context: Activity): Int {
        val displaymetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.widthPixels
    }

    @JvmStatic
    fun getDimensionFromDp(dimenInDp: Float, context: Activity): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimenInDp, context.resources.displayMetrics)
    }
}