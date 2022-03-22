package it.unito.piscastore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

import it.unito.piscastore.controller.adapter.MyAdapter
import it.unito.piscastore.view.activity.LandingActivity
import it.unito.piscastore.view.catalog.HomeFragment
import it.unito.piscastore.view.order.CartFragment


class MainActivity : AppCompatActivity() {

    val BASEURL: String = "http://192.168.1.20:8080/catalog/api/v1/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("token")
        if(token != null){
            this.saveUser(token)
        }
        else if (this.getUser() == ""){
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

        //setSupportActionBar(findViewById(R.id.toolbar))
        setContentView(R.layout.activity_main)
        title = "PiscaStore"


        buttonBack.visibility = View.GONE


        val firstFragment=HomeFragment()
        val secondFragment=CartFragment()
        //val thirdFragment=ThirdFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.cart->setCurrentFragment(secondFragment)
                //R.id.settings->//setCurrentFragment(thirdFragment)

            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    public fun displayBack(b: Boolean){

        buttonBack.setOnClickListener {
           supportFragmentManager.popBackStack()
        }

        if(b) buttonBack.visibility = View.VISIBLE
        else buttonBack.visibility = View.GONE
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    fun saveUser(accessToken: String){
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("tokenStorage", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("token", accessToken)
        editor.apply()
        editor.commit()
    }

    fun getUser(): String{
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("tokenStorage", Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("token","")
        return sharedIdValue.toString()
    }

}

interface CellClickListener {
    fun onCellClickListener(id: Long)
}