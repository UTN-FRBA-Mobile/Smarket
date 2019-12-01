package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Product
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

object ProductService {

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("products")
    }

    fun getProduct(barcode: String, action : (_: Product)->Unit, error : () -> Unit) {
        getInstance().whereEqualTo("barcode", barcode).get()
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    val result = response.result!!.filterNotNull()
                    if (result.isEmpty())
                        error()
                    else
                        result.forEach {
                            action(transform(it))
                        }
                } else
                    throw RuntimeException("Error al conectarse con la base de datos")
            }
    }

    private fun transform(query: QueryDocumentSnapshot): Product {
        val data = query.data
        return Product(
            data["description"] as String,
            data["image"] as String,
            (data["price"] as Number).toDouble(),
            data["barcode"] as String
        )
    }
}