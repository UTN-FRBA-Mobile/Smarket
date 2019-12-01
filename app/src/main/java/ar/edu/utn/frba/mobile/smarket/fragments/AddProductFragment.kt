package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.model.Product
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.util.*

class AddProductFragment : FragmentCommunication() {

    private lateinit var purchase : Purchase
    private lateinit var product: Product

    override fun getFragment(): Int {
        return R.layout.fragment_add_product
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Agregar Producto")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = activityCommunication.get("product") as Product

        purchase = Purchase(null, 1, product.description, product.price)

        activityCommunication.put("purchase", purchase)

        textUnitaryPrice.text = purchase.price.toString()

        textDetailProduct.text = purchase.description

        updateAmount()

        calculateTotalPrice()

        buttonIncrementUnit.setOnClickListener { increment() }

        buttonDecrementUnit.setOnClickListener { decrement() }

        buttonOk.setOnClickListener { ok() }

        buttonCancel.setOnClickListener { cancel() }
    }

    private fun disableDecrement() {
        buttonDecrementUnit.isEnabled = false
    }

    private fun enableDecrement() {
        buttonDecrementUnit.isEnabled = true
    }

    private fun increment() {
        purchase.amount += 1
        updateAmount()
        calculateTotalPrice()
        enableDecrement()
    }

    private fun decrement() {
        purchase.amount -= 1
        updateAmount()
        calculateTotalPrice()
        if (purchase.amount <= 1){
            disableDecrement()
        }
    }

    private fun cancel() {
        activityCommunication.remove("purchase")
        findNavController().popBackStack()
    }

    private fun ok() {
        purchase.uid = product.barcode
        findNavController().popBackStack()
    }

    private fun updateAmount() {
        textUnityAmount.text = purchase.amount.toString()
    }

    private fun calculateTotalPrice() {
        val totalPrice = purchase.price * purchase.amount
        textTotalPrice.text = totalPrice.toString()
    }
}