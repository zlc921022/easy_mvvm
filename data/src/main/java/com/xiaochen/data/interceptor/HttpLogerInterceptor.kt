package com.xiaochen.data.interceptor

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.INFO
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 * okhttp日志拦截器
 */
class HttpLogerInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) :
    Interceptor {

    //默认不打印日志
    @Volatile
    private var level: Level =
        Level.NONE

    enum class Level {
        //不显示日志
        NONE,
        //显示Content-Type 和 Content-Length等信息
        HEADERS,
        //显示所有的日志信息
        BODY
    }

    interface Logger {
        fun log(message: String)

        companion object {

            /**
             * A [HttpLogerInterceptor.Logger] defaults output appropriate for the current platform.
             */
            val DEFAULT: Logger = object :
                Logger {
                override fun log(message: String) {
                    Platform.get().log(INFO, message, null)
                }
            }
        }
    }

    //设置日志打印级别
    fun setLevel(level: Level?): HttpLogerInterceptor {
        if (level == null) {
            throw NullPointerException("level == null. Use Level.NONE instead.")
        }
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val level = this.level
        val request = chain.request()
        val requestContentType = request.header("Content-Type")
        //NONE日志级别或者上传文件
        if (level == Level.NONE || requestContentType != null && requestContentType == "multipart/form-data") {
            return chain.proceed(request)
        }

        //判断日志级别
        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        //获取请求主体对象
        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        //--------------------------------------请求信息---------------------------------------------
        //获取连接对象
        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        logger.log("--------------------------------请求开始--------------------------------------")
        var requestStartMessage =
            "--> " + request.method() + ' '.toString() + request.url() + ' '.toString() + protocol
        //BODY级别日志才获取requestBody的长度
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody?.contentLength() + "-byte data)"
        }
        logger.log(requestStartMessage)

        if (logHeaders) {
            //判断requestBody是否为空
            if (hasRequestBody) {
                if (requestBody?.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType()!!)
                }
                if (requestBody?.contentLength()?.toInt() != -1) {
                    logger.log("Content-Length: " + requestBody?.contentLength())
                }
            }

            //获取请求头部信息
            val headers = request.headers()
            run {
                val count = headers.size()
                val sb = StringBuffer()
                for (i in 0 until count) {
                    val name = headers.name(i)
                    if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(
                            name, ignoreCase = true
                        )
                    ) {
                        sb.append("\"$name\":").append("\"${headers.value(i)}\",")
                    }
                }
                if(sb.isNotEmpty()){
                    logger.log("header: { ${sb.delete(sb.length-1,sb.length)}}")
                }
            }

            //判断requestBody不为空，并且请求参数为POST时
            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method())
            } else {
                val sb = StringBuilder()
                if (requestBody is FormBody) {
                    val body = requestBody as FormBody?
                    for (i in 0 until body!!.size()) {
                        sb.append(body.encodedName(i)).append(":")
                            .append(body.encodedValue(i))
                            .append(if (i != body.size() - 1) "," else "")
                    }
                    logger.log("Request: {$sb}")
                } else {
                    val buffer = Buffer()
                    requestBody?.writeTo(buffer)
                    logger.log("Request: " + buffer.readString(Charset.forName("UTF-8")))
                }
                logger.log(
                    "--> END " + request.method()
                            + " (" + requestBody?.contentLength() + "-byte data)"
                )
            }
        }

        //--------------------------------------响应信息---------------------------------------------
        //获取开始时间
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }

        //计算总时间
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        //获取响应内容
        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength.toInt() != -1) "$contentLength-byte" else "unknown-length"
        logger.log(
            "<-- " + response.code() + ' '.toString() + response.message() + ' '.toString()
                    + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders)
                ", "
                        + bodySize + " data"
            else
                "") + ')'.toString()
        )

        //打印响应日志信息
        if (logHeaders) {

            //获取响应头部信息
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                if (headers.name(i).contains("Content")) {
                    logger.log(headers.name(i) + ": " + headers.value(i))
                }
                i++
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP")
            } else {

                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire data.
                val buffer = source.buffer()

                var charset: Charset? =
                    UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8)
                    } catch (e: UnsupportedCharsetException) {
                        logger.log("")
                        logger.log("Couldn't decode the response data; charset is likely malformed.")
                        logger.log("<-- END HTTP")
                        return response
                    }

                }

                if (contentLength != 0L) {
                    logger.log("")
                    logger.log("Response: " + buffer.clone().readString(charset!!))
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte data)")
            }
        }
        logger.log("--------------------------------响应结束--------------------------------------")
        return response
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }
}