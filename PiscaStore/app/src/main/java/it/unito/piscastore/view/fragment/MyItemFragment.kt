package it.unito.piscastore.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.controller.adapter.RvAdapterMain
import it.unito.piscastore.model.Product
import it.unito.piscastore.view.catalog.DetailFragment
import it.unito.piscastore.view.catalog.HomeFragment
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.recyclerCart
import kotlinx.android.synthetic.main.fragment_catalog_list.*
import kotlinx.android.synthetic.main.fragment_catalog_list.txtNoProduct
import kotlinx.android.synthetic.main.fragment_my_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val ARG_PARAM1 = "param1"

class MyItemFragment: Fragment(), CellClickListener {

    private lateinit var adapter: RvAdapterMain
    private var param1: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
        println(param1)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_item, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*if (arguments!=null){
            val id: Long? = arguments?.getLong(1)
            println("CATE: "+ id);
            if(id!=null) getMyItems(id)
        }*/
        recyclerCart.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        param1?.let { getMyItems(it) }

        (activity as MainActivity).displayBack(true)
        (activity as MainActivity).showTitle("I miei Articoli")
    }

    private fun populateList(list: List<Product>){
        adapter = RvAdapterMain(this, list)
        if(list.size>0){
            if (recyclerCart != null){
                recyclerCart.adapter = adapter
                adapter.notifyDataSetChanged()

                //progressBar.setVisibility(View.GONE)
                recyclerCart.setVisibility(View.VISIBLE)
            }
            println("LIST: " + list.size)
        }
        else{
            recyclerCart.visibility = View.GONE
            txtNoProduct.visibility = View.VISIBLE
        }
    }

    fun getMyItems(id: Long){
        val retrofit = Retrofit.Builder()
                .baseUrl(resources.getString(R.string.url_catalog))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(CatalogService::class.java)


        var call = service.getCatalog()

        if(id > 0) call = service.getProductsByAuthor(id)



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

    override fun onCellClickListener(id: Long) {
        Toast.makeText(context,"Clicked: "+ id, Toast.LENGTH_SHORT).show()

        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.flFragment, DetailFragment.newInstance(id))
                ?.addToBackStack(null)
                ?.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                ?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long) =
                MyItemFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PARAM1, param1)
                    }
                }
    }
}