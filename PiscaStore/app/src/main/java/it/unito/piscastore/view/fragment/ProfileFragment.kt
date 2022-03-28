package it.unito.piscastore.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.AccountService
import it.unito.piscastore.model.CurrentInfo
import it.unito.piscastore.model.CurrentUser
import it.unito.piscastore.view.activity.LandingActivity
import it.unito.piscastore.view.order.OrderFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProfileFragment: Fragment() {

    private var id_user: Long = 0

    private lateinit var currentUser: CurrentInfo

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress(true)


        (activity as MainActivity).displayBack(false)
        (activity as MainActivity).showTitle("Il mio Profilo")

        val token = this.getToken()
        println(token)


        val id = getUserId(token)
        if(id!=null){
            this.id_user = id
        }

        val current = getUserDetail(token)
        if(current!=null){
            this.currentUser = current
            setView()
        }


        btnLogout.setOnClickListener {
            this.deleteToken()
            val intent = Intent(context, LandingActivity::class.java)
            startActivity(intent)
        }


        txtArticoli.setOnClickListener {
            if(this.id_user>0) (activity as MainActivity).setCurrentFragment(MyItemFragment.newInstance(id_user))
            else Toast.makeText(context,"Impossibila caricare gli articoli dell'utente loggato",Toast.LENGTH_SHORT).show()
        }
        txtOrdini.setOnClickListener {
            if(this.id_user>0) (activity as MainActivity).setCurrentFragment(OrderFragment.newInstance(id_user))
            else Toast.makeText(context,"Impossibila caricare gli ordini dell'utente loggato",Toast.LENGTH_SHORT).show()
        }

        showProgress(false)
    }

    private fun setView(){
        txtNome.text = currentUser.name  + " " +  currentUser.surname
        txtEmail.text = currentUser.email
    }

    private fun getUserDetail(token: String): CurrentInfo?{

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        //client.addInterceptor(logging)

        val api = Retrofit.Builder()
            .client(client.build())
            .baseUrl(resources.getString(R.string.url_account))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountService::class.java)



        val result = runBlocking {
            api.getCurrentUser(token)
        }

        if(result.isSuccessful && result.body()!=null) return result.body()!!
        else return null
    }


    private fun getUserId(token: String): Long?{
        val api: AccountService = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.url_account))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccountService::class.java)

        val result = runBlocking {
            api.getIdOfCurrentUser(token)
        }

        if(result.isSuccessful && result.body()!=null) return result.body()!!
        else return null
    }

    private fun showProgress(b: Boolean){
        if(b) {
            contentPaneProfile.visibility = View.GONE
            progressPaneProfile.visibility = View.VISIBLE
        }
        else{
            contentPaneProfile.visibility = View.VISIBLE
            progressPaneProfile.visibility = View.GONE
        }
    }

    fun getToken(): String{
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("tokenStorage", Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("token","")
        return "Bearer " + sharedIdValue.toString()
    }

    fun deleteToken(){
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("tokenStorage", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("token", "")
        editor.apply()
        editor.commit()
    }

}