package it.unito.piscastore.view.catalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.adapter.MyAdapter
import it.unito.piscastore.controller.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var adapter: ViewPagerAdapter;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        adapter = ViewPagerAdapter(childFragmentManager)

        // add fragment to the list
        adapter.addFragment(AllFragment.newInstance(0), "Tutti")
        adapter.addFragment(AllFragment.newInstance(1), "Vasi")
        adapter.addFragment(AllFragment.newInstance(2), "Dipinti")
        adapter.addFragment(AllFragment.newInstance(4), "Arte")
        adapter.addFragment(AllFragment.newInstance(3), "Altro")




        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adding the Adapter to the ViewPager
        viewPager.adapter = adapter
        viewPager.isSaveEnabled = false

        // bind the viewPager with the TabLayout.
        tabLayout.setupWithViewPager(viewPager)
    }
}
