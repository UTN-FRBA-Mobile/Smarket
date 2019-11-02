package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.util.*

class AddProductFragment : FragmentCommunication() {

    private lateinit var product : Product

    override fun getFragment(): Int {
        return R.layout.fragment_add_product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = activityCommunication.get("product") as Product

        textUnitaryPrice.text = product.price.toString()

        textDetailProduct.text = product.description

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
        product.amount += 1
        updateAmount()
        calculateTotalPrice()
        enableDecrement()
    }

    private fun decrement() {
        product.amount -= 1
        updateAmount()
        calculateTotalPrice()
        if (product.amount <= 1){
            disableDecrement()
        }
    }

    private fun cancel() {
        activityCommunication.remove("product")
        findNavController().popBackStack()
    }

    private fun ok() {
        product.uid = UUID.randomUUID().toString()
        findNavController().popBackStack()
    }

    private fun updateAmount() {
        textUnityAmount.text = product.amount.toString()
    }

    private fun calculateTotalPrice() {
        val totalPrice = product.price * product.amount
        textTotalPrice.text = totalPrice.toString()
    }
}