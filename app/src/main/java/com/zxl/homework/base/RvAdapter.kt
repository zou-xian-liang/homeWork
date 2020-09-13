package com.zxl.homework.base

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zxl on 2020/9/12 11:15
 * 说明：将直接继承BaseQuickAdapter改为继承rvAdapter，针对不需要修改baseViewHolder的适配器
 */

abstract class RvAdapter<T>(@LayoutRes layoutResId: Int) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId)