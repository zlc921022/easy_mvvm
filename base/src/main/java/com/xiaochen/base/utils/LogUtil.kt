package com.xiaochen.base.utils

import timber.log.Timber

/**
 * Log统一管理类
 * @author zlc
 */
object LogUtil {

    //开发完毕将isDebug置为false
    private val isDebug = true

    fun init(){
        if(isDebug){
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * 打印i级别的log
     * @param tag
     * @param msg
     */
    fun i(tag: String, msg: String) {
        if (isDebug) {
            Timber.tag(tag).i(msg)
        }
    }

    /**
     * 打印e级别的log
     * @param tag
     * @param msg
     */
    fun e(tag: String, msg: String) {
        if (isDebug) {
            Timber.tag(tag).e(msg)
        }
    }

    /**
     * 方便打log
     * @param object
     * @param msg
     */
    fun i(`object`: Any, msg: String) {
        if (isDebug) {
            Timber.tag(`object`.javaClass.simpleName).i(msg)
        }
    }

    /**
     * 方便打log
     * @param object
     * @param msg
     */
    fun e(`object`: Any, msg: String) {
        if (isDebug) {
            Timber.tag(`object`.javaClass.simpleName).e(msg)
        }
    }

    /***
     * 错误日志
     * @param msg
     */
    fun e(msg: String) {
        Timber.tag("AndroidRuntime").e(msg)
    }
}
