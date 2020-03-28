package com.xiaochen.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xiaochen.base.utils.LoadingManager
import com.xiaochen.base.utils.LogUtil
import com.xiaochen.base.viewmodel.BaseViewModel

/**
 * <p>抽象的父类activity 所有activity继承当前类</p >
 * @author     zhenglecheng
 */
abstract class AbsBaseFragment<T : BaseViewModel> : Fragment(), BaseUI {

    lateinit var mView: View
    var mContext: Context? = null
    var mViewModel: T? = null
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
        initView()
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initListener()
    }

    /**
     * 加载数据方法
     */
    override fun initData() {
        // 创建ViewModel
        mViewModel = createViewModel()
        if (isShowLoading && mContext != null) {
            mViewModel?.bindLoading(LoadingManager.getLoading(mContext!!))
        }
        // 异常统一处理
        exceptionDispose()
    }

    /**
     * 异常统一处理
     */
    private fun exceptionDispose() {
        mViewModel?.run {
            this.mExceptionLiveData.observe(this@AbsBaseFragment, Observer {
                LogUtil.e("tag 报错了", it.message + "")
            })
        }
    }

    /**
     * 获取布局id
     */
    abstract fun getLayoutId(): Int

    /**
     *创建 ViewModel对象
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