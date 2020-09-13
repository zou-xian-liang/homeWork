package com.zxl.homework.modules.user.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.chad.library.adapter.base.BaseViewHolder
import com.zxl.homework.R
import com.zxl.homework.base.RvAdapter
import com.zxl.homework.modules.user.model.Comment

/**
 * Created by zxl on 2020/9/12 18:13
 * 说明：朋友圈下的评论
 */
class CommentAdapter : RvAdapter<Comment>(R.layout.item_comment) {
    override fun convert(helper: BaseViewHolder, item: Comment) {
        val content = item.sender.username + "：" + item.content
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#5C6880")),
            0,
            item.sender.username.length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        helper.setText(R.id.tv_comment, spannableString)
    }
}