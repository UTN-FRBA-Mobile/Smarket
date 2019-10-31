package ar.edu.utn.frba.mobile.smarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import kotlinx.android.synthetic.main.item_purchase.view.*
import java.text.SimpleDateFormat

class PurchasesAdapter(
    private val dataSet: List<Purchase>,
    private val actionRepeatPurchase: (Purchase) -> Unit,
    private val context: Context
): RecyclerView.Adapter<PurchasesAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_purchase
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder{
        val textView = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val purchase : Purchase = dataSet[position]

        holder.itemView.textDescription.text = "$ " + purchase.price.toString()
        holder.itemView.textProducts.text = purchase.amount.toString() + " productos"

        holder.itemView.textDate.text = SimpleDateFormat("dd-MM-yyyy")
            .format(purchase.date).toString()

        holder.itemView.textHour.text = SimpleDateFormat("hh:mm")
            .format(purchase.date).toString()

        holder.itemView.textStatus.text = context.resources.getString(purchase.status.value)
        holder.itemView.textStatus.setTextColor(ContextCompat.getColor(context, purchase.status.color))

        holder.itemView.buttonRepeatPurchase.setOnClickListener {
            actionRepeatPurchase(purchase)
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}