package it.unito.piscastore.view.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.controller.OrderService
import it.unito.piscastore.controller.adapter.RvAdapterDetailOrder
import it.unito.piscastore.model.Order
import it.unito.piscastore.model.OrderItem
import it.unito.piscastore.model.Product
import it.unito.piscastore.model.ProductAuthor
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"


class OrderDetailFragment : Fragment(), CellClickListener {

    private var id: Long = 0
    private lateinit var order: Order

    private var items: ArrayList<Product> = ArrayList<Product>()

    private lateinit var adapter: RvAdapterDetailOrder

    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(ARG_PARAM1)
        }

        (activity as MainActivity).displayBack(true)
        (activity as MainActivity).showTitle("Riepilogo Ordine")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvOrderDetail.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        showProgress(true)


        println("Order id: " + this.id)

        getOrderDetails(this.id)

    }

    private fun getProduct(id_product: Long): Product? {
        val url = resources.getString(R.string.url_catalog)


        val client = OkHttpClient.Builder().build()

        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatalogService::class.java)

        val result = runBlocking {
            api.getProductById2(id_product)
        }

        if(result.isSuccessful && result.body()!=null) return result.body()?.product!!
        else return null
        //println("Product: " + result)
    }


    private fun getOrderDetails(id: Long) {
        val url = resources.getString(R.string.url_order)

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OrderService::class.java)

        val call = service.getOrder(id)

        call.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                val order = response.body()!!
                println("Order detail: " + order)

                setView(order)
                showProgress(false)
                //if (productResponse.product.id > 0) setView(productResponse)

            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                println("ERROR: " + t.message.toString())
                //Toast.makeText(this,"ERROR: " + t.message.toString(), Toast.LENGTH_SHORT).show()
                //txtFirst.text = t.message
                showProgress(false)
            }
        })
    }

    private fun showProgress(b: Boolean) {
        if (b) {
            contentPaneDetailOrder.visibility = View.GONE
            progressPaneDetailOrder.visibility = View.VISIBLE
        } else {
            contentPaneDetailOrder.visibility = View.VISIBLE
            progressPaneDetailOrder.visibility = View.GONE
        }
    }


    private fun setView(order: Order) {
        populateList(order.items.toList(), this)

        txtOrderIdDetailOrder.text = order.id.toString()
        txtAddressDetailOrder.text = order.id_address.toString()

        val total: Float = getTotal(order.items)
        txtTotalDetailOrder.text = "€ " + total

    }

    private fun populateList(list: List<OrderItem>, cellClickListener: CellClickListener) {

        for (i in list) {
            val p = getProduct(i.id_product)

            if(p!=null) this.items.add(p)

            println("ID: " + i.id_product)
        }
        println("LIST ITEMS: " + items.size)

        adapter = RvAdapterDetailOrder(cellClickListener, items)

        if (list.size > 0) {
            if (rvOrderDetail != null) {
                rvOrderDetail.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        } else {
            println("LIST: is empty")
            //txtNoProduct.visibility = View.VISIBLE
        }
    }

    /*private fun getProductDetails(id: Long){
        val url = resources.getString(R.string.url_catalog_local)


        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CatalogService::class.java)

        val call = service.getProductById(id)

        call.enqueue(object : Callback<ProductAuthor> {
            override fun onResponse(
                call: Call<ProductAuthor>,
                response: Response<ProductAuthor>
            ) {
                if (response.code() == 200) {
                    val productResponse = response.body()!!
                    println("ProductAuthor: " + productResponse)
                    if (productResponse.product.id > 0) setView(productResponse)
                }
            }

            override fun onFailure(call: Call<ProductAuthor>, t: Throwable) {
                println("ERROR: " + t.message.toString())
                //Toast.makeText(this,"ERROR: " + t.message.toString(), Toast.LENGTH_SHORT).show()
                //txtFirst.text = t.message
            }
        })

        //return product?.product;
    }*/


    private fun getTotal(items: Array<OrderItem>): Float {
        var total = 0f
        for (i in items) {
            total += i.price
        }
        return total
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long) =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, id)
                }
            }
    }

    override fun onCellClickListener(id: Long) {
    }
}