package it.unito.piscastore.model

data class Product(
    var id: Long,
    var title: String,
    var description: String,
    var dimensions: String,
    var price: Float,
    var image: String,
    var image2: String,
    var image3: String,
    var image4: String,
    var available: Boolean
)
