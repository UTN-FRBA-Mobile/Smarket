package ar.edu.utn.frba.mobile.smarket.service

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

object RatingService {

    fun qualify(uid: String, rating: Float) {
        val query = HashMap<String, Any>()
        query["purchaseId"] = uid
        query["rating"] = rating
        getInstance().add(query)
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("rating")
    }
}