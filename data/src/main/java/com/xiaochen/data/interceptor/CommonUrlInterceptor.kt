package com.xiaochen.data.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * <p>通用url拦截器</p >
 * @author     zhenglecheng
 */
class CommonUrlInterceptor : Interceptor {
    /**
     * baseUrl
     */
    private val BASE_URL = "dynamic_url"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()
        val builder = request.newBuilder()
        val list = request.headers(BASE_URL)
        if (list.isNotEmpty()) {
            builder.removeHeader(BASE_URL)
            val baseUrl = HttpUrl.parse(list[0])
            //重建新的HttpUrl，需要重新设置的url部分
            baseUrl?.let {
                val httpUrl = url.newBuilder()
                    // http / https
                    .scheme(it.scheme())
                    // 主机地址
                    .host(it.host())
                    // 端口
                    .port(it.port())
                    .build()
                val newRequest = builder.url(httpUrl).build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(request)
    }

}