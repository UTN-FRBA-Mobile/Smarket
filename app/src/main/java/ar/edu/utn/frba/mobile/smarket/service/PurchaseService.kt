package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

object PurchaseService {

    fun getHistory(action: () -> Unit): ArrayList<Purchase> {
        val history = ArrayList<Purchase>()
        getInstance().get()
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    response.result!!.filterNotNull()
                        .forEach { history.add(transform(it)) }
                    action()
                } else
                    throw RuntimeException("Error al conectarse con la base de datos")
            }
        return history
    }

    private fun transform(query: QueryDocumentSnapshot): Purchase {
        val data = query.data
        val purchase = Purchase(query.id,
                        Date(data["date"] as Long),
                        data["price"] as Number,
                        data["amount"] as Number,
                        null,
                        PurchaseStatus.valueOf(data["status"] as String))
        if (data["rating"] != null)
            purchase.rating = (data["rating"] as Double).toFloat()
        return purchase
    }

    fun updatePurchase(purchase: Purchase) {
        val query = HashMap<String, Any>()
        query["status"] = purchase.status.toString()
        query["rating"] = purchase.rating!!

        getInstance().document(purchase.uid)
            .update(query)
            .addOnFailureListener(OnFailureListener { throw RuntimeException("Error al conectarse con la base de datos") })
    }

    fun savePurchase(purchase: Purchase) {
        val query = HashMap<String, Any>()
        query["date"] = purchase.date.time
        query["price"] = purchase.price
        query["amount"] = purchase.amount
        query["status"] = purchase.status.toString()

        getInstance().add(query)
            .addOnSuccessListener(OnSuccessListener<Any> {
                ProductService.save(purchase.products!!, (it as DocumentReference).id)
            })
            .addOnFailureListener(OnFailureListener { throw RuntimeException("Error al conectarse con la base de datos") })
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("history")
    }
}