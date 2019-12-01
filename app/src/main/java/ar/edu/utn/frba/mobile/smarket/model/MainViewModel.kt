package ar.edu.utn.frba.mobile.smarket.model

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import java.util.ArrayList

class MainViewModel : ViewModel() {

    lateinit var user : FirebaseUser
    var totalPrice : Double = 0.0
    var product : Product? = null
    var purchase : Purchase? = null
    var purchases : ArrayList<Purchase>? = null
    var products : ArrayList<Product>? =  null
    var history : ArrayList<History>? = null
    var locations : ArrayList<Location>? = null


}