package ar.edu.utn.frba.mobile.smarket.adapters

import android.app.ActionBar
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.item_purchase.view.*
import java.text.SimpleDateFormat
import android.widget.FrameLayout
import android.widget.RelativeLayout
import ar.edu.utn.frba.mobile.smarket.service.RatingService

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

        if (purchase.status == PurchaseStatus.FINISHED) {
            holder.itemView.buttonQualify.visibility = View.VISIBLE

            holder.itemView.buttonQualify.setOnClickListener {
                //holder.itemView.relativeLayoutRatingBar.layoutParams = RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
                holder.itemView.relativeLayoutItemPurchase.layoutParams.width = 0
                holder.itemView.relativeLayoutItemPurchase.layoutParams.height = 0
            }

            holder.itemView.buttonQualifyCancel.setOnClickListener {
                //holder.itemView.relativeLayoutItemPurchase.layoutParams = RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
                holder.itemView.relativeLayoutRatingBar.layoutParams.width = 0
                holder.itemView.relativeLayoutRatingBar.layoutParams.height = 0
            }

            holder.itemView.buttonQualifyOk.setOnClickListener {
                RatingService.qualify(purchase.uid, holder.itemView.ratingBar.rating)
                purchase.status = PurchaseStatus.QUALIFIED
                PurchaseService.updateStatus(purchase)
               // holder.itemView.relativeLayoutItemPurchase.layoutParams = RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
                holder.itemView.relativeLayoutRatingBar.layoutParams.width = 0
                holder.itemView.relativeLayoutRatingBar.layoutParams.height = 0
            }
        }
    }

    private fun linearLayoutInvisible(linearLayout: View) {
        linearLayout.layoutParams =
            FrameLayout.LayoutParams(0, 0)
    }

    private fun linearLayoutVisible(linearLayout: View) {
        linearLayout.layoutParams =
            FrameLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}