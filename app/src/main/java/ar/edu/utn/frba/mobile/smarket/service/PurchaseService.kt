package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Purchase
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

object PurchaseService {

    fun getHistory() : ArrayList<Purchase> {
        val history = ArrayList<Purchase>()
        getInstance().get()
            .addOnCompleteListener { response ->
                if (response.isSuccessful)
                    response.result!!.filterNotNull()
                        .forEach {
                            history.add(transform(it))
                        }
                else
                    println("BASE DE DATOS ERROR READ")
            }
        return history
    }

    private fun transform(query: QueryDocumentSnapshot): Purchase {
        val data = query.data
        return Purchase(query.id,
                        Date(data["date"] as Long),
                        data["price"] as Number,
                        data["amount"] as Number)
    }

    fun savePurchase(purchase: Purchase) {
        val query = HashMap<String, Any>()
        query["date"] = purchase.date.time
        query["price"] = purchase.price
        query["amount"] = purchase.amount

        getInstance().add(query)
            .addOnSuccessListener(OnSuccessListener<Any> {
                println("BASE DE DATOS SUCCESS")
            })
            .addOnFailureListener(OnFailureListener { println("BASE DE DATOS ERROR") })
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("history")
    }
}