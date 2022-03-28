package it.unito.piscastore.model

import java.util.*

data class OrderRv(
    var creation: Date,
    var id : Long,
    var address: Address?,
    var items: Array<OrderItem>
)
