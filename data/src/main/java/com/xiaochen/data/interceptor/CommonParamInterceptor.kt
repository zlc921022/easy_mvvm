package com.xiaochen.data.interceptor

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*

/**
 * <p>添加通用参数拦截器</p >
 * @author     zhenglecheng
 */
class CommonParamInterceptor : Interceptor {

    private val mCommonParams = HashMap<String, Any>()

    init {
        // 添加通用参数
        mCommonParams["id"] = ""
        mCommonParams["token"] = ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return when {
            request.method().equals("GET", true) -> {
                val newRequest = getAddParams(request)
                chain.proceed(newRequest)
            }
            request.body() is FormBody -> {
                val newRequest = postAddParams(request)
                chain.proceed(newRequest)
            }
            else -> {
                val newRequest = gsonAddParams(request)
                chain.proceed(newRequest)
            }
        }
    }

    /**
     * get方式添加通用参数
     * @param request 原始请求
     */
    private fun getAddParams(request: Request): Request {
        val builder = request.url().newBuilder()
        for (entry in mCommonParams.entries) {
            builder.addQueryParameter(entry.key, entry.value.toString())
        }
        val httpUrl = builder.build()
        return request.newBuilder()
            .url(httpUrl)
            .build()
    }

    /**
     * post方式 form表单添加通用参数
     * @param request 原始请求
     */
    private fun postAddParams(request: Request): Request {
        val builder = FormBody.Builder()
        val body = request.body() as FormBody
        // 添加通用参数
        for (entry in mCommonParams.entries) {
            builder.add(entry.key, entry.value.toString())
        }
        // 以前的参数添加
        for (i in 0..body.size()) {
            if (!mCommonParams.containsKey(body.name(i))) {
                builder.add(body.encodedName(i), body.encodedValue(i))
            }
        }
        return request.newBuilder()
            .post(builder.build())
            .build()
    }

    /**
     * gson 方式添加参数
     */
    private fun gsonAddParams(request: Request): Request {
        val gson = Gson()
        val buffer = okio.Buffer()
        request.body()?.writeTo(buffer)
        val paramStr = buffer.readUtf8()
        val mapType = object : TypeToken<HashMap<String, Any>>() {}.type
        val map: HashMap<String, Any> = gson.fromJson(paramStr, mapType)
        if (map.isNotEmpty() && mCommonParams.isNotEmpty()) {
            for (entry in mCommonParams.entries) {
                if (!map.containsKey(entry.key)) {
                    map[entry.key] = entry.value.toString()
                }
            }
        }
        val json = gson.toJson(map)
        val type = MediaType.parse("application/json; charset=UTF-8")
        val requestBody = RequestBody.create(type, json)
        return Request.Builder()
            .url(request.url())
            .post(requestBody)
            .build()
    }
}