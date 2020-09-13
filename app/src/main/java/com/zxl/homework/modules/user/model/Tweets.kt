package com.zxl.homework.modules.user.model

import com.google.gson.annotations.SerializedName

/**
 * Created by zxl on 2020/9/11 10:36
 * 个人朋友圈数据
 */

data class Tweets(
    val comments: List<Comment>,
    val content: String,
    val images: List<Image>,
    val sender: Sender,
    @SerializedName("unknown error")
    val unknownError: String,
    val error: String
)

data class Comment(
    val content: String,
    val sender: Sender
)

data class Image(
    val url: String
)


data class Sender(
    val avatar: String,
    val nick: String,
    val username: String
)