package io.agaghd.agaghdnews.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import io.agaghd.agaghdnews.R
import io.agaghd.agaghdnews.activity.BaseActivity
import io.agaghd.agaghdnews.activity.WebActivity
import io.agaghd.agaghdnews.adapter.NewsAdapter
import io.agaghd.agaghdnews.network.RequestListener
import io.agaghd.agaghdnews.network.RequestUtil
import io.agaghd.agaghdnews.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_news.*
import org.json.JSONObject

/**
 * @author : agaghd
 * time : 2019/7/28
 * desc : 新闻
 */
class NewsFragment : Fragment() {

    val PAGE_SIZE = 40
    val newsAdapter = NewsAdapter(R.layout.item_news, mutableListOf())
    lateinit var mContext: Context
    var channel: String = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        val bundle = arguments
        channel = bundle?.getString("channel").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        loadNews(channel, PAGE_SIZE, 0)
    }

    fun initViews() {
        news_rv.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        news_rv.layoutManager = LinearLayoutManager(mContext)
        news_rv.itemAnimator = DefaultItemAnimator()
        news_rv.adapter = newsAdapter
        refresh_layout.setColorSchemeResources(R.color.theme_blue)
    }

    fun initListeners() {
        refresh_layout.setOnRefreshListener {
            newsAdapter.setNewData(mutableListOf())
            loadNews(channel, PAGE_SIZE, 0)
        }
        newsAdapter.setOnLoadMoreListener({
            loadNews(channel, PAGE_SIZE, newsAdapter.itemCount)
        }, news_rv)
        newsAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val intent = Intent(mContext, WebActivity::class.java)
            intent.putExtra("url", newsAdapter.getItem(position)?.optString("url"))
            intent.putExtra("channel", channel)
            startActivity(intent)
        }

    }

    fun loadNews(channel: String, pageSize: Int, start: Int) {
        if(mContext is BaseActivity && !refresh_layout.isRefreshing){
            (mContext as BaseActivity).showProgressDialog()
        }
        RequestUtil.getNewsByChannel(
            channel, pageSize, start,
            object : RequestListener {
                override fun onSuccess(response: String) {
                    if(mContext is BaseActivity){
                        (mContext as BaseActivity).closeProgressDialog()
                    }
                    if (!isVisible) {
                        return
                    }
                    refresh_layout.isRefreshing = false
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
                    if(mContext is BaseActivity){
                        (mContext as BaseActivity).closeProgressDialog()
                    }
                    refresh_layout.isRefreshing = false
                    ToastUtil.showToast(mContext, msg)
                    newsAdapter.loadMoreFail()
                }

            }
        )
    }

}