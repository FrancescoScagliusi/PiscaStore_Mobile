package it.unito.piscastore.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import it.unito.piscastore.MainActivity
import it.unito.piscastore.R
import it.unito.piscastore.controller.UserService
import it.unito.piscastore.model.CurrentUser
import it.unito.piscastore.model.SigninUser
import it.unito.piscastore.view.activity.LandingActivity
import it.unito.piscastore.view.activity.RegisterActivity
import kotlinx.android.synthetic.main.fragment_catalog_list.*
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

    private fun showProgress(b: Boolean){
        if(b) {
            formPaneLogin.visibility = View.GONE
            progressPaneLogin.visibility = View.VISIBLE
        }
        else{
            formPaneLogin.visibility = View.VISIBLE
            progressPaneLogin.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress(false)

        btnLogin.setOnClickListener{

            showProgress(true)
            val retrofit = Retrofit.Builder()
                    .baseUrl(resources.getString(R.string.url_user_local))
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

                    showProgress(true)
                    Toast.makeText(context,"Login effettuato!",Toast.LENGTH_SHORT).show()

                    var intent = Intent(getContext(), MainActivity::class.java)
                    intent.putExtra("token", u.accessToken)
                    startActivity(intent)

                }

                override fun onFailure(call: Call<CurrentUser?>, t: Throwable) {
                    println("ERROR: "+t.message.toString())

                    showProgress(true)
                    Toast.makeText(context,"Errore Login! Controlla le info",Toast.LENGTH_SHORT).show()

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