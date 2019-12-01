package ar.edu.utn.frba.mobile.smarket.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.activities.ScanActivity
import ar.edu.utn.frba.mobile.smarket.adapters.ProductsAdapter
import ar.edu.utn.frba.mobile.smarket.enums.RequestCode
import ar.edu.utn.frba.mobile.smarket.model.Product
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.ProductService
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartFragment : FragmentCommunication() {

    private var purchases = ArrayList<Purchase>()

    private var totalPrice = 0.0
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var viewManager: LinearLayoutManager

    override fun getFragment(): Int {
        return R.layout.fragment_shopping_cart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        if (mainActivity.mViewModel.purchases != null)
            purchases = mainActivity.mViewModel.purchases!!

        val activityMain = mainActivity
        activityMain.permissions[RequestCode.RC_PERMISSION_CAMERA] = { showActivityScan() }

        addPurchase()
        showProducts()
        setEnabledButtonFinish()
        buttonAddProduct.setOnClickListener {
            showScan()
        }

        buttonFinishPurchase.setOnClickListener {
            mainActivity.mViewModel.totalPrice = totalPrice
            val action =
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToOrderFragment()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Carrito de Compras")
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

    private fun addPurchase() {
        val purchase = mainActivity.mViewModel.purchase
        if (purchase?.barCode != null) {
            val actualPurchase = this.purchases.firstOrNull { it.barCode == purchase.barCode}
            if (actualPurchase != null)
                actualPurchase.amount += purchase.amount
            else
                purchases.add(purchase)
            mainActivity.mViewModel.purchase = null
            saveProducts()
            setEnabledButtonFinish()
        }
        setTotal()
    }

    private fun setEnabledButtonFinish() {
        buttonFinishPurchase.isEnabled = purchases.isNotEmpty()
    }

    private fun setTotal(){
        val inicial = 0
        totalPrice = purchases
            .map { it.price * it.amount }
            .fold(inicial.toDouble(), {
                total, next -> total + next
            })

        finishPurchaseText.text = "TOTAL: $$totalPrice"
    }

    private fun removeProductCallback(purchase: Purchase){
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle(purchase.description)
            .setMessage("¿Desea eliminar este producto?")
            .setPositiveButton("Eliminar") { _, _ ->
                removeProduct(purchase)
            }
        builder.create().show()
    }

    private fun removeProduct(purchase: Purchase){
        purchases.remove(purchase)
        saveProducts()
        productsAdapter.notifyDataSetChanged()
        setEnabledButtonFinish()
        setTotal()
    }

    private fun updateCant(purchase: Purchase, cant: Int){
        purchase.amount += cant

        productsAdapter.notifyDataSetChanged()
        setEnabledButtonFinish()
        setTotal()
    }

    private fun showProducts() {

        productsAdapter = ProductsAdapter(purchases, ::removeProductCallback, ::updateCant, context!!)
        viewManager = LinearLayoutManager(context)

        recycler_view_products.apply {
            layoutManager = viewManager
            adapter = productsAdapter
        }
        setTotal()
    }

    private fun saveProducts() {
        mainActivity.mViewModel.purchases = purchases
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.RC_SCAN && resultCode == RESULT_OK) {
            val barCode = data?.extras?.get("barCode") as String
            ProductService.getProduct(barCode, {setProduct(it)}, {
                Toast.makeText(context!!, resources.getString(R.string.productNotFound), Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun setProduct (product: Product) {
        mainActivity.mViewModel.product = product
        val action =
            ShoppingCartFragmentDirections.actionShoppingCartFragmentToAddProductFragment()
        findNavController().navigate(action)
    }

}