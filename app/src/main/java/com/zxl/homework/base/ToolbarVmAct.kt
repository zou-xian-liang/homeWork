package com.zxl.homework.base

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import com.zxl.homework.R
import com.zxl.homework.base.layout.StatusLayoutManager
import com.zxl.homework.util.setStatusBarHeight
import kotlinx.android.synthetic.main.layout_toolbar.*

abstract class ToolbarVmAct<VM : BaseViewModel> : BaseAct<VM>() {
    private lateinit var statusLayoutManager: StatusLayoutManager
    override fun initView() {
        //动态设置状态栏高度
        setStatusBarHeight(findViewById(R.id.statusBar), this)
        getActionBarToolbar()
        initLayoutManager()
    }

    override fun initData() {
        mViewModel.loadState.value = LoadState.LOADING
        getNewData()
    }

    abstract fun getNewData()
    override fun observe() {
        mViewModel.apply {
            loadState.observe(this@ToolbarVmAct, Observer {
                when (it) {
                    LoadState.LOADING -> onRequestLoading()
                    LoadState.SUCCESS -> onRequestSuccess()
                    LoadState.EMPTY -> onNoData(noDataMsg())
                    LoadState.ERROR -> onNoConnect()
                }
            })
        }
    }

    open fun noDataMsg(msg: String = "暂无数据"): String = msg

    private fun getActionBarToolbar() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        image_back.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive && currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            finish()
        }
    }

    private fun initLayoutManager() {
        statusLayoutManager = StatusLayoutManager.newBuilder(this)
            .emptyDataView(R.layout.view_empty)
            .loadingView(R.layout.view_progress)
            .netWorkErrorView(R.layout.view_network_error)
            .retryViewId(R.id.retry)
            .onRetryListener { onRetryLoad() }
            .build()
        main_ll.addView(statusLayoutManager.rootLayout)
    }

    //请求重试
    protected open fun onRetryLoad() {
        mViewModel.loadState.value = LoadState.LOADING
        getNewData()
    }


    /**
     * 无网络连接
     */
    open fun onNoConnect() {
        statusLayoutManager!!.showNetWorkError()
    }

    /**
     * 正在加载中...
     */
    open fun onRequestLoading() {
        main_ll.visibility = View.VISIBLE
        statusLayoutManager!!.showLoading()
    }

    /**
     * 请求成功
     */
    open fun onRequestSuccess() {
        main_ll.visibility = View.GONE
    }

    open fun onNoData(msg: String?) {
        statusLayoutManager!!.showEmptyData()
        val emptyText = statusLayoutManager!!.rootLayout.findViewById<TextView>(R.id.empty_text)
        if (emptyText != null && !TextUtils.isEmpty(msg)) {
            emptyText.text = msg
        }
    }
}