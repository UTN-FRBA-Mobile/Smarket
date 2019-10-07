package ar.edu.utn.frba.mobile.smarket.activities

interface Communication {

    fun put(key : String, value : Any)
    fun get(key : String) : Any?
    fun remove(key : String)
    fun exist(key : String) : Boolean

}