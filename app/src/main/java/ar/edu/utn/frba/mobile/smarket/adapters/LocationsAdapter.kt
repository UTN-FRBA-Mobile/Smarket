package ar.edu.utn.frba.mobile.smarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.Location
import kotlinx.android.synthetic.main.item_location.view.*

class LocationsAdapter(
    private val locations: List<Location>,
    private val actionRemove: (Location) -> Unit,
    private var actionClick: () -> Unit
): RecyclerView.Adapter<LocationsAdapter.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_location
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                            .from(parent.context)
                            .inflate(viewType, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(locations[position])
    }

    override fun getItemCount() = locations.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindData(location: Location){
            itemView.locationDescription.text = location.address

            itemView.buttonRemove.setOnClickListener { actionRemove(location) }
            itemView.setOnClickListener { actionClick() }
        }
    }
}