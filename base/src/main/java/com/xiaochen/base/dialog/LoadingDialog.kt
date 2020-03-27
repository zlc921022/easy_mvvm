package com.xiaochen.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.xiaochen.base.R
import com.xiaochen.base.utils.ImageUtil

/**
 * Email: zlc921022@163.com
 * Desc: loading对话框
 *
 * @author zlc
 */
class LoadingDialog private constructor(builder: Builder) : MaterialDialog(builder) {

    private var mLoadingIcon: ImageView? = null
    private val mContext: Context? = builder.context
    private var mDialog: MaterialDialog? = null

    init {
        initDialog()
    }

    private fun initDialog() {
        val builder = Builder(mContext!!)
        val layout = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null)
        mLoadingIcon = layout.findViewById(R.id.loading_icon)
        builder.customView(layout, false)
        mDialog = builder.build()
        mDialog!!.setCanceledOnTouchOutside(false)
    }

    /**
     * 显示loading
     */
    override fun show() {
        try {
            if (!isShowing) {
                mDialog?.show()
                setLoadingWindow()
                showGif()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun isShowing(): Boolean {
        return mDialog?.isShowing ?: false
    }

    /**
     * 显示gif图
     */
    private fun showGif() {
        if (mContext == null || mLoadingIcon == null) {
            return
        }
        ImageUtil.showGif2(mContext, mLoadingIcon!!, R.drawable.loading)
    }

    /**
     * 关闭弹框
     */
    override fun dismiss() {
        if (isShowing) {
            mDialog?.dismiss()
        }
    }

    /**
     * 设置loading 弹框的宽度和背景
     */
    private fun setLoadingWindow() {
        if (mContext == null || mDialog == null) {
            return
        }
        val window = mDialog!!.window
        if (window != null) {
            window.setDimAmount(0.20f)
            val lp = window.attributes
            val size = mContext.resources.getDimension(R.dimen.dp_80).toInt()
            lp.width = size
            lp.height = size
            window.attributes = lp
        }
    }

    companion object {

        fun getLoading(context: Context): LoadingDialog {
            return LoadingDialog(MaterialDialog.Builder(context))
        }
    }

}
