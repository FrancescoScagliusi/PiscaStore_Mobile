package it.unito.piscastore.view.catalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.controller.adapter.RvAdapterMain
import it.unito.piscastore.model.Product
import kotlinx.android.synthetic.main.fragment_catalog_list.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OtherFragment : Fragment(), CellClickListener {

    val BASEURL: String = "http://192.168.1.20:8080/catalog/api/v1/"

    val CATEGORY: Long = 3

    private lateinit var adapter: RvAdapterMain

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        recyclerView1.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun getData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CatalogService::class.java)

        val call = service.getCatalogByCategory(CATEGORY)


        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.code() == 200) {
                    val productResponse = response.body()!!
                    println("P: " + productResponse)
                    populateList(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("ERROR: " + t.message.toString())

                //txtFirst.text = t.message
            }
        })
    }

    private fun populateList(list: List<Product>) {
        adapter = RvAdapterMain(this, list)

        if (list.size > 0 && recyclerView1 != null){
            recyclerView1.adapter = adapter
            adapter.notifyDataSetChanged()

            //progressBar.setVisibility(View.GONE)
            recyclerView1.setVisibility(View.VISIBLE)

            println("LIST: " + list.size)
        } else {
            recyclerView1.visibility = View.GONE
            txtNoProduct.visibility = View.VISIBLE
        }
    }


    override fun onCellClickListener(id: Long) {
        Toast.makeText(context, "Clicked: " + id, Toast.LENGTH_SHORT).show()
    }
}