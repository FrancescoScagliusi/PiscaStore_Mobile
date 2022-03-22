package it.unito.piscastore.model


data class SigninUser (
    var username: String,
    var password: String
)

data class SignupUser(
        var name: String,
        var surname: String,
        var username: String,
        var email: String,
        var password: String,
        val phone: String
)

data class CurrentUser(
        var id: Long,
        var name: String,
        var surname: String,
        var username: String,
        var email: String,
        var roles: Array<Any>,
        var accessToken: String,
        var tokenType: String
)

