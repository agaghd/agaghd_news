package io.agaghd.agaghdnews.others

import android.util.Log

open class K1Max{
    fun max():Int{
        return 0
    }

    interface Min{
        fun min()
    }
}

class K1JP :  K1Max(), K1Max.Min {
    override fun min() {
        Log.i("wtf", "great")
    }

}

interface Base{
    fun print()
}

class BaseImpl(val x:Int) : Base {
    override fun print() {
        print(x)
    }
}

class Derived(b : Base){

}

