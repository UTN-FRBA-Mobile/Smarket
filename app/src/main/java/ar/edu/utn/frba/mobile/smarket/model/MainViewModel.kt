package ar.edu.utn.frba.mobile.smarket.model

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import java.util.ArrayList

class MainViewModel : ViewModel() {

    lateinit var user : FirebaseUser
    var totalPrice : Double = 0.0
    var product : Product? = null
    var products : ArrayList<Product> =  ArrayList()
    var history : ArrayList<Purchase> = ArrayList()


}