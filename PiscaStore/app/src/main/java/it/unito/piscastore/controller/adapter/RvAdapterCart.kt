package it.unito.piscastore.controller.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import it.unito.piscastore.R
import it.unito.piscastore.model.Product
import it.unito.piscastore.view.catalog.OnProductClickListener


class RvAdapterCart(private val onProductListener: OnProductClickListener, private var dataList: ArrayList<Product>) : RecyclerView.Adapter<RvAdapterCart.ViewHolder>() {

    
    private lateinit var context: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.product_item_cart, p0, false)
        context = p0.context
        return ViewHolder(v);
    }

    override fun getItemCount():Int{

        Log.d("SIZE", "Size: " + dataList.size)
        return dataList.size


    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val data = dataList.get(p1)

        println("CART ADAPTER: " + data)

        p0.name?.text = data.name.capitalize()
        p0.dimensions?.text = data.dimensions.capitalize()
        p0.price?.text = "€ " +data.price.toString()

        p0.btnRemove?.setOnClickListener {
            val position = p0.adapterPosition
            val product = dataList[position]
            onProductListener.onDelete(product)
        }

        Picasso.get().load("http://192.168.1.20:8080/catalog/api/v1/image/" +  data.image).into(p0.image)
    }



    fun removeProduct(model: Product) {
        val position = dataList.indexOf(model)
        dataList.remove(model)
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.txtNameProductCart)
        val dimensions = itemView.findViewById<TextView>(R.id.txtDimensionsCart)
        val price = itemView.findViewById<TextView>(R.id.txtPriceCart)
        val image = itemView.findViewById<ImageView>(R.id.imageProduct)
        val btnRemove = itemView.findViewById<ImageButton>(R.id.btnRemoveCart)

    }


}