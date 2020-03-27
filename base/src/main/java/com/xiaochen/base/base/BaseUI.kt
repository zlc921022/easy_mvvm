package com.xiaochen.base.base

import android.view.View

/**
 * <p>基类</p >
 * @author     zhenglecheng
 */
interface BaseUI : View.OnClickListener{
    /**
     * 初始化控件
     */
    fun initView()

    /**
     * 加载数据
     */
    fun initData()

    /**
     * 点击事件处理
     */
    fun initListener()
}