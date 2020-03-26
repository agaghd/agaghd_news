package io.agaghd.agaghdnews.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.agaghd.agaghdnews.CustomProgressDialog
import io.agaghd.agaghdnews.R

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mContext: Context
    private lateinit var mProgressDialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        Log.i("wtf", this.javaClass.name + " onCreate")
        Log.i("wtf", this.javaClass.name + " taskId: " + taskId)
        mProgressDialog = CustomProgressDialog(mContext, R.style.CustomProgressDialogStyle)
    }

    override fun onStart() {
        Log.i("wtf", this.javaClass.name + " onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.i("wtf", this.javaClass.name + " onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.i("wtf", this.javaClass.name + " onPause")

        super.onPause()
    }

    override fun onStop() {
        Log.i("wtf", this.javaClass.name + " onStop")

        super.onStop()
    }

    override fun onDestroy() {
        Log.i("wtf", this.javaClass.name + " onDestroy")

        super.onDestroy()
    }

    override fun onRestart() {
        Log.i("wtf", this.javaClass.name + " onRestart")

        super.onRestart()
    }

    fun showProgressDialog() {
        mProgressDialog.show()
    }

    fun closeProgressDialog() {
        mProgressDialog.dismiss()
    }
}