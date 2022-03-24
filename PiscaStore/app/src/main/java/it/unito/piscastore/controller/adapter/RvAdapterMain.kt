package it.unito.piscastore.controller.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.R
import it.unito.piscastore.model.Product


class RvAdapterMain(private val cellClickListener: CellClickListener, private val dataList: List<Product>) : RecyclerView.Adapter<RvAdapterMain.ViewHolder>() {

    
    private lateinit var context: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.product_item, p0, false)
        context = p0.context
        return ViewHolder(v);
    }

    override fun getItemCount():Int{
        return dataList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val data = dataList.get(p1)

        p0.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data.id)
        }

        p0.name?.text = data.name.capitalize()
        p0.dimensions?.text = data.dimensions.capitalize()
        p0.price?.text = "€ " + data.price.toString()

        Picasso.get().load(context.resources.getString(R.string.url_image_local) +  data.image).into(p0.image)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.txtNameProduct)
        val dimensions = itemView.findViewById<TextView>(R.id.txtDimensions)
        val price = itemView.findViewById<TextView>(R.id.txtPrice)
        val image = itemView.findViewById<ImageView>(R.id.imageProduct)

    }


}