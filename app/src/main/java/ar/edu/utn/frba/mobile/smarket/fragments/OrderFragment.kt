package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment  : Fragment() {

    var totalPrice : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textTotalPrice.text = totalPrice.toString()

        textSeeShoppingCart.setOnClickListener {
            val action =
                OrderFragmentDirections.actionOrderFragmentToShoppingCartFragment()
            findNavController().navigate(action)
        }

        buttonFinishOrder.setOnClickListener {

        }

    }
}