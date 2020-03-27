package com.xiaochen.data.interceptor

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * <p>通用请求头拦截器</p >
 * @author     zhenglecheng
 */
class CommonHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.headers(getHeads())
        return chain.proceed(builder.build())
    }

    /**
     * 添加通用请求头并获取请求头
     */
    private fun getHeads(): Headers {
        val heads = HashMap<String, String>()
//        heads["token"] = ""
//        heads["id"] = ""
//        heads["cookie"] = ""
        return Headers.of(heads)
    }

}