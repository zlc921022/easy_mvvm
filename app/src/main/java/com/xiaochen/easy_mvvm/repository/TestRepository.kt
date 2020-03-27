package com.xiaochen.easy_mvvm.repository

import com.xiaochen.base.info.BaseResult
import com.xiaochen.base.repository.BaseRepository
import com.xiaochen.data.request.SendSmsReqVO
import com.xiaochen.data.response.HomeArticleRespVO
import com.xiaochen.data.response.SendSmsRespVO

/**
 * <p></p >
 * @author     zhenglecheng
 */
class TestRepository : BaseRepository() {

    suspend fun sendSms(phone : String): BaseResult<SendSmsRespVO.Data> {
        return safeApiCall(call = { requestSms(phone) }, errorMessage = "网络错误")
    }

    private suspend fun requestSms(phone : String): BaseResult<SendSmsRespVO.Data> {
        val reqVO = SendSmsReqVO()
        reqVO.phoneNo = phone
        return executeResponse(mApiManager.sendSms(reqVO))
    }

    suspend fun getArticle(page: Int): BaseResult<HomeArticleRespVO.Data> {
        return safeApiCall(call = { requestArticle(page) }, errorMessage = "网络错误")
    }

    private suspend fun requestArticle(page: Int): BaseResult<HomeArticleRespVO.Data> {
        return executeResponse(mApiManager.getHomeArticles(page))
    }
}