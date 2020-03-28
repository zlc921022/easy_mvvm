package com.xiaochen.data

import com.xiaochen.data.interceptor.CommonHeaderInterceptor
import com.xiaochen.data.interceptor.CommonParamInterceptor
import com.xiaochen.data.interceptor.HttpLogerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * <p>http网络请求提供类</p >
 * @author     zhenglecheng
 */
object HttpManager {

    private var mBaseUrl = BASE_URL
    private var isDebug = IS_DEBUG
    private var isAuthorization = IS_AUTHORIZATION


    //获取okHttp对象
    private val okHttpClient: OkHttpClient
    private val mRetrofit: Retrofit

    init {
        this.okHttpClient = createOkHttpClient()
        this.mRetrofit = createRetrofit()
    }

    //创建Retrofit
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(mBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //创建OkHttpClient
    private fun createOkHttpClient(): OkHttpClient {

        val srcBuilder: OkHttpClient.Builder = if (!isAuthorization) {
            OkHttpClient.Builder()
                .addInterceptors(getInterceptors())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        } else {
            OkHttpClient.Builder()
                .addInterceptors(getInterceptors())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        }
        //处理https
        httpSSl(srcBuilder)
        return srcBuilder.build()
    }

    //处理https
    private fun httpSSl(srcBuilder: OkHttpClient.Builder) {
        if (mBaseUrl.startsWith("https:")) {
            try {
                val sslParams = SSLUtil.getSslSocketFactory(null, null, null)
                srcBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                srcBuilder.hostnameVerifier { _, _ -> true }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 添加通用请求头和参数拦截器
     */
    private fun OkHttpClient.Builder.addInterceptors(list: List<Interceptor>): OkHttpClient.Builder {
        for (interceptor in list) {
            this.addInterceptor(interceptor)
        }
        return this
    }

    /**
     * 获取需要添加的拦截器链
     */
    private fun getInterceptors(): List<Interceptor> {
        val list = ArrayList<Interceptor>()
        list.add(CommonHeaderInterceptor())
        list.add(CommonParamInterceptor())
        val logInterceptor = getLogInterceptor()
        logInterceptor?.let {
            list.add(it)
        }
        return list
    }

    /**
     *  获取日志拦截器
     */
    private fun getLogInterceptor(): Interceptor? {
        if (isDebug) {
            val loggingInterceptor = HttpLogerInterceptor(object :
                HttpLogerInterceptor.Logger {
                override fun log(message: String) {
                    try {
                        Timber.tag("HTTP").e(message)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
            loggingInterceptor.setLevel(HttpLogerInterceptor.Level.BODY)
            return loggingInterceptor
        }
        return null
    }

    /**
     * 生成api对象
     */
    fun <T> createApi(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

}