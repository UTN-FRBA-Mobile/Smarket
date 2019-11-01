package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Card
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

object UserService {

    fun authenticate(username: String, password: String) : Boolean {
        return username.toUpperCase() == "ADMIN" && password == "admin"
    }

    fun getCreditCards(action: (List<Card>) -> Unit) {
        getInstance().get()
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    val cardList = response.result!!.filterNotNull()
                        .map { transform(it) }
                    action(cardList)
                } else
                    throw RuntimeException("Error al conectarse con la base de datos")
            }
    }

    private fun transform(query: QueryDocumentSnapshot): Card {
        val data = query.data
        @Suppress("UNCHECKED_CAST")
        return Card(
                data["number"] as String,
                data["month"] as Int,
                data["year"] as Int,
                data["titular"] as String
            )
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("history")
    }

}