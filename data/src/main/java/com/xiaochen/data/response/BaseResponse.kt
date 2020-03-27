package com.xiaochen.data.response

/**
 * <p>response基类</p >
 * @author     zhenglecheng
 */
open class BaseResponse<T> {
    val errorCode: Int? = null
    val errorMsg: String? = null
    val data: T? = null
}