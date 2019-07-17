package io.agaghd.agaghdnews.utils

import android.util.Log

open class K1Max{
    fun max():Int{
        return 0
    }

    interface Min{
        fun min()
    }
}

class K1JP :  K1Max(),K1Max.Min {
    override fun min() {
        Log.i("wtf", "great")
    }

}