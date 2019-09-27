package ar.edu.utn.frba.mobile.smarket

interface Communication {

    fun put(key : String, value : Any)
    fun get(key : String) : Any?
    fun remove(key : String)

}