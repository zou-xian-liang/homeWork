package com.zxl.homework.modules.user.activity

import android.graphics.Color
import com.google.android.material.appbar.AppBarLayout
import com.zxl.homework.R
import com.zxl.homework.base.BaseAct
import com.zxl.homework.modules.user.adapter.TweetsAdapter
import com.zxl.homework.modules.user.vm.UserViewModel
import com.zxl.homework.util.ImageLoadUtil
import com.zxl.homework.util.dp2px
import com.zxl.homework.util.setStatusBarHeight
import kotlinx.android.synthetic.main.act_user_tweets.*

/**
 * Created by zxl on 2020/9/12 18:52
 * 说明：个人朋友圈
 */
class UserTweetsListAct : BaseAct<UserViewModel>() {
    private var mPageIndex = 1
    private lateinit var mAdapter: TweetsAdapter
    override fun viewModelClass(): Class<UserViewModel> = UserViewModel::class.java
    override fun setLayout(): Int = R.layout.act_user_tweets
    override fun initView() {
        setStatusBarHeight(statusBar, this)
        tv_title.text = getString(R.string.tweets)
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, i ->
            refresh.isEnabled = i == 0
            if (-i < dp2px(105f)) {
                toolbar.setBackgroundColor(Color.argb(-i * 255 / dp2px(105f), 255, 255, 255))
                tv_title.setTextColor(Color.argb(-i * 255 / dp2px(105f), 51, 51, 51))
            } else {
                tv_title.setTextColor(resources.getColor(R.color.title))
                toolbar.setBackgroundColor(Color.WHITE)
            }
        })

        mViewModel.apply {
            getTweetsList(mPageIndex)
            getUserInfo()
        }

        mAdapter = TweetsAdapter().apply {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({
                mPageIndex++
                mViewModel.getTweetsList(mPageIndex)
            }, recyclerView)
        }
        refresh.apply {
            isRefreshing = true
            setProgressViewOffset(false, 190, 390)
            setOnRefreshListener {
                mPageIndex = 1
                mViewModel.getTweetsList(mPageIndex)
                mAdapter.setEnableLoadMore(false)
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.apply {
            mUserProfile.observe(this@UserTweetsListAct, {
                tv_name.text = it.nick
                ImageLoadUtil.with(this@UserTweetsListAct).loadUrl(it.avatar).placeHolder(R.drawable.ic_error_avatar).errorHolder(R.drawable.ic_error_avatar).into(iv_user_avatar)
                ImageLoadUtil.with(this@UserTweetsListAct).loadUrl(it.profile).placeHolder(R.drawable.user_profile_normal).errorHolder(R.drawable.user_profile_normal).into(iv_bg)
            })

            mCurrentPageIndexData.observe(this@UserTweetsListAct, { data ->
                if (mPageIndex == 1) mAdapter.setNewData(null)
                mAdapter.addData(data)
                mAdapter.loadMoreComplete()
                if (data.size < 5) mAdapter.loadMoreEnd(true)
            })
            isRefresh.observe(this@UserTweetsListAct, { refresh.isRefreshing = it })
            isLoadMoreEnd.observe(this@UserTweetsListAct, { if (it) mAdapter.loadMoreEnd(true) })
        }
    }
}