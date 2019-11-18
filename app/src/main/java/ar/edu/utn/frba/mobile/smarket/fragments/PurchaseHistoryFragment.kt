package ar.edu.utn.frba.mobile.smarket.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.adapters.PurchasesAdapter
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.ProductService
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.fragment_purchase_history.*
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.activities.MapActivity
import ar.edu.utn.frba.mobile.smarket.enums.RequestCode
import ar.edu.utn.frba.mobile.smarket.model.Address
import com.google.android.gms.maps.model.LatLng

class PurchaseHistoryFragment : FragmentCommunication() {

    private lateinit var history: List<Purchase>
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

        (activityCommunication as MainActivity).permissions[RequestCode.RC_PERMISSION_LOCATION] = { showMap() }

        if (activityCommunication.exist("history")) {
            history = activityCommunication.get("history") as List<Purchase>
            showHistory()
        } else {
            history = PurchaseService.getHistory { showHistory() }
            activityCommunication.put("history", history)
        }

        buttonNewPurchase.setOnClickListener {
            super.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, RequestCode.RC_PERMISSION_LOCATION) { showMap() }
        }
    }

    private fun showMap() {
        val intent = Intent(activity!!, MapActivity::class.java)
        startActivityForResult(intent, RequestCode.RC_MAP)
    }

    private fun goToShoppingCart() {
        val action =
            PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToShoppingCartFragment()
        findNavController().navigate(action)
    }

    private fun showHistory() {
        purchasesAdapter =
            PurchasesAdapter(history, ::repeatPurchase, context!!)
        viewManager = LinearLayoutManager(context)

        recycler_view_purchases.apply {
            layoutManager = viewManager
            adapter = purchasesAdapter
        }
    }

    private fun repeatPurchase(purchase: Purchase) {
        if (purchase.products != null) {
            activityCommunication.put("products", purchase.products!!)
            goToShoppingCart()
        } else
            activityCommunication.put("products", ProductService.getProducts(
                purchase.uid
            ) { goToShoppingCart() })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.RC_MAP && resultCode == Activity.RESULT_OK) {
            val address = data?.extras?.get("address") as String
            val latLng = data.extras?.get("latLng") as LatLng
            activityCommunication.put(
                "address", Address(address, latLng)
            )
            val action =
                PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }
    }
}
