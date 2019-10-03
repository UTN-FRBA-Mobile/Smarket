package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment : FragmentCommunication() {

    private var products = ArrayList<Product>()

    private var totalPrice = 0.0

    override fun getFragment(): Int {
        return R.layout.fragment_shopping_cart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        if (activityCommunication.exist("products"))
            products = activityCommunication.get("products") as ArrayList<Product>

        addProduct()

        showProducts()
        
        buttonAddProduct.setOnClickListener {
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToScanProductFragment()
            findNavController().navigate(action)
        }

        buttonFinishPurchase.setOnClickListener {
            activityCommunication.put("totalPrice", totalPrice)
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToOrderFragment()
            findNavController().navigate(action)
        }

        buttonCancelPurchase.setOnClickListener {
            activityCommunication.put("products", ArrayList<Product>())
            findNavController().popBackStack()
        }
    }

    private fun addProduct() {
        val product = activityCommunication.get("product") as Product?
        if (product?.uid != null) {
            products.add(product)
            activityCommunication.remove("product")
            saveProducts()
            buttonFinishPurchase.isEnabled = true
        }
    }

    private fun showProducts() {
        tableShoppingCart.removeAllViews()
        setTitles()
        totalPrice = 0.0
        products.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.description.orEmpty()))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + (it.price * it.amount).toString()))
            tableRow.addView(newButton(it.uid!!))
            tableShoppingCart.addView(tableRow)
            totalPrice += it.price * it.amount
        }
        textTotalPrice.text = totalPrice.toString()
    }

    private fun newButton(uid: String): View {
        val button = Button(context)
        button.text = "Eliminar (H)"
        button.setOnClickListener {
            deleteProduct(uid)
        }
        return button
    }

    private fun deleteProduct(uid: String) {
        val product = products.firstOrNull { it.uid == uid }
        if (product != null) {
            products.remove(product)
            saveProducts()
            showProducts()
            if (products.isEmpty())
                buttonFinishPurchase.isEnabled = false
        }
    }

    private fun saveProducts() {
        activityCommunication.put("products", products)
    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }

    private fun setTitles() {
        val tableRow = TableRow(context)
        tableRow.addView(newTextView(resources.getString(R.string.product)))
        tableRow.addView(newTextView(resources.getString(R.string.amount)))
        tableRow.addView(newTextView(resources.getString(R.string.price)))
        tableRow.addView(newTextView(resources.getString(R.string.actions)))
        tableShoppingCart.addView(tableRow)
    }
}