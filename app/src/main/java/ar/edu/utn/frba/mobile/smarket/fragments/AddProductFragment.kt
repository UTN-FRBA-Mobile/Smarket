package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.Communication
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_add_product.*

class AddProductFragment : Fragment() {

    lateinit var activityCommunication: Communication

    lateinit var product : Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activityCommunication = activity as Communication
        return inflater.inflate(R.layout.fragment_add_product, container, false)
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

    private fun increment() {
        product.amount += 1
        updateAmount()
        calculateTotalPrice()
    }

    private fun decrement() {
        product.amount -= 1
        updateAmount()
        calculateTotalPrice()
    }

    private fun cancel() {
        activityCommunication.remove("product")
        val action = AddProductFragmentDirections.actionAddProductFragmentToShoppingCartFragment()
        findNavController().navigate(action)
    }

    private fun ok() {
        val action = AddProductFragmentDirections.actionAddProductFragmentToShoppingCartFragment()
        findNavController().navigate(action)
    }

    private fun updateAmount() {
        textUnityAmount.text = product.amount.toString()
    }

    private fun calculateTotalPrice() {
        val totalPrice = product.price * product.amount
        textTotalPrice.text = totalPrice.toString()
    }
}