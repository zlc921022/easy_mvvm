package com.xiaochen.data.api

import com.xiaochen.data.request.SendSmsReqVO
import com.xiaochen.data.response.HomeArticleRespVO
import com.xiaochen.data.response.SendSmsRespVO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *
 * 服务器接口{d}
 *
 * @author zhenglecheng
 */
interface IServiceApi {

    /**
     * 发送短信验证码
     *
     * @param reqVO 请求对象
     */
    @POST("ezy/sendSms")
    suspend fun sendSms(@Body reqVO: SendSmsReqVO): SendSmsRespVO
    /**
     * 首页文章列表
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): HomeArticleRespVO
}
