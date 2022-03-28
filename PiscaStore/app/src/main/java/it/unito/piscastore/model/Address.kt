package it.unito.piscastore.model

import java.util.*

data class Address(
    var id : Long,
    var street: String,
    var city: String,
    var country: String,
    var zipCode: String)
