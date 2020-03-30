package com.xiaochen.base.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * <p>抽象的父类activity 所有activity继承当前类</p >
 * @author     zhenglecheng
 */
abstract class AbsBaseActivity : AppCompatActivity(), View.OnClickListener {

    val tag: String? = javaClass.simpleName
    var isShowLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
        initListener()
    }

    /**
     * 控件初始化
     */
    open fun initView() {

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