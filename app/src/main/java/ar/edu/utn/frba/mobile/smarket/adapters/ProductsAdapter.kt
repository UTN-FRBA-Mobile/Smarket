package ar.edu.utn.frba.mobile.smarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter(
    private val purchases: List<Purchase>,
    private val actionRemoveProduct: (Purchase) -> Unit,
    private val actionUpdateProductCant: (Purchase, Int) -> Unit,
    private val context: Context
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
        holder.bindData(purchases[position])
    }

    override fun getItemCount() = purchases.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindData(purchase: Purchase){
            itemView.productName.text = purchase.description
            itemView.productPrice.text = "$ " + purchase.price.toString()
            itemView.productTotalPrice.text = purchase.getTotalPrice().toString()
            itemView.productUnits.text = purchase.amount.toString()
            Picasso.with(context).load(purchase.image).into(itemView.imageItemProduct)

            itemView.buttonRemoveProduct.setOnClickListener { actionRemoveProduct(purchase) }

            itemView.buttonIncreaseUnit.setOnClickListener { actionUpdateProductCant(purchase, 1) }

            itemView.buttonDecreaseUnit.isEnabled = purchase.amount > 1
            itemView.buttonDecreaseUnit.setOnClickListener { actionUpdateProductCant(purchase, -1) }
        }
    }

}