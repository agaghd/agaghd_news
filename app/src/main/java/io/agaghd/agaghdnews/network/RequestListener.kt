package io.agaghd.agaghdnews.network

/**
 * @author  :   agaghd
 * time :   2019/7/18
 * desc :   网络请求的封装回调
 */
interface RequestListener {
    fun onSuccess(reponse: String)
    fun onError(msg: String)
}