package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.Communication
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment : Fragment() {

    private lateinit var activityCommunication: Communication

    private var products = ArrayList<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        activityCommunication = activity as Communication

        @Suppress("UNCHECKED_CAST")
        if (activityCommunication.exist("products"))
            products = activityCommunication.get("products") as ArrayList<Product>

        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addProduct()

        showProducts()
        
        buttonAddProduct.setOnClickListener {
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToScanProductFragment()
            findNavController().navigate(action)
        }

        buttonFinishPurchase.setOnClickListener {
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToOrderFragment()
            findNavController().navigate(action)
        }

        buttonCancelPurchase.setOnClickListener {
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToPurchaseHistoryFragment()
            findNavController().navigate(action)
        }
    }

    private fun addProduct() {
        if (activityCommunication.exist("product")) {
            products.add(activityCommunication.get("product") as Product)
            saveProducts()
        }
    }

    private fun showProducts() {
        tableShoppingCart.removeAllViews()
        setTitles()
        products.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.description.orEmpty()))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + (it.price * it.amount).toString()))
            tableRow.addView(newButton(it.id))
            tableShoppingCart.addView(tableRow)
        }
    }

    private fun newButton(id: Int): View {
        val button = Button(context)
        button.text = "Eliminar (H)"
        button.setOnClickListener {
            deleteProduct(id)
        }
        return button
    }

    private fun deleteProduct(id: Int) {
        val product = products.firstOrNull { it.id == id }
        if (product != null) {
            products.remove(product)
            saveProducts()
            showProducts()
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