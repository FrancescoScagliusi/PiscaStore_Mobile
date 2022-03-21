package it.unito.piscastore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.parse.ParseObject
import it.unito.piscastore.view.activity.LandingActivity


class MainActivity : AppCompatActivity() {

    val BASEURL: String = "http://192.168.1.155:8080/catalog/api/v1/"
    val userStorage: String = "userStorage.data"


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("token")
        if(token != null){
            this.saveUser(token)
            setContentView(R.layout.activity_main)
        }
        else if (this.getUser() != ""){
            setContentView(R.layout.activity_main)
            }
            else{
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
            }
        //setSupportActionBar(findViewById(R.id.toolbar))

        /*title = "PiscaStore"


        tabLayout.addTab(tabLayout.newTab().setText("Tutti"))
        tabLayout.addTab(tabLayout.newTab().setText("Vasi"))
        tabLayout.addTab(tabLayout.newTab().setText("Dipinti"))
        tabLayout.addTab(tabLayout.newTab().setText("Altro"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MyAdapter(this, supportFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })*/

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

    fun saveObjectToSharedPreference(context: Context, serializedObjectKey: String?, `object`: Any?) {
        val sharedPreferences = context.getSharedPreferences(userStorage, 0)
        val sharedPreferencesEditor = sharedPreferences.edit()
        val gson = Gson()
        val serializedObject = gson.toJson(`object`)
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject)
        sharedPreferencesEditor.apply()
    }

    fun <GenericClass> getSavedObjectFromPreference(context: Context, preferenceKey: String?, classType: Class<GenericClass>?): GenericClass? {
        val sharedPreferences = context.getSharedPreferences(userStorage, 0)
        if (sharedPreferences.contains(preferenceKey)) {
            val gson = Gson()
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType)
        }
        return null
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