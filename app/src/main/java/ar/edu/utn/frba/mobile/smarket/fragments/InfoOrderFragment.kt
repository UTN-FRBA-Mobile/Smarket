package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.adapters.InfoProductsAdapter
import kotlinx.android.synthetic.main.fragment_info_order.*

class InfoOrderFragment: FragmentCommunication() {

    override fun getFragment(): Int {
        return R.layout.fragment_info_order
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val history = mainActivity.mViewModel.historySelected!!

        val infoProductAdapter = InfoProductsAdapter(history.purchases!!, context!!)
        val viewManager = LinearLayoutManager(context)

        recycler_view_info_products.apply {
            layoutManager = viewManager
            adapter = infoProductAdapter
        }

        textTotalPriceHistoryInfo.text = "TOTAL: ${history.price}"
        textLocation.text = "Dirección: ${history.address}"

        buttonReturnToHistory.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}