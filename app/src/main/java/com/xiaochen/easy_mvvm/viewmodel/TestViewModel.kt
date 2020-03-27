package com.xiaochen.easy_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaochen.base.info.BaseResult
import com.xiaochen.base.viewmodel.BaseViewModel
import com.xiaochen.data.response.HomeArticleRespVO
import com.xiaochen.data.response.SendSmsRespVO
import com.xiaochen.easy_mvvm.repository.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * <p></p >
 * @author     zhenglecheng
 */
class TestViewModel : BaseViewModel() {

    val mSmsData: MutableLiveData<SendSmsRespVO.Data> = MutableLiveData()
    val mHomeLiveData: MutableLiveData<HomeArticleRespVO.Data> = MutableLiveData()
    private val testRepository = TestRepository()

    fun sendSms(phone: String) {
        launchUI {
            val result = withContext(Dispatchers.IO) {
                testRepository.sendSms(phone)
            }
            if (result is BaseResult.Success) {
                mSmsData.value = result.data
            } else if (result is BaseResult.Error) {
                mExceptionLiveData.value = result.exception
            }
        }
    }

    fun getArticleInfo(page: Int) {
        launchUI {
            val result = withContext(Dispatchers.IO) {
                testRepository.getArticle(page)
            }
            if (result is BaseResult.Success) {
                mHomeLiveData.value = result.data
            } else if (result is BaseResult.Error) {
                mExceptionLiveData.value = result.exception
            }
        }
    }

}