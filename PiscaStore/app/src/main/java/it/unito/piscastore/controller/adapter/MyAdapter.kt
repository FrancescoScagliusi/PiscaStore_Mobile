package it.unito.piscastore.controller.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import it.unito.piscastore.view.catalog.AllFragment
import it.unito.piscastore.view.catalog.VasiFragment
import it.unito.piscastore.view.catalog.DipintiFragment
import it.unito.piscastore.view.catalog.OtherFragment

internal class MyAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllFragment()
            }
            1 -> {
                VasiFragment()
            }
            2 -> {
                DipintiFragment()
            }
            3 -> {
                OtherFragment()
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