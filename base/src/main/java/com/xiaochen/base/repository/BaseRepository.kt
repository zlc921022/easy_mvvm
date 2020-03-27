package com.xiaochen.base.repository

import android.text.TextUtils
import com.xiaochen.base.info.BaseResult
import com.xiaochen.data.ApiManager
import com.xiaochen.data.api.IServiceApi
import com.xiaochen.data.response.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

/**
 * <p>Repository基类</p >
 * @author     zhenglecheng
 */
open class BaseRepository {

    val mApiManager = ApiManager.createApi(IServiceApi::class.java)

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> BaseResult<T>,
        errorMessage: String
    ): BaseResult<T> {
        return try {
            call()
        } catch (e: Throwable) {
            if (TextUtils.isEmpty(e.message)) {
                BaseResult.Error(IOException(errorMessage, e))
            } else {
                BaseResult.Error(IOException(e.message, e))
            }
        }
    }

    suspend fun <T : Any> executeResponse(
        response: BaseResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): BaseResult<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                BaseResult.Error(IOException(response.errorMsg))
            } else {
                successBlock?.let { it() }
                BaseResult.Success(response.data)
            }
        }
    }
}