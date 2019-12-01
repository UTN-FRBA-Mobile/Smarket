package ar.edu.utn.frba.mobile.smarket.model

class Purchase(var barCode: String?, var amount: Long, var description: String, var price: Double, var image: String) {
    fun getTotalPrice() : Double {
        return this.price * this.amount
    }
}