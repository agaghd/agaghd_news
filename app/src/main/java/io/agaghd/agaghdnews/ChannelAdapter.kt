package io.agaghd.agaghdnews

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

public class ChannelAdapter(layoutResId: Int, data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    var selectedChannel = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.item_tv, item)
        val bgColor: Int
        val textColor: Int
        if (item == selectedChannel) {
            bgColor = helper?.itemView?.context?.resources?.getColor(R.color.theme_blue)!!
            textColor = Color.WHITE
        } else {
            bgColor = Color.WHITE
            textColor = helper?.itemView?.context?.resources?.getColor(R.color.text_color)!!
        }
        helper.setBackgroundColor(R.id.item_root, bgColor)
        helper.setTextColor(R.id.item_tv, textColor)
    }


}