package ar.edu.utn.frba.mobile.smarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter(
    private val products: List<Product>
): RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_product
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.producto_nombre.text = products[position].description
        holder.itemView.producto_precio.text = products[position].price.toString()
        holder.itemView.producto_cantidad.text = products[position].amount.toString()
    }

    override fun getItemCount() = products.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}