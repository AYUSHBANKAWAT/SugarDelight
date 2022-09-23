package com.application.sugarrush.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PagerAdapter(
    private val fm:FragmentActivity,
    private val resultBundle:Bundle,
    private val tabLayout: TabLayout?,
    private val viewPager2: ViewPager2?
) :FragmentStateAdapter(fm) {
    private val fragmentList:ArrayList<Fragment> = arrayListOf()
    private val fragmentTitleList:ArrayList<String> = arrayListOf()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        fragmentList[position].arguments = resultBundle
        return fragmentList[position]
    }

    fun addFragment(fragment:Fragment,title:String?="Title"){
        fragmentList.add(fragment)
        fragmentTitleList.add(title.toString())
    }

    private fun setTabLayoutMediator(){
        TabLayoutMediator(tabLayout!!, viewPager2!!) {
                tab, position ->
            tab.text = this.fragmentTitleList[position]
        }.attach()
    }


    fun attach(){
        viewPager2?.adapter = this
        tabLayout?.let {
            viewPager2?.let {
                setTabLayoutMediator()
            }
        }
    }

}