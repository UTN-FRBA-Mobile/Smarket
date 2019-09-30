package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import kotlinx.android.synthetic.main.fragment_order.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

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
            val action =
                OrderFragmentDirections.actionOrderFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }

        buttonFinishOrder.setOnClickListener {
            @Suppress("UNCHECKED_CAST")
            val history = activityCommunication.get("history") as ArrayList<Purchase>
            val products = activityCommunication.get("products") as ArrayList<Product>
            history.add(Purchase(Random.nextInt(), Date(), totalPrice, products.sumBy { it.amount }))

            val action = OrderFragmentDirections.actionOrderFragmentToPurchaseHistoryFragment()
            findNavController().navigate(action)
        }

    }
}