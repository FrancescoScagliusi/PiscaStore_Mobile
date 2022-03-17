package it.unito.piscastore.view.catalog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import it.unito.piscastore.R
import kotlinx.android.synthetic.main.activity_detail_product.*


class DetailProductActivity : AppCompatActivity() {


    var id: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id: Long = intent.getLongExtra("id",0)



        println("ID_PRODUCT: " + id)

        if(id>0) this.id = id;
        else Toast.makeText(baseContext,"ERROR: Invalid product id",Toast.LENGTH_SHORT).show()

        setContentView(R.layout.activity_detail_product)

        buttonBackDetail.setOnClickListener {
            back()
        }
    }


    private fun back(){
        finish()
    }


}