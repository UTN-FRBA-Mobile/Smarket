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
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment : Fragment() {

    var products = ArrayList<Product>()
    lateinit var savedInstance: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null)
            savedInstance = savedInstanceState


        @Suppress("UNCHECKED_CAST")
        if (savedInstanceState?.get("products") != null)
            products = savedInstanceState["products"] as ArrayList<Product>
        else
            products.add(Product(1,2,"Sarasa", 12.3))


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

    private fun showProducts() {
        products.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.description.orEmpty()))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + it.price.toString()))
            tableRow.addView(newButton(it.id))
            tableShoppingCart.addView(tableRow)
        }
    }

    private fun newButton(id : Int): View {
        val button = Button(context)
        button.setText("Eliminar (H)")
        button.setOnClickListener {
            deleteProduct(id)
            this.savedInstance.putParcelableArrayList("products", products)
        }
        return button
    }

    private fun deleteProduct(id: Int) {
        products.remove(products.first { it.id == id })
    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }
}