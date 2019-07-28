package io.agaghd.agaghdnews.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import io.agaghd.agaghdnews.R
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : BaseActivity() {

    lateinit var url: String
    lateinit var channel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        getDataFromIntent()
        initViews()
        setListeners()
        init()
    }

    fun getDataFromIntent() {
        url = intent.getStringExtra("url")
        channel = intent.getStringExtra("channel")
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initViews() {
        webview.setNetworkAvailable(true)
        webview.settings.javaScriptEnabled = true
        title_tv.text = channel
    }

    fun setListeners() {
        back_btn.setOnClickListener { finish() }
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    fun init() {
        webview.loadUrl(url)
    }

    override fun onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null)
            webview.clearHistory()
            (webview.parent as ViewGroup).removeView(webview)
            webview.destroy()
        }
        super.onDestroy()
    }
}