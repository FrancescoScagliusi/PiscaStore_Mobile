package it.unito.piscastore.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.UserService
import it.unito.piscastore.model.CurrentUser
import it.unito.piscastore.model.SignupUser
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            val retrofit = Retrofit.Builder()
                    .baseUrl(resources.getString(R.string.url_user_local))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val service = retrofit.create(UserService::class.java)
            val user = SignupUser(txtName.text.toString(),txtSurname.text.toString(),txtUsrname.text.toString(), txtEmail.text.toString(), txtPassword.text.toString(),"" )
            val call = service.register(user)
            println("Creating account")
            call?.enqueue(object : Callback<CurrentUser?> {
                override fun onResponse(call: Call<CurrentUser?>, response: Response<CurrentUser?>) {
                    val u = response.body()!!
                    println("Registered: " + u)
                }

                override fun onFailure(call: Call<CurrentUser?>, t: Throwable) {
                    println("ERROR: "+t.message.toString())
                }
            })
        }
    }

    fun Back(view: View){
        intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
    }

    fun Login(view: View){
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}