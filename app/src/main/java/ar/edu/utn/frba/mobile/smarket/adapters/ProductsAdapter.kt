package ar.edu.utn.frba.mobile.smarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.fragment_shopping_cart.view.*
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter(
    private val products: List<Product>,
    private val actionRemoveProduct: (Product) -> Unit,
    private val actionUpdateProductCant: (Product, Int) -> Unit
): RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                            .from(parent.context)
                            .inflate(viewType, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(products[position])
    }

    override fun getItemCount() = products.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindData(product: Product){
            itemView.producto_nombre.text = product.description
            itemView.producto_precio.text = "$ " + product.price.toString()
            itemView.producto_cantidad.text = product.amount.toString()

            itemView.producto_eliminar.setOnClickListener { actionRemoveProduct(product) }

            itemView.producto_increase_cantidad.setOnClickListener { actionUpdateProductCant(product, 1) }

            itemView.producto_decrease_cantidad.isEnabled = product.amount > 1
            itemView.producto_decrease_cantidad.setOnClickListener { actionUpdateProductCant(product, -1) }
        }
    }

}