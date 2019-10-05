package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.adapters.PurchasesAdapter
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.ProductService
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.fragment_purchase_history.*
import ar.edu.utn.frba.mobile.smarket.MainActivity


class PurchaseHistoryFragment : FragmentCommunication() {

    private lateinit var history : List<Purchase>
    private lateinit var purchasesAdapter: PurchasesAdapter
    private lateinit var viewManager: LinearLayoutManager

    override fun getFragment(): Int {
        return R.layout.fragment_purchase_history
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Historial de Compras")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activityCommunication.exist("history")) {
            history = activityCommunication.get("history") as List<Purchase>
        } else {
            history = PurchaseService.getHistory {}
            activityCommunication.put("history", history)
        }
        showHistory()

        buttonNewPurchase.setOnClickListener {
            goToShoppingCart()
        }
    }

    private fun goToShoppingCart() {
        val action =
            PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToShoppingCartFragment()
        findNavController().navigate(action)
    }

    private fun showHistory() {
        purchasesAdapter = PurchasesAdapter(history)
        viewManager = LinearLayoutManager(context)
        //agregar boton??? o hacerlo seleccionable

        recycler_view_purchases.apply {
            layoutManager = viewManager
            adapter = purchasesAdapter
        }
    }

    private fun newButtonLoad(purchase : Purchase) : View {
        val button = Button(context)
        button.text = getString(R.string.repeat)
        button.setOnClickListener {
            if (purchase.products != null) {
                activityCommunication.put("products", purchase.products!!)
                goToShoppingCart()
            } else
                activityCommunication.put("products", ProductService.getProducts(purchase.uid
                ) { goToShoppingCart() })
        }
        return button
    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }
}
