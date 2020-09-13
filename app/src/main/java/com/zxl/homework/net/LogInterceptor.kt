package com.zxl.homework.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 * 添加公共的请求参数
 *
 * 打印请求数据以及响应数据
 */
class LogInterceptor : Interceptor {

    private var requestParam = ""
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        //------------添加公共参数--------start-----------
       /* if (request.method == "POST" && request.body is FormBody) {
            val requestBuilder = request.newBuilder()
            val oldFormBody = request.body as FormBody?
            val newFormBodyBuilder = FormBody.Builder()
            newFormBodyBuilder.add("PlatformType", "2")
            val paramSize = oldFormBody!!.size
            if (paramSize > 0) for (i in 0 until paramSize) newFormBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i))
            requestBuilder.post(newFormBodyBuilder.build())
            request = requestBuilder.build()
        }
        //------------添加公共参数------end------------*/

        //------------打印请求数据以及响应数据-----------
        val requestBody = request.body
        if (requestBody != null) {//这里是POST请求
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset = Charset.forName("UTF-8")
            val contentType = requestBody.contentType()
            charset = contentType!!.charset(charset)
            requestParam = buffer.readString(charset)
        }
        val startTime = System.nanoTime()
        //发起请求
        val response = chain.proceed(request)
        //===========这是响应体信息=========
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) //响应的时长
        val responseBody = response.body
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer()
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(charset)
            } catch (e: UnsupportedCharsetException) {
                e.printStackTrace()
            }
        }
        Log.e("Frame", "${request.method}：${request.url}?$requestParam\n响应时长:${tookMs}ms ${buffer.clone().readString(charset)}")
        //------------打印请求数据以及响应数据-----------
        return response
    }

}