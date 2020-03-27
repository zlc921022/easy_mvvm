package com.xiaochen.data.request

/**
 * <p>request基类</p >
 * @author     zhenglecheng
 */
open class BaseRequest {
    private var token: String? = null
        set(value) {
            field = value
        }
        get() = field
}