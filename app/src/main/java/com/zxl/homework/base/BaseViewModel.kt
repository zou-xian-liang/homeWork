package com.zxl.homework.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.zxl.homework.util.loge
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit


open class BaseViewModel : ViewModel() {
    val loadState: MutableLiveData<LoadState> = MutableLiveData()
    var isRefresh: MutableLiveData<Boolean> = MutableLiveData<Boolean>().default(true)
    var isLoadMoreEnd: MutableLiveData<Boolean> = MutableLiveData<Boolean>().default(false)

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(block: Block<Unit>, error: Error? = null, cancel: Cancel? = null): Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke() }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     */
    private fun onError(e: Exception) {
        when (e) {
            is ConnectException -> {
                // 连接失败
                loadState.value = LoadState.ERROR
                loge("网络异常")
            }
            is SocketTimeoutException -> {
                // 请求超时
                loadState.value = LoadState.ERROR
            }
            is JsonParseException -> {
                // 数据解析错误
                loadState.value = LoadState.EMPTY
            }
            else -> {
                // 其他错误
                e.message?.let { loge(it) }
            }
        }
    }


}

/**
 * 给LiveData设置默认值
 * @receiver MutableLiveData<T>
 * @param initialValue T    默认值
 * @return MutableLiveData<T>
 */
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }