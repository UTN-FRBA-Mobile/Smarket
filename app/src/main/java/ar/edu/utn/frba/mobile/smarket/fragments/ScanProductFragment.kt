package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_scan_product.*

class ScanProductFragment  : FragmentCommunication() {

    override fun getFragment(): Int {
        return R.layout.fragment_scan_product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            activityCommunication.put("product", Product(-1,1,"Coca Cola 1,5lt", 17.5))
            val action =
                ScanProductFragmentDirections.actionScanProductFragmentToAddProductFragment()
            findNavController().navigate(action)
        }
    }

}