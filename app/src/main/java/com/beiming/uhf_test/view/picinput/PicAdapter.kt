package com.beiming.uhf_test.view.picinput

import android.view.View
import android.widget.ImageView
import com.beiming.uhf_test.R
import com.beiming.uhf_test.bean.pic.PhotoBean
import com.beiming.uhf_test.utils.GlideUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.entity.LocalMedia

/**
 * created by zhangshi on 2022/8/23.
 */
class PicAdapter(photos: List<LocalMedia>) : BaseQuickAdapter<LocalMedia, BaseViewHolder>
    (R.layout.gv_filter_image, photos) {



    override fun convert(helper: BaseViewHolder, item: LocalMedia) {

        val iv: ImageView = helper.getView(R.id.fiv)
        val ivDel: ImageView = helper.getView(R.id.iv_del)
        if (helper.absoluteAdapterPosition == (data.size-1)) {
            iv.setImageResource(R.drawable.add)
            ivDel.visibility = View.GONE
        } else{
            GlideUtil.load(mContext, iv, item.realPath)
            ivDel.visibility = View.VISIBLE
            ivDel.setOnClickListener{
                val index = helper.absoluteAdapterPosition
                data.removeAt(index)
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, data.size)
            }
        }

    }


}