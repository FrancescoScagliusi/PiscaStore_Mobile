package it.unito.piscastore.model

data class OrderItem(
    var id: Long,
    var name: String,
    var price: Float,
    var id_product: Long
)
