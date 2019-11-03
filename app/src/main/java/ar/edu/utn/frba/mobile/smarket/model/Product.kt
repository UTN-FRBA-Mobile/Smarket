package ar.edu.utn.frba.mobile.smarket.model

class Product(var uid: String?, var amount: Long, var description: String, var price: Double) {
    fun getTotalPrice() : Double {
        return this.price * this.amount
    }
}