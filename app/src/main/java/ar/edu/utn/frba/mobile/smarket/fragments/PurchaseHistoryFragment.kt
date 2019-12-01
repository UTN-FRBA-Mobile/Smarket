package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.adapters.PurchasesAdapter
import ar.edu.utn.frba.mobile.smarket.model.History
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import ar.edu.utn.frba.mobile.smarket.service.HistoryService
import kotlinx.android.synthetic.main.fragment_purchase_history.*
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity

class PurchaseHistoryFragment : FragmentCommunication() {

    private lateinit var history: List<History>
    private lateinit var purchasesAdapter: PurchasesAdapter
    private lateinit var viewManager: LinearLayoutManager

    override fun getFragment(): Int {
        return R.layout.fragment_purchase_history
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Mis Compras")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = activity?.findViewById<ProgressBar>(R.id.progressBar)
        val relativeLayout = activity?.findViewById<RelativeLayout>(R.id.relativeLayout)
        val noPurchasesText = activity?.findViewById<TextView>(R.id.noPurchasesText)

        progressBar?.visibility = View.VISIBLE
        relativeLayout?.visibility = View.INVISIBLE
        noPurchasesText?.visibility = View.INVISIBLE

        if (activityCommunication.exist("history")) {
            history = activityCommunication.get("history") as List<History>
            showHistory()
        } else {
            history = HistoryService.getHistory { showHistory() }
            activityCommunication.put("history", history)
        }

        buttonNewPurchase.setOnClickListener {
            val action = PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToLocationsFragment()
            findNavController().navigate(action)
        }
    }

    private fun goToShoppingCart() {
        val action =
            PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToLocationsFragment()
        findNavController().navigate(action)
    }

    private fun showHistory() {

        if(history.count() == 0){
            noPurchasesText?.visibility = View.VISIBLE
            relativeLayout?.visibility = View.VISIBLE

        }else{
            startAdapter()

            noPurchasesText?.visibility = View.GONE
            relativeLayout?.visibility = View.VISIBLE
        }
        progressBar?.visibility = View.GONE
    }

    fun startAdapter(){
        purchasesAdapter =
            PurchasesAdapter(history, ::repeatPurchase, context!!)
        viewManager = LinearLayoutManager(context)

        recycler_view_purchases.apply {
            layoutManager = viewManager
            adapter = purchasesAdapter
        }
    }

    private fun repeatPurchase(history: History) {
        if (history.purchases != null) {
            activityCommunication.put("purchases", history.purchases!!)
            goToShoppingCart()
        } else
            activityCommunication.put("purchases", PurchaseService.getProducts(
                history.uid
            ) { goToShoppingCart() })
    }
}
