package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.fragment_purchase_history.*

class PurchaseHistoryFragment : FragmentCommunication() {

    override fun getFragment(): Int {
        return R.layout.fragment_purchase_history
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!activityCommunication.exist("history"))
            activityCommunication.put("history", PurchaseService.getHistory())

        showPurchases(activityCommunication.get("history") as List<Purchase>)

        buttonNewPurchase.setOnClickListener {
            val action =
                PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }

        buttonMap.setOnClickListener {
            val action =
                PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToMapFragment()
            findNavController().navigate(action)
        }
    }

    private fun showPurchases(history : List<Purchase>) {
        history.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.date.toString()))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + it.price.toString()))
            tablePurchaseHistory.addView(tableRow)
        }
    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }
}
