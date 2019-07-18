package io.agaghd.agaghdnews.network

import io.agaghd.agaghdnews.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * @author agaghd
 * time :   2019/7/18
 * desc :   京东万象新闻接口，所有
 */
interface NewsApi {

    @GET("channel?appkey=" + BuildConfig.API_KEY)
    fun getChannel(): Call<ResponseBody>

    @GET("get?appkey=" + BuildConfig.API_KEY)
    fun getNewsByChannel(
        @Query("channel") channel: String,
        @Query("num") num: Int,
        @Query("start") start: Int
    ): Call<ResponseBody>

    @GET("newSearch?appkey=" + BuildConfig.API_KEY)
    fun newSearch(@Query("keyword") keyword: String): Call<ResponseBody>
}