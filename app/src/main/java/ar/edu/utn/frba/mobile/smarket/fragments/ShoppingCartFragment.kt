package ar.edu.utn.frba.mobile.smarket.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.activities.ScanActivity
import ar.edu.utn.frba.mobile.smarket.adapters.ProductsAdapter
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import kotlin.random.Random

class ShoppingCartFragment : FragmentCommunication() {

    private val RC_SCAN = 2

    private var products = ArrayList<Product>()

    private var totalPrice = 0.0
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var viewManager: LinearLayoutManager

    override fun getFragment(): Int {
        return R.layout.fragment_shopping_cart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        if (activityCommunication.exist("products"))
            products = activityCommunication.get("products") as ArrayList<Product>

            val numero = 45
            products.add(Product("1", 3, "Galletas Manon x 20 Grs", numero.toDouble()))
            products.add(Product("2", 1, "Bebida Coca Cola x 1lt", numero.toDouble()))
            products.add(Product("3", 2, "Jabon en polvo Ariel x 1kg", numero.toDouble()))
            products.add(Product("4", 1,"Aceite Pirulo x 1lt", numero.toDouble()))
            products.add(Product("5", 40, "Descripcion", numero.toDouble()))
            products.add(Product("6", 50, "Descripcion", numero.toDouble()))
            products.add(Product("1", 60, "Descripcion", numero.toDouble()))
            products.add(Product("1", 70, "Descripcion", numero.toDouble()))

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
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Carrito de Compras")
    }

    private fun showScan() {
        val intent = Intent(activity!!, ScanActivity::class.java)
        startActivityForResult(intent, RC_SCAN)

    }

    private fun addProduct() {
        val product = activityCommunication.get("product") as Product?
        if (product?.uid != null) {
            products.add(product)
            activityCommunication.remove("product")
            saveProducts()
            setEnabledButtonFinish()
        }
        setTotal()
    }

    private fun setEnabledButtonFinish() {
        buttonFinishPurchase.isEnabled = products.isNotEmpty()
    }

    private fun setTotal(){
        val inicial = 0
        val total = products
            .map { it.price * it.amount }
            .fold(inicial.toDouble(), {
                total, next -> total + next
            })

        finishPurchaseText.text = "TOTAL: $$total"
    }

    private fun removeProductCallback(product: Product){
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle(product.description)
            .setMessage("¿Desea eliminar este producto?")
            .setPositiveButton("Eliminar") { _, _ ->
                removeProduct(product)
            }
        builder.create().show()
    }

    private fun removeProduct(product: Product){
        products.remove(product)
        saveProducts()
        productsAdapter.notifyDataSetChanged()
        setEnabledButtonFinish()
        setTotal()
    }

    private fun updateCant(product: Product, cant: Int){
        product.amount += cant

        productsAdapter.notifyDataSetChanged()
        setEnabledButtonFinish()
        setTotal()
    }

    private fun showProducts() {

        productsAdapter = ProductsAdapter(products, ::removeProductCallback, ::updateCant)
        viewManager = LinearLayoutManager(context)

        recycler_view_products.apply {
            layoutManager = viewManager
            adapter = productsAdapter
        }
        setTotal()
    }

    private fun saveProducts() {
        activityCommunication.put("products", products)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SCAN && resultCode == RESULT_OK) {
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