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
import it.unito.piscastore.controller.AccountService
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.controller.OrderService
import it.unito.piscastore.controller.adapter.RvAdapterMain
import it.unito.piscastore.controller.adapter.RvAdapterOrder
import it.unito.piscastore.model.*
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val ARG_PARAM1 = "param1"


class OrderFragment : Fragment(),CellClickListener {

    private var id_user: Long? = null


    private lateinit var adapter: RvAdapterOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id_user = it.getLong(ARG_PARAM1)
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

        (activity as MainActivity).displayBack(true)
        (activity as MainActivity).showTitle("I miei Ordini")

        getOrder(this.id_user!!)
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

        var listOrder : ArrayList<OrderRv> = ArrayList()

        for(i in list){
            val address: Address? =  getAddress(i.id_address)
            listOrder.add(OrderRv(i.creation,i.id,address,i.items))
        }

        adapter = RvAdapterOrder(this, listOrder)
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
            txtNoProduct.visibility = View.VISIBLE
        }
    }

    private fun getAddress(id_address: Long): Address?{
        val url = resources.getString(R.string.url_account)

        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountService::class.java)

        val result = runBlocking {
            api.getAddressById(id_address)
        }

        if(result.isSuccessful && result.body()!=null) return result.body()!!
        else return null
    }



    companion object {
        @JvmStatic
        fun newInstance(id_user: Long) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, id_user)
                }
            }
    }

    override fun onCellClickListener(id: Long) {
        Toast.makeText(context,"CLICK: " + id.toString(), Toast.LENGTH_SHORT).show()

        (activity as MainActivity).setCurrentFragment(OrderDetailFragment.newInstance(id))

    }
}