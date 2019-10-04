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

class PurchaseHistoryFragment : FragmentCommunication() {

    private lateinit var history : List<Purchase>
    private lateinit var purchasesAdapter: PurchasesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun getFragment(): Int {
        return R.layout.fragment_purchase_history
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activityCommunication.exist("history")) {
            history = activityCommunication.get("history") as List<Purchase>
            //showHistory()
        } else {
            history = PurchaseService.getHistory { showHistory() }
            activityCommunication.put("history", history)
        }

        recycler_view_purchases.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = purchasesAdapter
        }

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
        history.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.date.toString()))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + it.price.toString()))
            tableRow.addView(newButtonLoad(it))
            //tablePurchaseHistory.addView(tableRow)
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
