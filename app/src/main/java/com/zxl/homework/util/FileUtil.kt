package com.zxl.homework.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.zxl.homework.global.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Created by zxl on 2020/9/12 17:47
 * 说明：文件工具类
 */
object FileUtil {
    /**
     * 通过给定的文件路径，去获取该文件对应的bitmap对象
     * @param path
     * @return bitmap
     */
    fun getBitmapFromDisk(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        if (!TextUtils.isEmpty(path)) bitmap = BitmapFactory.decodeFile(path)
        return bitmap
    }

    /**
     * 将bitmap保存至磁盘中
     */
    suspend fun saveImgToDisk(url: String, bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val file = File(MyApp.context.cacheDir, Md5Util.encodeString(url))
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        }
    }

    /**
     * 获取指定url路径的图片在文件中的缓存路径
     * @param url String
     * @return String?
     */
    fun getCacheFilePath(url: String): String {
        val fileName = Md5Util.encodeString(url)
        //存在内部缓存  - android/data/data/包名/cache目录
        val file = File(MyApp.context.cacheDir, fileName)
        return file.absolutePath
    }
}