package io.agaghd.agaghdnews.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import io.agaghd.agaghdnews.R
import io.agaghd.agaghdnews.adapter.ChannelAdapter
import io.agaghd.agaghdnews.adapter.NewsPagerAdapter
import io.agaghd.agaghdnews.fragment.NewsFragment
import io.agaghd.agaghdnews.network.RequestListener
import io.agaghd.agaghdnews.network.RequestUtil
import io.agaghd.agaghdnews.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

/**
 * @author : wjy
 * time : 2019/7/28
 * desc : 主页面
 */
class MainActivity : BaseActivity() {

    var channelAdapter = ChannelAdapter(R.layout.item_channel, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initListeners()
        loadChannel()
    }

    fun initViews() {
        channel_rv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        channel_rv.itemAnimator = DefaultItemAnimator()
        channel_rv.adapter = channelAdapter
    }

    fun initListeners() {
        channelAdapter.setOnItemClickListener { adapter, view, position ->
            channelAdapter.selectedChannel = channelAdapter.getItem(position).toString()
            news_pager.setCurrentItem(position, true)
        }
        news_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                channelAdapter.selectedChannel = channelAdapter.getItem(p0).toString()
                channel_rv.scrollToPosition(p0)
            }
        })
    }

    fun loadChannel() {
        RequestUtil.getChannel(object : RequestListener {
            override fun onSuccess(response: String) {
                try {
                    val jsonObject = JSONObject(response)
                    val result = jsonObject.optJSONObject("result")
                    val channelResult = result.optJSONArray("result")
                    val data = mutableListOf<String>()
                    val fragmentList = mutableListOf<Fragment>()
                    for (i in 0 until channelResult.length()) {
                        val channel = channelResult.getString(i)
                        val newsFragment = NewsFragment()
                        val bundle = Bundle()
                        bundle.putString("channel", channel)
                        newsFragment.arguments = bundle
                        fragmentList.add(newsFragment)
                        data.add(channel)
                    }
                    channelAdapter.addData(data)
                    channelAdapter.selectedChannel = channelAdapter.getItem(0).toString()
                    val newsPageAdapter = NewsPagerAdapter(supportFragmentManager, fragmentList)
                    news_pager.adapter = newsPageAdapter
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
