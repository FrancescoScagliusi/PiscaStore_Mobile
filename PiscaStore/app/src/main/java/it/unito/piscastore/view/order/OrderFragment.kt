package it.unito.piscastore.view.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.controller.OrderService
import it.unito.piscastore.controller.adapter.RvAdapterMain
import it.unito.piscastore.controller.adapter.RvAdapterOrder
import it.unito.piscastore.model.Order
import it.unito.piscastore.model.Product
import it.unito.piscastore.model.ProductAuthor
import kotlinx.android.synthetic.main.fragment_catalog_list.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class OrderFragment : Fragment(),CellClickListener {

    private var param1: String? = null
    private var param2: String? = null


    private lateinit var adapter: RvAdapterOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewOrder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        showProgress(true)

        (activity as MainActivity).displayBack(false)
        (activity as MainActivity).showTitle("I miei Ordini")



        getOrder(1)
    }

    private fun showProgress(b: Boolean){
        if(b) {
            contentPaneOrder.visibility = View.GONE
            progressPaneOrder.visibility = View.VISIBLE
        }
        else{
            contentPaneOrder.visibility = View.VISIBLE
            progressPaneOrder.visibility = View.GONE
        }
    }

    private fun getOrder(id_user: Long) {
        val url = resources.getString(R.string.url_order)


        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OrderService::class.java)

        val call = service.getOrderOfUser(id_user)

        call.enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.code() == 200) {
                    val orders = response.body()!!
                    println("ORDERS: " + orders)
                    populateList(orders)
                    //if (productResponse.product.id > 0) setView(productResponse)
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                println("ERROR: " + t.message.toString())
                //Toast.makeText(this,"ERROR: " + t.message.toString(), Toast.LENGTH_SHORT).show()
                //txtFirst.text = t.message
            }
        })
    }

    private fun populateList(list: List<Order>){
        adapter = RvAdapterOrder(this, list)
        showProgress(false)

        if(list.size>0){
            if (recyclerViewOrder != null){
                recyclerViewOrder.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            println("LIST: " + list.size)
        }
        else{
            println("LIST: is empty")
            //txtNoProduct.visibility = View.VISIBLE
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCellClickListener(id: Long) {
        Toast.makeText(context,"CLICK: " + id.toString(), Toast.LENGTH_SHORT).show()

        (activity as MainActivity).setCurrentFragment(OrderDetailFragment.newInstance(id))

    }
}