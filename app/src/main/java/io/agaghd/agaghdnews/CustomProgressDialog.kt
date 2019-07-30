package io.agaghd.agaghdnews

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.progress_dialog.*

class CustomProgressDialog : Dialog {

    constructor(context: Context, theme: Int) : super(context, theme) {
        setContentView(R.layout.progress_dialog)
        window?.attributes?.gravity = Gravity.CENTER
        gif_iv?.let { Glide.with(context).load(R.drawable.suwako).into(gif_iv) }
    }
}