package io.agaghd.agaghdnews

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import io.agaghd.agaghdnews.network.RequestListener
import io.agaghd.agaghdnews.network.RequestUtil
import io.agaghd.agaghdnews.utils.BaseActivity
import io.agaghd.agaghdnews.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    var channelAdapter = ChannelAdapter(R.layout.item_channel, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initListeners()
        loadChannel()
    }

    fun initRecyclerView() {
        channel_rv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        channel_rv.itemAnimator = DefaultItemAnimator()
        channel_rv.adapter = channelAdapter
    }

    fun initListeners() {
        channelAdapter.setOnItemClickListener { adapter, view, position ->
            channelAdapter.selectedChannel = channelAdapter.getItem(position).toString()
        }
    }

    fun loadChannel() {
        RequestUtil.getChannel(object : RequestListener {
            override fun onSuccess(reponse: String) {
                try {
                    val jsonObject = JSONObject(reponse)
                    val result = jsonObject.optJSONObject("result")
                    val channelResult = result.optJSONArray("result")
                    val data = mutableListOf<String>()
                    for (i in 0 until channelResult.length()) {
                        data.add(channelResult.getString(i))
                    }
                    channelAdapter.addData(data)
                    channelAdapter.selectedChannel = channelAdapter.getItem(0).toString()
                } catch (e: Exception) {
                    e.message?.let { onError(it) }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.showToast(mContext, msg)
            }

        })
    }
}
