package io.agaghd.agaghdnews

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getVersionInfo()
    }

    fun getVersionInfo() {
        //TODO 获取版本信息和升级
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 2500)

    }


}