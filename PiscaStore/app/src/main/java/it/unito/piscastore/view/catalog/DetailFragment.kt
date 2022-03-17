package it.unito.piscastore.view.catalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import it.unito.piscastore.R
import it.unito.piscastore.controller.CatalogService
import it.unito.piscastore.model.ProductAuthor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.android.synthetic.main.fragment_detail.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        var id = (activity as DetailProductActivity).id
        println("id from activity:" + id)
        getProductDetails(id)
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getProductDetails(id: Long){
        val url =  resources.getString(R.string.url_catalog)

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CatalogService::class.java)

        val call = service.getProductById(id)

        call.enqueue(object : Callback<ProductAuthor> {
            override fun onResponse(call: Call<ProductAuthor>, response: Response<ProductAuthor>) {
                if (response.code() == 200) {
                    val productResponse = response.body()!!
                    println("ProductAuthor: " + productResponse)
                    if(productResponse.product.id > 0) setView(productResponse)
                }
            }

            override fun onFailure(call: Call<ProductAuthor>, t: Throwable) {
                println("ERROR: "+t.message.toString())
                Toast.makeText(context,"ERROR: " + t.message.toString(), Toast.LENGTH_SHORT).show()
                //txtFirst.text = t.message
            }
        })
    }

    private fun setView(p: ProductAuthor){

        val url_image = resources.getString(R.string.url_image)

        detailTxtName.text = p.product.name

        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(
            SlideModel(url_image + p.product.image, ScaleTypes.CENTER_CROP
        )
        )
        if(!p.product.image2?.trim().isEmpty()) imageList.add(
            SlideModel(url_image + p.product.image2,
            ScaleTypes.CENTER_CROP
        )
        )
        if(!p.product.image3?.trim().isEmpty()) imageList.add(
            SlideModel(url_image + p.product.image3,
            ScaleTypes.CENTER_CROP
        )
        )
        if(!p.product.image4?.trim().isEmpty()) imageList.add(
            SlideModel(url_image + p.product.image4,
            ScaleTypes.CENTER_CROP
        )
        )

        image_product_slider.setImageList(imageList)

        detailTxtPrice.text = "€" + p.product.price
        detailTxtDescription.text = p.product.description
        detailTxtDimensions.text = p.product.dimensions
    }
}