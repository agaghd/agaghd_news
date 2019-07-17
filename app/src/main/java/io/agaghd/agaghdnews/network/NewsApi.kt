package io.agaghd.agaghdnews.network

import io.agaghd.agaghdnews.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET


/**
 *
 */
interface NewsApi {
    @GET("channel?appkey=" + BuildConfig.API_KEY)
    fun getChannel(): Call<ResponseBody>


}