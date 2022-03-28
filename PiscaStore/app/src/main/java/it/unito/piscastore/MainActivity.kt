package it.unito.piscastore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.unito.piscastore.view.activity.LandingActivity
import it.unito.piscastore.view.catalog.HomeFragment
import it.unito.piscastore.view.fragment.ProfileFragment
import it.unito.piscastore.view.order.CartFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("token")
        val id = intent.getLongExtra("id", -1)
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

        bottomNavigationView.visibility = View.VISIBLE
        buttonBack.visibility = View.GONE


        val firstFragment=HomeFragment()
        val secondFragment=CartFragment()
        val thirdFragment=ProfileFragment()
        val b = Bundle()
        b.putString("token", this.getUser())
        thirdFragment.arguments = b;
        //val thirdFragment=ThirdFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.cart->setCurrentFragment(secondFragment)
                R.id.profile->setCurrentFragment(thirdFragment)
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
            this.setBottomNav(true)
           supportFragmentManager.popBackStack()
        }

        if(b) buttonBack.visibility = View.VISIBLE
        else buttonBack.visibility = View.GONE
    }

     fun setCurrentFragment(fragment: Fragment)=
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

    public fun setBottomNav(flag: Boolean){
        if(flag) bottomNavigationView.visibility = View.VISIBLE
        else bottomNavigationView.visibility = View.GONE
    }

}

interface CellClickListener {
    fun onCellClickListener(id: Long)
}