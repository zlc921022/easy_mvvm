package com.xiaochen.base.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.xiaochen.base.utils.LoadingManager
import com.xiaochen.base.utils.LogUtil
import com.xiaochen.base.viewmodel.BaseViewModel

/**
 * <p>抽象的父类activity 所有activity继承当前类</p >
 * @author     zhenglecheng
 */
abstract class AbsBaseActivity<T : BaseViewModel> : AppCompatActivity(), BaseUI {

    var mViewModel: T? = null
    val tag: String? = javaClass.simpleName
    var isShowLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
        initListener()
    }

    override fun initView() {

    }

    /**
     * 加载数据方法
     */
    override fun initData() {
        // 创建ViewModel
        mViewModel = createViewModel()
        if (isShowLoading) {
            mViewModel?.bindLoading(LoadingManager.getLoading(this))
        }
        // 异常统一处理
        exceptionDispose()
    }

    /**
     * 异常统一处理
     */
    private fun exceptionDispose() {
        mViewModel?.run {
            this.mExceptionLiveData.observe(this@AbsBaseActivity, Observer {
                LogUtil.e("tag 报错了", it.message + "")
            })
        }
    }

    /**
     * 获取布局id
     */
    abstract fun getLayoutId(): Int

    /**
     * 创建 ViewModel对象
     */
    open fun createViewModel(): T? {
        return null
    }

    /**
     * 点击事件
     */
    override fun onClick(v: View?) {

    }
}