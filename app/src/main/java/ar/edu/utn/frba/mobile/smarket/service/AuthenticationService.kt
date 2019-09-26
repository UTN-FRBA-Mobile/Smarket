package ar.edu.utn.frba.mobile.smarket.service

object AuthenticationService {

    fun authenticate(username: String, password: String) : Boolean {
        return username.toUpperCase() == "ADMIN" && password == "admin"
    }

}