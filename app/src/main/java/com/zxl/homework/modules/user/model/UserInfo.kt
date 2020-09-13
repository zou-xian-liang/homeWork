package com.zxl.homework.modules.user.model

import com.google.gson.annotations.SerializedName

/**
 * Created by zxl on 2020/9/11 10:51
 * 说明：用户信息
 */
data class UserInfo(
    val avatar: String,
    val nick: String,
    @SerializedName("profile-image")
    val profile: String,
    val username: String
)