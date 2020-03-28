package com.xiaochen.base.utils

import android.content.Context
import com.xiaochen.base.dialog.LoadingDialog

/**
 * <p>loading管理类</p>
 * @author    zhenglecheng
 * @date      2020/3/28
 */
object LoadingManager {

    var mLoading: LoadingDialog? = null
    fun getLoading(context: Context): LoadingDialog {
        if (mLoading == null) {
            mLoading = LoadingDialog.getLoading(context)
        }
        return mLoading!!
    }

    fun show() {
        mLoading?.show()
    }

    fun dismiss() {
        if (mLoading != null) {
            mLoading?.dismiss()
            mLoading = null
        }
    }
}