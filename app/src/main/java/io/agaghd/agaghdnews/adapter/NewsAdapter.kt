package io.agaghd.agaghdnews.adapter

import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import io.agaghd.agaghdnews.R
import io.agaghd.agaghdnews.utils.RoundRectCrop
import org.json.JSONObject

public class NewsAdapter(layoutResId: Int, data: MutableList<JSONObject>?) :
    BaseQuickAdapter<JSONObject, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: JSONObject?) {
        val title = item?.optString("title", "无标题")
        helper?.setText(R.id.title_tv, title)
        val timeSrc = item?.optString("src", "未知来源") + " " + item?.optString("time", "未知时间")
        helper?.setText(R.id.time_src_tv, timeSrc)
        val picUrl = item?.optString("pic", "")
        if (!TextUtils.isEmpty(picUrl)) {
            helper?.setVisible(R.id.pic_iv, true)
            val picIv = helper?.itemView?.findViewById<ImageView>(R.id.pic_iv)
            Glide.with(picIv!!).asBitmap().load(picUrl).transform(CenterCrop(), RoundRectCrop(8)).into(picIv)
        } else {
            helper?.setVisible(R.id.pic_iv, false)
        }
    }


}