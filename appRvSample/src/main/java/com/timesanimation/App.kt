package com.timesanimation

import com.timesanimation.Utils.ResourceUtils
import android.app.Application

/**
 * Created by Yogesh Kumar.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ResourceUtils.initialize(applicationContext)
    }
}