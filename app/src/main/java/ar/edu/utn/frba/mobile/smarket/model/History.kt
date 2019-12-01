package ar.edu.utn.frba.mobile.smarket.model

import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import java.util.*
import kotlin.collections.ArrayList

class History(
    var uid: String,
    var date: Date,
    var price: Number,
    var amount: Number,
    var purchases: ArrayList<Purchase>?,
    var status: PurchaseStatus
) {
    var rating: Float? = null
}
