package ar.edu.utn.frba.mobile.smarket.model

import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import java.util.*

class Purchase(
    var uid: String,
    var date: Date,
    var price: Number,
    var amount: Number,
    var products: List<Product>?,
    var status: PurchaseStatus
)
