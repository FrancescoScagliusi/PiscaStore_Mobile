package it.unito.piscastore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


import it.unito.piscastore.R
import it.unito.piscastore.controller.adapter.RvAdapterMain
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(),CellClickListener {

    val BASEURL_old: String = "http://10.0.2.2:8080/catalog/api/v1/"

    val BASEURL: String = "http://192.168.1.20:8080/catalog/api/v1/"

    val CATEGORY: Long = 1

    private lateinit var adapter: RvAdapterMain

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getData(){
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
                println("ERROR: "+t.message.toString())

                //txtFirst.text = t.message
            }
        })
    }
    private fun populateList(list: List<Product>){
        adapter = RvAdapterMain(this, list)
        recyclerView1.adapter = adapter
        adapter.notifyDataSetChanged()

        //progressBar.setVisibility(View.GONE)
        recyclerView1.setVisibility(View.VISIBLE)
    }

    override fun onCellClickListener(id: Long) {
        Toast.makeText(context,"Clicked: "+ id, Toast.LENGTH_SHORT).show()
    }
}