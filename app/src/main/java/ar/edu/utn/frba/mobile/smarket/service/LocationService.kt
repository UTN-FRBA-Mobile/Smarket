package ar.edu.utn.frba.mobile.smarket.service

import ar.edu.utn.frba.mobile.smarket.model.Location
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*
import kotlin.collections.HashMap

object LocationService {

    fun getLocations(action: () -> Unit): ArrayList<Location> {
        val locations = ArrayList<Location>()
        getInstance().get()
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    response.result!!.filterNotNull()
                        .forEach { locations.add(transform(it)) }
                    action()
                } else
                    throw RuntimeException("Error al conectarse con la base de datos")
            }
        return locations
    }

    private fun transform(query: QueryDocumentSnapshot): Location {
        val latLng = query.data["latLng"] as HashMap<String, Object>
        return Location(query.data["description"] as String, LatLng(latLng["latitude"] as Double, latLng["longitude"] as Double))
    }

    fun save(location: Location) {
        val query = HashMap<String, Any>()
        query["description"] = location.address
        query["latLng"] = location.latLng
        getInstance().add(query)
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("locations")
    }
}