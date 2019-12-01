package ar.edu.utn.frba.mobile.smarket.model

class Purchase(var uid: String?, var amount: Long, var description: String, var price: Double) {
    fun getTotalPrice() : Double {
        return this.price * this.amount
    }
}