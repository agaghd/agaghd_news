package io.agaghd.agaghdnews.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class NewsPagerAdapter(fm: FragmentManager, list: MutableList<Fragment>) : FragmentPagerAdapter(fm) {

    val mFm = fm
    val mList = list

    override fun getItem(p0: Int): Fragment {
        return mList[p0]
    }

    override fun getCount(): Int {
        return mList.size
    }

}