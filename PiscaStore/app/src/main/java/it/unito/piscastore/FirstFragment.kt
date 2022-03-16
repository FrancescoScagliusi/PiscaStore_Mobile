package it.unito.piscastore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


import it.unito.piscastore.R
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    val BASEURL: String = "http://10.0.2.2:8080/catalog/api/v1/"


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //khttp.get("http://httpbin.org/get")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CatalogService::class.java)

        val call = service.getCatalog()


        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.code() == 200) {
                    val productResponse = response.body()!!

                    println("RESPONSE: "+productResponse.toString())
                    txtFirst.text = productResponse.toString()

                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println("ERROR: "+t.message.toString())

                txtFirst.text = t.message
            }
        })

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}