package io.agaghd.agaghdnews.utils

import android.content.Context
import android.widget.Toast

object ToastUtil{
    fun showToast(context:Context,msg:CharSequence){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}
