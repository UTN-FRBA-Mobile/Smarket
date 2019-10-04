package ar.edu.utn.frba.mobile.smarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.ScanActivity
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_scan_product.*
import kotlin.random.Random

class ScanProductFragment : FragmentCommunication() {

    private val RC_SCAN = 2

    override fun getFragment(): Int {
        return R.layout.fragment_scan_product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            val intent = Intent(activity!!, ScanActivity::class.java)
            startActivityForResult(intent,RC_SCAN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SCAN) {
            val barCode = data?.extras?.get("barCode") as String
            activityCommunication.put("product", Product(null, 1, barCode, Random.nextInt(1,50).toDouble()))
            val action =
                 ScanProductFragmentDirections.actionScanProductFragmentToAddProductFragment()
             findNavController().navigate(action)
        }
    }
}