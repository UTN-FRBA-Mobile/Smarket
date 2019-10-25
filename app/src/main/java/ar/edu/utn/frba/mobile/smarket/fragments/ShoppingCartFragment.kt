package ar.edu.utn.frba.mobile.smarket.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.activities.ScanActivity
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import kotlin.random.Random

class ShoppingCartFragment : FragmentCommunication() {

    private var products = ArrayList<Product>()

    private var totalPrice = 0.0

    override fun getFragment(): Int {
        return R.layout.fragment_shopping_cart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        if (activityCommunication.exist("products"))
            products = activityCommunication.get("products") as ArrayList<Product>

        val activityMain = activityCommunication as MainActivity
        activityMain.permissions[RequestCode.RC_PERMISSION_CAMERA] = { showActivityScan() }

        addProduct()

        showProducts()

        setEnabledButtonFinish()

        buttonAddProduct.setOnClickListener {
            showScan()
        }

        buttonFinishPurchase.setOnClickListener {
            activityCommunication.put("totalPrice", totalPrice)
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToOrderFragment()
            findNavController().navigate(action)
        }

        buttonCancelPurchase.setOnClickListener {
            activityCommunication.put("products", ArrayList<Product>())
            findNavController().popBackStack()
        }
    }

    private fun showScan() {
        getPermission(
            Manifest.permission.CAMERA,
            RequestCode.RC_PERMISSION_CAMERA
        ) { showActivityScan() }
    }

    private fun showActivityScan() {
        val intent = Intent(activity!!, ScanActivity::class.java)
        startActivityForResult(intent, RequestCode.RC_SCAN)
    }

    private fun addProduct() {
        val product = activityCommunication.get("product") as Product?
        if (product?.uid != null) {
            products.add(product)
            activityCommunication.remove("product")
            saveProducts()
            buttonFinishPurchase.isEnabled = true
            setEnabledButtonFinish()
        }
    }

    private fun setEnabledButtonFinish() {
        buttonFinishPurchase.isEnabled = products.isNotEmpty()
    }

    private fun showProducts() {
        tableShoppingCart.removeAllViews()
        setTitles()
        totalPrice = 0.0
        products.forEach {
            val tableRow = TableRow(context)
            tableRow.addView(newTextView(it.description))
            tableRow.addView(newTextView(it.amount.toString()))
            tableRow.addView(newTextView("$ " + (it.price * it.amount).toString()))
            tableRow.addView(newButton(it.uid!!))
            tableShoppingCart.addView(tableRow)
            totalPrice += it.price * it.amount
        }
        textTotalPrice.text = totalPrice.toString()
    }

    private fun newButton(uid: String): View {
        val button = Button(context)
        button.text =
            "Eliminar (H)" //todo los nombres con (H) son hardcodeados hay que ver si vva un simbolo o que
        button.setOnClickListener {
            deleteProduct(uid)
        }
        return button
    }

    private fun deleteProduct(uid: String) {
        val product = products.firstOrNull { it.uid == uid }
        if (product != null) {
            products.remove(product)
            saveProducts()
            showProducts()
            setEnabledButtonFinish()
        }
    }

    private fun saveProducts() {
        activityCommunication.put("products", products)
    }

    private fun newTextView(text: String): View {
        val textView = TextView(context)
        textView.text = text
        return textView
    }

    private fun setTitles() {
        val tableRow = TableRow(context)
        tableRow.addView(newTextView(resources.getString(R.string.product)))
        tableRow.addView(newTextView(resources.getString(R.string.amount)))
        tableRow.addView(newTextView(resources.getString(R.string.price)))
        tableRow.addView(newTextView(resources.getString(R.string.actions)))
        tableShoppingCart.addView(tableRow)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.RC_SCAN && resultCode == RESULT_OK) {
            val barCode = data?.extras?.get("barCode") as String
            activityCommunication.put(
                "product",
                Product(null, 1, barCode, Random.nextInt(1, 50).toDouble())
            )
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToAddProductFragment()
            findNavController().navigate(action)
        }
    }

}