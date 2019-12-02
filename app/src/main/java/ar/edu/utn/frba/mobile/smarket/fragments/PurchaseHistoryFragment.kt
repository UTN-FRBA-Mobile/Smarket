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

    private lateinit var history: ArrayList<History>
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

        if (mainActivity.mViewModel.history != null) {
            history = mainActivity.mViewModel.history!!
            showHistory()
        } else {
            history = HistoryService.getHistory { showHistory() }
            mainActivity.mViewModel.history = history
        }

        buttonNewPurchase.setOnClickListener {
            mainActivity.mViewModel.purchases = null
            goToLocations()
        }
    }

    private fun goToLocations() {
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

    private fun startAdapter(){
        purchasesAdapter =
            PurchasesAdapter(history.sortedByDescending { it.date }, ::repeatPurchase, ::showInfoOrderFragmet, context!!)
        viewManager = LinearLayoutManager(context)

        recycler_view_purchases.apply {
            layoutManager = viewManager
            adapter = purchasesAdapter
        }
    }

    private fun showInfoOrderFragmet(history: History) {
        val action =
            PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToInfoOrderFragment()
        mainActivity.mViewModel.historySelected = history
        if (history.purchases != null) {
            findNavController().navigate(action)
        } else
            history.purchases = PurchaseService.getProducts(
                history.uid
            ) { findNavController().navigate(action) }
    }

    private fun repeatPurchase(history: History) {
        if (history.purchases != null) {
            mainActivity.mViewModel.purchases = history.purchases!!
            goToLocations()
        } else
            mainActivity.mViewModel.purchases = PurchaseService.getProducts(
                history.uid
            ) { goToLocations() }
    }
}
