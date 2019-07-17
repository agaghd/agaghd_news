package io.agaghd.agaghdnews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.agaghd.agaghdnews.network.NewsApi
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var retrofit = Retrofit.Builder().baseUrl(BuildConfig.API_URL).build()
        var newsApi = retrofit.create(NewsApi::class.java)
        newsApi.getChannel().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.body()!=null){
                    try {
                        var result = response.body()!!.string()
                        Log.i("wtf", response.body()!!.string())
                        hello_world_tv.text= result
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }
}
