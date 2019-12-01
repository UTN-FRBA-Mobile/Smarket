package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Purchase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object PurchaseService {

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("purchases")
    }

    fun getProducts(uidPurchase: String, action : ()->Unit): List<Purchase> {
        val products = ArrayList<Purchase>()
        getInstance().whereEqualTo("uid", uidPurchase).get()
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    response.result!!.filterNotNull()
                        .forEach {
                            products.addAll(transform(it))
                        }
                    action()
                } else
                    throw RuntimeException("Error al conectarse con la base de datos")
            }
        return products
    }

    private fun transform(query: QueryDocumentSnapshot): List<Purchase> {
        val data = query.data
        @Suppress("UNCHECKED_CAST") 
        val products = data["purchases"] as List<HashMap<String, Any>>
        return products.map {
            Purchase(
                it["barcode"] as String,
                it["amount"] as Long,
                it["description"] as String,
                it["price"] as Double,
                it["image"] as String
            )
        }
    }

    private fun transform(purchase: Purchase): HashMap<String, Any> {
        val query = HashMap<String, Any>()
        query["barcode"] = purchase.barCode!!
        query["amount"] = purchase.amount
        query["price"] = purchase.price
        query["description"] = purchase.description
        query["image"] = purchase.image
        return query
    }

    fun save(purchases: List<Purchase>, uid: String) {
        val query = HashMap<String, Any>()
        query["uid"] = uid
        query["purchases"] = purchases.map { transform(it) }
        getInstance().add(query)
    }
}