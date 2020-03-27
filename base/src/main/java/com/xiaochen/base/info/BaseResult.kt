package com.xiaochen.base.info

/**
 * <p>1</p >
 * @author     zhenglecheng
 */
sealed class BaseResult<out T : Any> {

    /**
     * 请求成功返回
     */
    data class Success<out T : Any>(val data: T?) : BaseResult<T>()

    /**
     * 请求失败返回
     */
    data class Error(val exception: Exception) : BaseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}