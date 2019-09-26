package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Purchase
import java.util.*

object PurchaseService {

    fun getHistory() : ArrayList<Purchase> {
        val history = ArrayList<Purchase>()
        history.add(Purchase(1, Date(), 250.0, 4))
        return history
    }

}