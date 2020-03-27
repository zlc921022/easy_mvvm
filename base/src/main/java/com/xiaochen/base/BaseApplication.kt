package com.xiaochen.base

import android.app.Application
import android.content.Context

import androidx.multidex.MultiDex

/**
 * 父类application
 * 负责context和apiManager对象的创建和获取
 * @author admin
 */
open class BaseApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {

        var application: BaseApplication? = null
         get() = field
    }
}
