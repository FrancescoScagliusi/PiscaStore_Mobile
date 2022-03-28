package it.unito.piscastore.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.AccountService
import it.unito.piscastore.model.CurrentInfo
import it.unito.piscastore.model.CurrentUser
import it.unito.piscastore.view.activity.LandingActivity
import it.unito.piscastore.view.order.OrderFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment: Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = this.getToken()
        println(token)
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Auth", "Bearer $token")
                    .build()
            chain.proceed(newRequest)
        }.build()

        val retrofit: Retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(resources.getString(R.string.url_account))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(AccountService::class.java)
        val call = service.getCurrentUser(token)
        println("Logging")
        call?.enqueue(object : Callback<CurrentInfo?> {
            override fun onResponse(call: Call<CurrentInfo?>, response: Response<CurrentInfo?>) {
                println(response)
                println(response.code())

                    //val u = response.body()!!
                    //println("Logged: " + u)

            }
            override fun onFailure(call: Call<CurrentInfo?>, t: Throwable) {
                println("ERROR: "+t.message.toString())
            }
        })


        btnLogout.setOnClickListener {
            this.deleteToken()
            val intent = Intent(context, LandingActivity::class.java)
            startActivity(intent)
        }

        txtOrdini.setOnClickListener {
            (activity as MainActivity).setCurrentFragment(OrderFragment())
        }
    }

    fun getToken(): String{
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("tokenStorage", Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("token","")
        return sharedIdValue.toString()
    }

    fun deleteToken(){
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("tokenStorage", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("token", "")
        editor.apply()
        editor.commit()
    }

}