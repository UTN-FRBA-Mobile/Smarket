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

    lateinit var activityCommunication: Communication

    var products = ArrayList<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activityCommunication = activity as Communication
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addProduct()

        @Suppress("UNCHECKED_CAST")
        if (savedInstanceState?.get("products") != null)
            products = savedInstanceState["products"] as ArrayList<Product>

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
        if (activityCommunication.get("product") != null)
            products.add(activityCommunication.get("product") as Product)
    }

    private fun showProducts() {
        products.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.description.orEmpty()))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + (it.price * it.amount).toString()))
            tableRow.addView(newButton(it.id))
            tableShoppingCart.addView(tableRow)
        }
    }

    private fun newButton(id : Int): View {
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
            showProducts()
        }
    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }
}