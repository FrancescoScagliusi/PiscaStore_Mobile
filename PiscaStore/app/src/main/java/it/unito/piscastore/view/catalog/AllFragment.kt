package it.unito.piscastore.view.catalog


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.controller.adapter.RvAdapterMain
import it.unito.piscastore.model.Product
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalog_list.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM_CATEGORY = "id_category"

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AllFragment : Fragment(), CellClickListener {

    val BASEURL_old: String = "http://10.0.2.2:8080/catalog/api/v1/"

    val BASEURL: String = "http://192.168.1.20:8080/catalog/api/v1/"


    private lateinit var adapter: RvAdapterMain

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as MainActivity).displayBack(false)

        if (arguments!=null){
            val id: Long? = arguments?.getLong(ARG_PARAM_CATEGORY)
            println("CATE: "+ id);
            if(id!=null) getData(id)
        }

        recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getData(id: Long){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CatalogService::class.java)


        var call = service.getCatalog()

        if(id > 0) call = service.getCatalogByCategory(id)



        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.code() == 200) {
                    val productResponse = response.body()!!
                    println("P: " + productResponse)
                    populateList(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("ERROR: "+t.message.toString())

                //txtFirst.text = t.message
            }
        })
    }


    private fun populateList(list: List<Product>){
        adapter = RvAdapterMain(this, list)
        if(list.size>0){
            if (recyclerView1 != null){
                recyclerView1.adapter = adapter
                adapter.notifyDataSetChanged()

                //progressBar.setVisibility(View.GONE)
                recyclerView1.setVisibility(View.VISIBLE)
            }
            println("LIST: " + list.size)
        }
        else{
            recyclerView1.visibility = View.GONE
            txtNoProduct.visibility = View.VISIBLE
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(category: Long) =
            AllFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM_CATEGORY, category)
                }
            }
    }


    override fun onCellClickListener(id: Long) {
        Toast.makeText(context,"Clicked: "+ id, Toast.LENGTH_SHORT).show()

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.flFragment,DetailFragment.newInstance(id))
            ?.addToBackStack(null)
            ?.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
            ?.commit()

        /*var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.allLayout, DetailFragment.newInstance(id))
        fr?.addToBackStack(null)
        fr?.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_pop_exit_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_exit_anim)
        fr?.commit()*/

    }
}