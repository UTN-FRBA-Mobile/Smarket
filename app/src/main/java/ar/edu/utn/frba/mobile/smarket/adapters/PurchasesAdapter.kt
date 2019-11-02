package ar.edu.utn.frba.mobile.smarket.adapters

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.enums.PurchaseStatus
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import ar.edu.utn.frba.mobile.smarket.service.PurchaseService
import kotlinx.android.synthetic.main.item_purchase.view.*
import java.text.SimpleDateFormat
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
        holder.bindData(purchase)

    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindData(purchase: Purchase){
            itemView.textDescription.text = "$ " + purchase.price.toString()
            itemView.textProducts.text = purchase.amount.toString() + " productos"

            itemView.textDate.text = SimpleDateFormat("dd-MM-yy hh:mm")
                .format(purchase.date).toString()

            setPurchaseTextStatus(purchase)
            itemView.buttonRepeatPurchase.setOnClickListener {
                actionRepeatPurchase(purchase)
            }

            when {
                purchase.status == PurchaseStatus.FINISHED -> {
                    setButtonQualifyVisibility()
                    itemView.buttonQualify.setOnClickListener {

                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Califica esta compra")

                        val ratingBar = View.inflate(context, R.layout.ratingbar, null)
                        builder.setView(ratingBar)

                        builder.setPositiveButton("Confirmar") { _, _ ->
                            val number = ratingBar.findViewById<RatingBar>(R.id.ratingBar).rating
                            RatingService.qualify(purchase.uid, number)
                            purchase.status = PurchaseStatus.QUALIFIED

                            PurchaseService.updateStatus(purchase)
                            updateViewOnQualifyResult(purchase, number)
                        }
                        builder.create().show()
                    }
                }

                purchase.status == PurchaseStatus.QUALIFIED -> {
                    setButtonQualifyGone()
                    //TODO: obtener rating
                }

                else -> setButtonQualifyAndStatusGone()
            }
        }

        private fun updateViewOnQualifyResult(purchase: Purchase, number: Float){
            itemView.qualifyResult.rating = number
            setPurchaseTextStatus(purchase)
            setButtonQualifyGone()
        }

        private fun setPurchaseTextStatus(purchase: Purchase){
            itemView.textStatus.text = context.resources.getString(purchase.status.value)
            itemView.textStatus.setTextColor(ContextCompat.getColor(context, purchase.status.color))
        }

        private fun setButtonQualifyVisibility(){
            itemView.buttonQualify.visibility = View.VISIBLE
            itemView.qualifyResult.visibility = View.GONE
        }

        private fun setButtonQualifyGone(){
            itemView.buttonQualify.visibility = View.GONE
            itemView.qualifyResult.visibility = View.VISIBLE
        }

        private fun setButtonQualifyAndStatusGone(){
            itemView.buttonQualify.visibility = View.GONE
            itemView.qualifyResult.visibility = View.GONE
        }
    }

}