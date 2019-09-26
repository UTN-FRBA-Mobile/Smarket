package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.fragment_purchase_history.*

class PurchaseHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val history = ArrayList<Purchase>()

        if (savedInstanceState?.get("history") != null)
            history.addAll(savedInstanceState["history"] as ArrayList<Purchase>)
        else {
            history.addAll(PurchaseService.getHistory())
            savedInstanceState?.putParcelableArrayList("totalPrice", history)
        }

        showPurchases(history)

        buttonNewPurchase.setOnClickListener {
            val action =
                PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToOrderFragment()
            findNavController().navigate(action)
        }
    }

    private fun showPurchases(history : List<Purchase>) {
        history.forEach {
            val purchase = it
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(purchase.date.toString()))
            tableRow.addView(newTextView(purchase.amount.toString()))
            tableRow.addView(newTextView("$ " + purchase.price.toString()))
            tablePurchaseHistory.addView(tableRow)
        }

    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }
}
