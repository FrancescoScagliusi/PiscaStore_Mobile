package it.unito.piscastore.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.UserService
import it.unito.piscastore.model.CurrentUser
import it.unito.piscastore.model.SigninUser
import it.unito.piscastore.view.activity.LandingActivity
import it.unito.piscastore.view.activity.RegisterActivity
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener{

            val retrofit = Retrofit.Builder()
                    .baseUrl(resources.getString(R.string.url_user))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val service = retrofit.create(UserService::class.java)
            val user = SigninUser(txtUsername.text.toString(), txtPassword.text.toString())
            val call = service.login(user)
            println("Logging")
            call?.enqueue(object : Callback<CurrentUser?> {
                override fun onResponse(call: Call<CurrentUser?>, response: Response<CurrentUser?>) {
                    val u = response.body()!!
                    println("Logged: " + u)
                    var intent = Intent(getContext(), MainActivity::class.java)
                    intent.putExtra("token", u.accessToken)
                    startActivity(intent)

                }

                override fun onFailure(call: Call<CurrentUser?>, t: Throwable) {
                    println("ERROR: "+t.message.toString())
                }
            })
        }

        btnRegister.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(context, LandingActivity::class.java)
            startActivity(intent)
        }
    }
}