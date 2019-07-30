package io.agaghd.agaghdnews.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.agaghd.agaghdnews.CustomProgressDialog
import io.agaghd.agaghdnews.R

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mContext: Context
    private lateinit var mProgressDialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mProgressDialog = CustomProgressDialog(mContext, R.style.CustomProgressDialogStyle)
    }

    fun showProgressDialog() {
        mProgressDialog.show()
    }

    fun closeProgressDialog() {
        mProgressDialog.dismiss()
    }
}