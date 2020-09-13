package com.zxl.homework.net

import com.zxl.homework.modules.user.model.UserInfo
import com.zxl.homework.modules.user.model.Tweets
import retrofit2.http.GET

val apiService by lazy { RetrofitClient.getService<ApiService>() }

interface ApiService {

    //    @GET("userInfo.json")
    @GET("user/jsmith")
    suspend fun getUser(): UserInfo

    //    @GET("circle.json")
    @GET("user/jsmith/tweets")
    suspend fun getTweets(): MutableList<Tweets>

}
