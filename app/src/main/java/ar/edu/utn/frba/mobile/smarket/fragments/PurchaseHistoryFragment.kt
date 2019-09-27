package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val history =
            if(savedInstanceState != null) {
                if (savedInstanceState.get("history") == null)
                    savedInstanceState.putParcelableArrayList("history", PurchaseService.getHistory())
                @Suppress("UNCHECKED_CAST")
                savedInstanceState.get("history") as ArrayList<Purchase>
            } else
                PurchaseService.getHistory()

        showPurchases(history)

        buttonNewPurchase.setOnClickListener {
            val action =
                PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToShoppingCartFragment()
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
