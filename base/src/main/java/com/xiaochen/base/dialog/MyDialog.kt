package com.xiaochen.base.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView


import com.xiaochen.base.R

import java.lang.ref.WeakReference

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc : 对话框工具类
 */
class MyDialog private constructor(activity: Activity) : View.OnClickListener {

    private val mDialogOk: TextView
    private val mDialogCancel: TextView
    private val mTvContent: TextView
    private val mAlertDialog: AlertDialog
    private val mTvTitle: TextView

    //activity是否存在
    private val isActive: Boolean
        get() {
            val activity = mReference!!.get()
            return activity != null && !activity.isFinishing && !activity.isDestroyed
        }

    val isShowing: Boolean
        get() = mAlertDialog.isShowing

    private var mOnDialogBtnClickListener: OnDialogBtnClickListener? = null

    init {

        val builder = AlertDialog.Builder(activity)
        val dialogView = LayoutInflater.from(activity).inflate(
            R.layout.layout_dialog, null, false
        )
        builder.setView(dialogView)
        mAlertDialog = builder.create()

        mTvTitle = dialogView.findViewById(R.id.tv_title)
        mDialogOk = dialogView.findViewById(R.id.dialog_ok)
        mDialogCancel = dialogView.findViewById(R.id.dialog_cancel)
        mTvContent = dialogView.findViewById(R.id.tv_content)
        mDialogOk.setOnClickListener(this)
        mDialogCancel.setOnClickListener(this)
        if (mAlertDialog.window != null) {
            mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable())
        }
    }

    fun showDialog() {
        if (isActive) {
            mAlertDialog.setCanceledOnTouchOutside(false)
            if (!mAlertDialog.isShowing) {
                mAlertDialog.show()
            }
        }
    }

    fun dismiss() {
        if (isShowing) {
            mAlertDialog.dismiss()
        }
    }

    fun setTitle(title: String): MyDialog {
        mTvTitle.text = title
        return this
    }

    fun setContent(content: String): MyDialog {
        mTvContent.text = content
        return this
    }

    fun setDialogOkText(text: String): MyDialog {
        mDialogOk.text = text
        return this
    }

    fun setDialogCancelText(text: String): MyDialog {
        mDialogCancel.text = text
        return this
    }

    fun setDialogCancelShow(visible: Int): MyDialog {
        mDialogCancel.visibility = visible
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): MyDialog {
        mAlertDialog.setCanceledOnTouchOutside(cancel)
        return this
    }

    override fun onClick(v: View) {
        val id = v.id
        if (mOnDialogBtnClickListener == null) {
            return
        }
        if (id == R.id.dialog_ok) {
            dismiss()
            mOnDialogBtnClickListener!!.onBtnOkClick()
        } else if (id == R.id.dialog_cancel) {
            dismiss()
            mOnDialogBtnClickListener!!.onBtnCancelClick()
        }
    }

    interface OnDialogBtnClickListener {
        fun onBtnOkClick()

        fun onBtnCancelClick() {}
    }

    fun setOnDialogBtnClickListener(onDialogBtnClickListener: OnDialogBtnClickListener): MyDialog {
        mOnDialogBtnClickListener = onDialogBtnClickListener
        return this
    }

    companion object {
        private var mReference: WeakReference<Activity>? = null

        fun getDialog(activity: Activity): MyDialog {
            mReference = WeakReference(activity)
            return MyDialog(activity)
        }
    }
}
