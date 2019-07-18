package io.agaghd.agaghdnews.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author  :   agaghd
 * time     :   2019/7/18
 * desc     :   retrofit回调实现
 */
class CallbackImpl(requestListener: RequestListener) : Callback<ResponseBody> {

    val listener: RequestListener = requestListener

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        if (response.body() != null) {
            try {
                val result = response.body()!!.string()
                listener.onSuccess(result)
            } catch (e: Throwable) {
                onFailure(call, e)
            }

        }
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        t.message?.let { listener.onError(it) }
    }
}