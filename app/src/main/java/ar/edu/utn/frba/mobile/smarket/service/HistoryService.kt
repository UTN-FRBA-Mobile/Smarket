package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import ar.edu.utn.frba.mobile.smarket.model.History
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

object HistoryService {

    fun getHistory(action: () -> Unit): ArrayList<History> {
        val history = ArrayList<History>()
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

    private fun transform(query: QueryDocumentSnapshot): History {
        val data = query.data
        val purchase = History(query.id,
                        Date(data["date"] as Long),
                        data["price"] as Number,
                        data["amount"] as Number,
                        null,
                        PurchaseStatus.valueOf(data["status"] as String))
        if (data["rating"] != null)
            purchase.rating = (data["rating"] as Double).toFloat()
        return purchase
    }

    fun updatePurchase(history: History) {
        val query = HashMap<String, Any>()
        query["status"] = history.status.toString()
        query["rating"] = history.rating!!

        getInstance().document(history.uid)
            .update(query)
            .addOnFailureListener { throw RuntimeException("Error al conectarse con la base de datos") }
    }

    fun savePurchase(history: History) {
        val query = HashMap<String, Any>()
        query["date"] = history.date.time
        query["price"] = history.price
        query["amount"] = history.amount
        query["status"] = history.status.toString()

        getInstance().add(query)
            .addOnSuccessListener {
                PurchaseService.save(history.purchases!!, (it as DocumentReference).id)
            }
            .addOnFailureListener { throw RuntimeException("Error al conectarse con la base de datos") }
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("history")
    }
}