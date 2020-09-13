package com.zxl.homework.modules.user.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.zxl.homework.R
import com.zxl.homework.base.RvAdapter
import com.zxl.homework.modules.user.model.Tweets
import com.zxl.homework.util.ImageLoadUtil
import com.zxl.homework.widget.VerticalItemDecoration


/**
 * Created by zxl on 2020/9/11 11:18
 * 说明：个人朋友圈列表
 */
class TweetsAdapter : RvAdapter<Tweets>(R.layout.item_user_tweets) {
    override fun convert(holder: BaseViewHolder, item: Tweets) {
        holder.apply {
            ImageLoadUtil.with(mContext).loadUrl(item.sender.avatar)
                .placeHolder(R.drawable.ic_error_avatar).errorHolder(R.drawable.ic_error_avatar)
                .into(getView(R.id.iv_user_avatar))
            setText(R.id.tv_user_name, item.sender.username)
            setText(R.id.tv_content, item.content)
            getView<TextView>(R.id.tv_content).visibility =
                if (TextUtils.isEmpty(item.content)) View.GONE else View.VISIBLE
            getView<RecyclerView>(R.id.recyclerView).apply {
                if (item.images.isNullOrEmpty().not()) {
                    when (item.images.size) {
                        1, 2, 4 -> layoutManager = GridLayoutManager(mContext, 2)
                        else -> GridLayoutManager(mContext, 3)
                    }
                }
                adapter = ImageAdapter().apply {
                    if (item.images.isNullOrEmpty().not()) setNewData(item.images)
                }
            }


            CommentAdapter().apply {
                bindToRecyclerView(getView(R.id.rv_comment))
                holder.getView<RecyclerView>(R.id.rv_comment).addItemDecoration(
                    VerticalItemDecoration(
                        mContext.resources.getColor(R.color.ea),
                        2
                    )
                )
                if (item.comments.isNullOrEmpty().not()) setNewData(item.comments)
            }
        }
    }
}