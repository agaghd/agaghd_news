package io.agaghd.agaghdnews

import android.os.Bundle
import io.agaghd.agaghdnews.network.RequestListener
import io.agaghd.agaghdnews.network.RequestUtil
import io.agaghd.agaghdnews.utils.BaseActivity
import io.agaghd.agaghdnews.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RequestUtil.getChannel(object : RequestListener {
            override fun onSuccess(reponse: String) {
                hello_world_tv.append("\n" + reponse + "\n")
            }

            override fun onError(msg: String) {
                ToastUtil.showToast(mContext, msg)
            }

        })
        RequestUtil.getNewsByChannel("财经", 10, 0, object : RequestListener {
            override fun onSuccess(reponse: String) {
                hello_world_tv.append("\n" + reponse + "\n")
            }

            override fun onError(msg: String) {
                ToastUtil.showToast(mContext, msg)
            }

        })
        RequestUtil.newSearch("科创板", object : RequestListener {
            override fun onSuccess(reponse: String) {
                hello_world_tv.append("\n" + reponse + "\n")
            }

            override fun onError(msg: String) {
                ToastUtil.showToast(mContext, msg)
            }

        })
    }

}
