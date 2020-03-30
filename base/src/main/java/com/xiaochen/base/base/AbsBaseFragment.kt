package com.xiaochen.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * <p>抽象的父类activity 所有activity继承当前类</p >
 * @author     zhenglecheng
 */
abstract class AbsBaseFragment : Fragment(), View.OnClickListener {

    var mView: View? = null
    var mContext: Context? = null
    var isShowLoading: Boolean = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(getLayoutId(), container, false)
        return mView!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mView?.let {
            initView(mView)
            initData()
            initListener()
        }
    }

    /**
     * 控件初始化
     */
    open fun initView(view: View?) {

    }

    /**
     * 加载数据方法
     */
    open fun initData() {

    }

    /**
     * 点击事件监听
     */
    open fun initListener() {

    }

    /**
     * 获取布局id
     */
    abstract fun getLayoutId(): Int

    /**
     * 点击事件
     */
    override fun onClick(v: View?) {

    }
}