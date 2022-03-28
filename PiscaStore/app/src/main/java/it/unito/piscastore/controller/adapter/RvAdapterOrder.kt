package it.unito.piscastore.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.unito.piscastore.CellClickListener
import it.unito.piscastore.R
import it.unito.piscastore.controller.AccountService
import it.unito.piscastore.model.Address
import it.unito.piscastore.model.Order
import it.unito.piscastore.model.OrderItem
import it.unito.piscastore.model.OrderRv
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class RvAdapterOrder(private val cellClickListener: CellClickListener, private val dataList: List<OrderRv>) : RecyclerView.Adapter<RvAdapterOrder.ViewHolder>() {

    
    private lateinit var context: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.order_item, p0, false)
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

        val total = getTotal(data.items)

        val month_date = SimpleDateFormat("dd MMM yyyy", Locale.ITALY)
        val date = month_date.format(data.creation)


        DateFormat.getDateInstance(DateFormat.FULL, Locale.ITALY);


        p0.id?.text = data.id.toString()
        p0.nrItems?.text = data.items.size.toString()
        p0.price?.text = "€ " + total
        p0.date?.text = date
        p0.address.text = getAddress(data.address)

    }

    private fun getTotal(items: Array<OrderItem>): Float{
        var total = 0f
        for(i in items){
            total+=i.price
        }
        return total
    }

    private fun getAddress(address: Address?): String{
        if (address!=null) return "${address.street} ${address.city}, ${address.zipCode} ${address.country}"
        else return "Impossibile caricare l'indirizzo"
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val id = itemView.findViewById<TextView>(R.id.txtOrderId)
        val nrItems = itemView.findViewById<TextView>(R.id.txtNrItems)
        val price = itemView.findViewById<TextView>(R.id.txtPrice)
        val address = itemView.findViewById<TextView>(R.id.txtAddressOrder)
        val date = itemView.findViewById<TextView>(R.id.txtDateOrder)


    }


}