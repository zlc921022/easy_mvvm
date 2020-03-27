package com.xiaochen.data

/**
 * <p>api管理类</p >
 * @author     zhenglecheng
 */
object ApiManager {

    private var mHttpProvider = HttpProvider

    /**
     * 获得api接口对象
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> createApi(service: Class<T>): T {
        return mHttpProvider.createApi(service)
    }
}