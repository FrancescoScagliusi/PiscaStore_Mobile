package it.unito.piscastore.controller.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import it.unito.piscastore.view.catalog.AllFragment


internal class MyAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                return AllFragment.newInstance(0)
            }
            1 -> {
                return AllFragment.newInstance(1)
            }
            2 -> {
                return AllFragment.newInstance(2)
            }
            3 -> {
                return AllFragment.newInstance(3)
            }
            else -> {
                getItem(0)
            }
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}