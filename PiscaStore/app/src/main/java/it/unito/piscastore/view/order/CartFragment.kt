package it.unito.piscastore.view.order

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.adapter.RvAdapterCart
import it.unito.piscastore.model.Product
import it.unito.piscastore.view.catalog.OnProductClickListener
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_catalog_list.*
import kotlinx.android.synthetic.main.product_item_cart.*
import java.lang.reflect.Type

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment(), OnProductClickListener {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: RvAdapterCart

    private var items : ArrayList<Product> = ArrayList()
    private var total: Float = 0f

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
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).displayBack(false)
        showEmpty(false)

        recyclerCart.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        txtTotal.text = "€ " + this.total

        getItems()

    }


    private fun populateList(){
     adapter = RvAdapterCart(this,this.items)
        if(this.items.size > 0){
            if(recyclerCart != null){
                recyclerCart.adapter = adapter
                adapter.notifyDataSetChanged()
                setTotal()
            }
        }
        else{
            //txtNoProduct.visibility = View.VISIBLE
            showEmpty(true)
        }
    }

    private fun showEmpty(b: Boolean){
        if(b) {
            emptyPaneCart.visibility = View.VISIBLE
            centerPaneCart.visibility = View.GONE
        }
        else{
            centerPaneCart.visibility = View.VISIBLE
            emptyPaneCart.visibility = View.GONE
        }
    }

    private fun getItems(){
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("cart",
            Context.MODE_PRIVATE
        )

        val gson = Gson()
        val json = sharedPreferences.getString("items", null)

        val type: Type = object : TypeToken<ArrayList<Product>>() {}.type

        val list = gson.fromJson<Any>(json, type)
        this.items = list as ArrayList<Product>

        println("CART LIST " + this.items.size)

        populateList()
    }

    private fun updateCart(){
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("cart",
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val editor = sharedPreferences.edit()
        editor.putString("items",gson.toJson(this.items))
        editor.apply()

        populateList()
    }


    private fun setTotal(){
        println("CART LIST TOT: " + this.items.size)

        this.total = 0f
        for(i in this.items){
            this.total += i.price
        }
        txtTotal.text = "€ " + this.total
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onUpdate(position: Int, model: Product) {
        TODO("Not yet implemented")
    }

    override fun onDelete(product: Product) {
        adapter.removeProduct(product)
        this.items.remove(product)

        updateCart()
    }
}