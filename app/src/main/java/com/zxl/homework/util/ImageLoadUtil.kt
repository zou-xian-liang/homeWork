package com.zxl.homework.util

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.zxl.homework.R
import com.zxl.homework.util.FileUtil.getBitmapFromDisk
import com.zxl.homework.util.FileUtil.getCacheFilePath
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Created by zxl on 2020/9/12 18:01
 * 说明：自己实现一个图片加载库
 */
class ImageLoadUtil {
    companion object {
        private var instance = ImageLoadUtil()
        private var mContext: Context? = null
        fun with(context: Context): ImageLoadUtil {
            mContext = context
            return instance
        }
    }

    /**
     * 设置图片链接
     * @param url String
     */
    fun loadUrl(url: String): RequestBuilder {
        return RequestBuilder(url)
    }

    inner class RequestBuilder(private var url: String) {
        private lateinit var mImageView: ImageView
        private var mPlaceholder = 0
        private var mError = 0

        /**
         * 占位图
         * @param placeholder Int
         */
        fun placeHolder(@DrawableRes placeholder: Int): RequestBuilder {
            mPlaceholder = placeholder
            return this
        }

        /**
         * 加载错误时候的占位图
         * @param error  Int
         */
        fun errorHolder(@DrawableRes error: Int): RequestBuilder {
            mError = error
            return this
        }

        /**
         * 需要设置url的imageView
         * @param imageView
         */
        fun into(imageView: ImageView) {
            mImageView = imageView
            if (mPlaceholder != 0) mImageView.setImageResource(mPlaceholder)
            val path = getCacheFilePath(url)
            val diskBitmap = getBitmapFromDisk(path)
            if (diskBitmap != null) mImageView.setImageBitmap(diskBitmap)
            else {
                try {
                    MainScope().launch {
                        val bitmap = BitmapUtil.getBitmapFromNet(url)
                        if (bitmap == null) {
                            if (mError != 0) mImageView.setImageResource(mError)
                            loge("网络获取图片失败=${url}")
                        } else {
                            mImageView.setImageBitmap(bitmap)
                            //保存到本地
                            FileUtil.saveImgToDisk(url, bitmap)
                        }
                    }
                } catch (e: Exception) {
                    loge("网络获取图片异常=${e}==${url}")
                    if (mError != 0) mImageView.setImageResource(mError)
                }
            }
        }
    }
}