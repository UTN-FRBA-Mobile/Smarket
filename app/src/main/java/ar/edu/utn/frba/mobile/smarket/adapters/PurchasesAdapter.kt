package ar.edu.utn.frba.mobile.smarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.fragments.PurchaseHistoryFragmentDirections
import ar.edu.utn.frba.mobile.smarket.model.Purchase
import kotlinx.android.synthetic.main.item_purchase.view.*
import java.text.SimpleDateFormat

class PurchasesAdapter(
        private val dataSet: List<Purchase>
    ): RecyclerView.Adapter<PurchasesAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_purchase
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder{
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return ViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.itemView.contenido.text = "$ " + dataSet[position].price.toString()
        holder.itemView.productos.text = dataSet[position].amount.toString() + " productos"

        holder.itemView.fecha.text = SimpleDateFormat("dd-MM-yyyy")
            .format(dataSet[position].date).toString()

        holder.itemView.hora.text = SimpleDateFormat("hh:mm")
            .format(dataSet[position].date).toString()

        holder.itemView.action_button_1.setOnClickListener {
            var action =
                PurchaseHistoryFragmentDirections
                    .actionPurchaseHistoryFragmentToShoppingCartFragment()

            NavHostFragment
                .findNavController(FragmentManager.findFragment(it))
                .navigate(action)
        }
    }

    override fun getItemCount() = dataSet.size

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}