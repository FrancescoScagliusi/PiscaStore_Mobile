package it.unito.piscastore.view.catalog

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.model.Product
import it.unito.piscastore.model.ProductAuthor
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalog_list.*
import kotlinx.android.synthetic.main.fragment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class DetailFragment : Fragment() {
   
    private var id: Long? = null
    private var product: Product? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)

        id?.let { getProductDetails(it) }

        (activity as MainActivity).displayBack(true)

        btnAddToCart.setOnClickListener {
            addToCart()
        }
    }


    private fun showProgress(b: Boolean){
        if(b) {
            paneDetail.visibility = View.GONE
            progressBarDetail.visibility = View.VISIBLE
        }
        else{
            paneDetail.visibility = View.VISIBLE
            progressBarDetail.visibility = View.GONE
        }
    }

    private fun addToCart(){
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("tokenStorage", MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString("items", null)

        var items = ArrayList<Product>()
        if(json!=null) {
            val type: Type = object : TypeToken<ArrayList<Product>>() {}.type

            items = gson.fromJson<Any>(json, type) as ArrayList<Product>

            if (this.product != null) {
                if (!items.contains(this.product!!)) items.add(this.product!!)
                else Toast.makeText(
                    context,
                    "L'articolo è già presente nel carrello!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            items.add(this.product!!)
        }

        val editor = sharedPreferences.edit()

        editor.putString("items",gson.toJson(items))
        editor.apply()

        Toast.makeText(context,"Articolo aggiunto al carrello!",Toast.LENGTH_SHORT).show()
    }

    private fun getProductDetails(id: Long) {
        val url = resources.getString(R.string.url_catalog)


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
    }

    private fun setView(p: ProductAuthor) {

        this.product = p.product

        val url_image = resources.getString(R.string.url_image)
        detailTxtName.text = p.product.name

        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(
            SlideModel(
                url_image + p.product.image, ScaleTypes.CENTER_CROP
            )
        )
        if (!p.product.image2?.trim().isEmpty()) imageList.add(
            SlideModel(
                url_image + p.product.image2,
                ScaleTypes.CENTER_CROP
            )
        )
        if (!p.product.image3?.trim().isEmpty()) imageList.add(
            SlideModel(
                url_image + p.product.image3,
                ScaleTypes.CENTER_CROP
            )
        )
        if (!p.product.image4?.trim().isEmpty()) imageList.add(
            SlideModel(
                url_image + p.product.image4,
                ScaleTypes.CENTER_CROP
            )
        )

        image_product_slider.setImageList(imageList)

        detailTxtPrice.text = "€" + p.product.price
        detailTxtDescription.text = p.product.description
        detailTxtDimensions.text = p.product.dimensions

        showProgress(false)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, id)
                }
            }
    }
}

interface OnProductClickListener {

    /**
     * When the user clicks on each row this method will be invoked.
     */
    fun onUpdate(position: Int, model: Product)

    /**
     * when the user clicks on delete icon this method will be invoked to remove item at position.
     */
    fun onDelete(model: Product)

}