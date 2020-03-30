package com.xiaochen.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaochen.base.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * <p>ViewModel基类</p >
 * @author     zhenglecheng
 */
open class BaseViewModel : ViewModel() {

    var loadingDialog: LoadingDialog? = null
    /**
     * 统一异常处理liveData
     */
    val mExceptionLiveData: MutableLiveData<Exception> = MutableLiveData()

    /**
     * ui线程中执行
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            showLoading()
            block()
            dismissLoading()
        }
    }

    /**
     * 默认线程中执行
     */
    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                showLoading()
            }
            block()
            withContext(Dispatchers.Main) {
                dismissLoading()
            }
        }
    }

    /**
     * io线程中执行
     */
    fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading()
            }
            block()
            withContext(Dispatchers.Main) {
                dismissLoading()
            }
        }
    }

    /**
     * 显示loading
     */
    fun showLoading() {
        loadingDialog?.show()
    }

    /**
     * 关闭loading
     */
    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    /**
     * 绑定loading
     */
    fun bindLoading(loading: LoadingDialog) {
        this.loadingDialog = loading
    }

    override fun onCleared() {
        dismissLoading()
        super.onCleared()
    }
}