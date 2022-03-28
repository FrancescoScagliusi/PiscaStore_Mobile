package it.unito.piscastore.model

import java.util.*

data class Order(
    var creation: Date,
    var id : Long,
    var id_user: Long,
    var id_address: Long,
    var items: Array<OrderItem>
)
