package it.unito.piscastore.model

import android.content.Context
import com.parse.Parse.getApplicationContext
import java.io.*


data class CurrentUser (

    var id: Long,
    var name: String,
    var surname: String,
    var username: String,
    var email: String,
    var roles: Array<Any>,
    var accessToken: String,
    var tokenType: String
)


