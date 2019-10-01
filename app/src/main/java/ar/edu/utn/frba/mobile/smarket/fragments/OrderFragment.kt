package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.fragment_order.*
import java.util.*

class OrderFragment  : FragmentCommunication() {

    var totalPrice = 0.0

    override fun getFragment(): Int {
        return R.layout.fragment_order
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalPrice = activityCommunication.get("totalPrice") as Double
        textTotalPrice.text = totalPrice.toString()

        textSeeShoppingCart.setOnClickListener {
            val action = OrderFragmentDirections.actionOrderFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }

        @Suppress("UNCHECKED_CAST")
        buttonFinishOrder.setOnClickListener {
            val history = activityCommunication.get("history") as ArrayList<Purchase>
            val products = activityCommunication.get("products") as ArrayList<Product>
            val purchase = Purchase(UUID.randomUUID().toString(), Date(), totalPrice, products.sumBy { it.amount })
            history.add(purchase)
            PurchaseService.savePurchase(purchase)
            findNavController().popBackStack()
        }

    }
}