package com.xiaochen.easy_mvvm

import com.xiaochen.base.BaseApplication
import com.xiaochen.base.utils.LogUtil

/**
 * <p></p >
 * @author     zhenglecheng
 */
class MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.init()
    }
}