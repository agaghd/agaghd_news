package io.agaghd.agaghdnews.activity

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import io.agaghd.agaghdnews.R
import io.agaghd.agaghdnews.adapter.ChannelAdapter
import io.agaghd.agaghdnews.adapter.NewsAdapter
import io.agaghd.agaghdnews.network.RequestListener
import io.agaghd.agaghdnews.network.RequestUtil
import io.agaghd.agaghdnews.utils.BaseActivity
import io.agaghd.agaghdnews.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val PAGE_SIZE = 40
    var channelAdapter = ChannelAdapter(R.layout.item_channel, mutableListOf())
    val newsAdapter = NewsAdapter(R.layout.item_news, mutableListOf())

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
        news_rv.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        news_rv.layoutManager = LinearLayoutManager(mContext)
        news_rv.itemAnimator = DefaultItemAnimator()
        news_rv.adapter = newsAdapter
    }

    fun initListeners() {
        channelAdapter.setOnItemClickListener { adapter, view, position ->
            channelAdapter.selectedChannel = channelAdapter.getItem(position).toString()
            newsAdapter.setNewData(mutableListOf())
            loadNews(channelAdapter.selectedChannel, PAGE_SIZE, channelAdapter.itemCount)
        }
        newsAdapter.setOnLoadMoreListener({
            loadNews(channelAdapter.selectedChannel, PAGE_SIZE, channelAdapter.itemCount)
        }, news_rv)
    }

    fun loadChannel() {
        RequestUtil.getChannel(object : RequestListener {
            override fun onSuccess(response: String) {
                try {
                    val jsonObject = JSONObject(response)
                    val result = jsonObject.optJSONObject("result")
                    val channelResult = result.optJSONArray("result")
                    val data = mutableListOf<String>()
                    for (i in 0 until channelResult.length()) {
                        data.add(channelResult.getString(i))
                    }
                    channelAdapter.addData(data)
                    channelAdapter.selectedChannel = channelAdapter.getItem(0).toString()
                    loadNews(channelAdapter.selectedChannel, PAGE_SIZE, 0)
                } catch (e: Exception) {
                    e.message?.let { onError(it) }
                }
            }

            override fun onError(msg: String) {
                ToastUtil.showToast(mContext, msg)
            }

        })
    }

    fun loadNews(channel: String, pageSize: Int, start: Int) {
        RequestUtil.getNewsByChannel(
            channel, pageSize, start,
            object : RequestListener {
                override fun onSuccess(response: String) {
                    try {
                        val jsonObject = JSONObject(response)
                        val result1 = jsonObject.optJSONObject("result")
                        val result2 = result1.optJSONObject("result")
                        val list = result2.optJSONArray("list")
                        val data = mutableListOf<JSONObject>()
                        for (i in 0 until list.length()) {
                            data.add(list.getJSONObject(i))
                        }
                        newsAdapter.addData(data)
                        if (data.size == PAGE_SIZE) {
                            newsAdapter.loadMoreComplete()
                        } else {
                            newsAdapter.loadMoreEnd()
                        }
                    } catch (e: Exception) {
                        e.message?.let { onError(it) }
                    }
                }

                override fun onError(msg: String) {
                    ToastUtil.showToast(mContext, msg)
                    newsAdapter.loadMoreFail()
                }

            }
        )
    }
}
