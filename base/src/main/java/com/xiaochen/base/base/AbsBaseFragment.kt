package com.xiaochen.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xiaochen.base.dialog.LoadingDialog
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
    var loading: LoadingDialog? = null
    var isShowLoading: Boolean? = false

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
     * 初始化View
     */
    override fun initView() {
        if (isShowLoading == true) {
            mContext?.run {
                loading = LoadingDialog.getLoading(this)
            }
        }
    }

    /**
     * 加载数据方法
     */
    override fun initData() {
        // 创建ViewModel
        mViewModel = createViewModel()
        // 异常统一处理
        exceptionDispose()
        // loading统一处理
        loadingDispose()
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
     * loading统一处理
     */
    private fun loadingDispose() {
        mViewModel?.run {
            this.mLoadingLiveData.observe(this@AbsBaseFragment, Observer {
                if (isShowLoading == false) {
                    return@Observer
                }
                if (it == true) {
                    this@AbsBaseFragment.showLoading()
                } else {
                    this@AbsBaseFragment.dismissLoading()
                }
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
    abstract fun createViewModel(): T

    /**
     * 显示loading
     */
    fun showLoading() {
        loading?.show()
    }

    /**
     * 关闭loading
     */
    fun dismissLoading() {
        loading?.dismiss()
    }

    /**
     * 点击事件
     */
    override fun onClick(v: View?) {

    }
}