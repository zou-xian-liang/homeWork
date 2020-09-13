package com.zxl.homework.modules.user.vm

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.zxl.homework.base.BaseViewModel
import com.zxl.homework.modules.user.model.Tweets
import com.zxl.homework.modules.user.model.UserInfo
import com.zxl.homework.net.apiService
import com.zxl.homework.util.loge

/**
 * Created by zxl on 2020/9/12 14:08
 * 说明：用户朋友圈vm
 */
class UserViewModel : BaseViewModel() {
    private var mAllTweetsList = mutableListOf<Tweets>()
    var mCurrentPageIndexData = MutableLiveData<MutableList<Tweets>>()
    var mUserProfile = MutableLiveData<UserInfo>()
    fun getTweetsList(pageIndex: Int) {
        launch({
            if (pageIndex == 1) {
                val tweets = apiService.getTweets()
                for (i in tweets.indices.reversed()) {
                    if (TextUtils.isEmpty(tweets[i].error)
                            .not() || TextUtils.isEmpty(tweets[i].unknownError)
                            .not() || (TextUtils.isEmpty(tweets[i].content) && tweets[i].images.isNullOrEmpty())
                    ) {
                        tweets.removeAt(i)
                    }
                }
                mAllTweetsList = tweets
                loge(tweets.size)
                mCurrentPageIndexData.value = when {
                    mAllTweetsList.isNullOrEmpty() -> {
                        isLoadMoreEnd.value = true
                        null
                    }
                    mAllTweetsList.size >= 5 -> mAllTweetsList.subList(0, 5)
                    else -> {
                        isLoadMoreEnd.value = true
                        mAllTweetsList
                    }
                }
                isRefresh.value = false
            } else {
                mAllTweetsList.apply {
                    mCurrentPageIndexData.value = when {
                        mAllTweetsList.size > (pageIndex - 1) * 5 && mAllTweetsList.size <= pageIndex * 5 -> {
                            isLoadMoreEnd.value = true
                            mAllTweetsList.subList((pageIndex - 1) * 5, mAllTweetsList.size)
                        }
                        else -> {
                            isLoadMoreEnd.value = false
                            mAllTweetsList.subList(
                                (pageIndex - 1) * 5,
                                mAllTweetsList.size - (pageIndex - 1) * 5
                            )
                        }
                    }
                }
                isRefresh.value = false
            }
        }, error = {
            isRefresh.value = false
        })
    }

    fun getUserInfo() {
        launch({
            mUserProfile.value = apiService.getUser()
        }, error = {
            isRefresh.value = false
        })
    }

}