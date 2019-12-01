package ar.edu.utn.frba.mobile.smarket.adapters

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
import ar.edu.utn.frba.mobile.smarket.model.History
import ar.edu.utn.frba.mobile.smarket.service.HistoryService
import kotlinx.android.synthetic.main.item_purchase.view.*
import java.text.SimpleDateFormat
import android.widget.Toast

class PurchasesAdapter(
    private val dataSet: List<History>,
    private val actionRepeatPurchase: (History) -> Unit,
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

        val history : History = dataSet[position]
        holder.bindData(history)

    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindData(history: History){
            itemView.textPrice.text = "$ " + history.price.toString()
            itemView.textProducts.text = history.amount.toString() + " productos"

            itemView.textDate.text = SimpleDateFormat("dd-MM-yy hh:mm")
                .format(history.date).toString()

            setPurchaseTextStatus(history)
            itemView.buttonRepeatPurchase.setOnClickListener {
                actionRepeatPurchase(history)
            }

            when {
                history.status == PurchaseStatus.FINISHED -> {
                    setButtonQualifyVisibility()
                    itemView.buttonQualify.setOnClickListener {

                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Califica esta compra")

                        val ratingBar = View.inflate(context, R.layout.ratingbar, null)
                        builder.setView(ratingBar)

                        builder.setPositiveButton("Confirmar") { _, _ ->
                            history.rating = ratingBar.findViewById<RatingBar>(R.id.ratingBar).rating
                            history.status = PurchaseStatus.QUALIFIED

                            HistoryService.updatePurchase(history)
                            updateViewOnQualifyResult(history)
                            Toast.makeText(context, "Gracias por calificar!", Toast.LENGTH_SHORT).show()
                        }
                        builder.create().show()
                    }
                }

                history.status == PurchaseStatus.QUALIFIED -> {
                    setButtonQualifyGone()
                    itemView.qualifyResult.rating = history.rating!!
                }

                else -> setButtonQualifyAndStatusGone()
            }
        }

        private fun updateViewOnQualifyResult(history: History){
            itemView.qualifyResult.rating = history.rating!!
            setPurchaseTextStatus(history)
            setButtonQualifyGone()
        }

        private fun setPurchaseTextStatus(history: History){
            itemView.textStatus.text = context.resources.getString(history.status.value)
            itemView.textStatus.setTextColor(ContextCompat.getColor(context, history.status.color))
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