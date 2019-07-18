package io.agaghd.agaghdnews.network

import io.agaghd.agaghdnews.BuildConfig
import retrofit2.Retrofit

/**
 * @author  :   agaghd
 * time :   2019/7/18
 * desc :   封装的请求工具
 */
object RequestUtil {

    val retrofit = Retrofit.Builder().baseUrl(BuildConfig.API_URL).build()
    val newsApi = retrofit.create(NewsApi::class.java)

    fun getChannel(requestListener: RequestListener) {
        newsApi.getChannel().enqueue(CallbackImpl(requestListener))
    }

    fun getNewsByChannel(channel: String, num: Int, start: Int, requestListener: RequestListener) {
        newsApi.getNewsByChannel(channel, num, start).enqueue(CallbackImpl(requestListener))
    }

    fun newSearch(keyword: String, requestListener: RequestListener) {
        newsApi.newSearch(keyword).enqueue(CallbackImpl(requestListener))
    }
}