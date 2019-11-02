package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Product
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object ProductService {

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("purchases")
    }

    fun getProducts(uidPurchase: String, action : ()->Unit): List<Product> {
        val products = ArrayList<Product>()
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

    private fun transform(query: QueryDocumentSnapshot): List<Product> {
        val data = query.data
        @Suppress("UNCHECKED_CAST") 
        val products = data["products"] as List<HashMap<String, Any>>
        return products.map {
            Product(
                UUID.randomUUID().toString(),
                it["amount"] as Long,
                it["description"] as String,
                it["price"] as Double
            )
        }
    }

    private fun transform(product: Product): HashMap<String, Any> {
        val query = HashMap<String, Any>()
        query["amount"] = product.amount
        query["price"] = product.price
        query["description"] = product.description
        return query
    }

    fun save(products: List<Product>, uid: String) {
        val query = HashMap<String, Any>()
        query["uid"] = uid
        query["products"] = products.map { transform(it) }
        getInstance().add(query)
    }
}