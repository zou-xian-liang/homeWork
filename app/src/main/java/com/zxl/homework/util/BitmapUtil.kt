package com.zxl.homework.util

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by zxl on 2020/9/12 18:00
 * 说明：Bitmap工具类
 */
object BitmapUtil {

    /**
     * 从图片地址获取Bitmap
     * @param imageUrl 网络图片地址
     * @return bitmap 图片
     */
     suspend fun getBitmapFromNet(imageUrl: String) = withContext(Dispatchers.Default) {
        try {
            val url = URL(imageUrl)
            val openConnection = url.openConnection() as HttpURLConnection
            openConnection.requestMethod = "GET"
            openConnection.connectTimeout = 20000
            openConnection.connect()
            if (openConnection.responseCode==200){
                val inputStream = openConnection.inputStream
                BitmapFactory.decodeStream(inputStream)
            }else{
                null
            }
        }catch (exception:Exception){
            null
        }
    }
}