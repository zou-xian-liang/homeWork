package com.zxl.homework.modules.user.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.zxl.homework.R
import com.zxl.homework.base.RvAdapter
import com.zxl.homework.modules.user.model.Image
import com.zxl.homework.util.ImageLoadUtil

/**
 * Created by zxl on 2020/9/12 18:16
 * 说明：朋友圈9图适配器
 */
class ImageAdapter : RvAdapter<Image>(R.layout.item_image) {
    override fun convert(helper: BaseViewHolder, item: Image) {
        helper.apply {
            ImageLoadUtil.with(mContext).loadUrl(item.url)
                .placeHolder(R.drawable.ic_load_fail).errorHolder(R.drawable.ic_load_fail)
                .into(getView(R.id.image))
        }
    }
}