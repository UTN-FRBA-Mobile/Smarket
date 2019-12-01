package ar.edu.utn.frba.mobile.smarket.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.activities.MapActivity
import ar.edu.utn.frba.mobile.smarket.adapters.LocationsAdapter
import ar.edu.utn.frba.mobile.smarket.enums.RequestCode
import ar.edu.utn.frba.mobile.smarket.model.Location
import ar.edu.utn.frba.mobile.smarket.service.LocationService
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_locations.*
import android.widget.Toast

class LocationsFragment : FragmentCommunication() {

    private lateinit var locations: ArrayList<Location>
    private lateinit var locationsAdapter: LocationsAdapter
    private lateinit var viewManager: LinearLayoutManager

    override fun getFragment(): Int {
        return R.layout.fragment_locations
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Mis ubicaciones")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activityCommunication.exist("locations")) {
            locations = activityCommunication.get("locations") as ArrayList<Location>
            showHistory()
        } else {
            locations = LocationService.getLocations { showHistory() }
            activityCommunication.put("locations", locations)
        }

        buttonNew.setOnClickListener {
            super.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, RequestCode.RC_PERMISSION_LOCATION) { showMap() }
        }
    }

    private fun showMap() {
        val intent = Intent(activity!!, MapActivity::class.java)
        startActivityForResult(intent, RequestCode.RC_MAP)
    }

    private fun showHistory() {
        locationsAdapter =
            LocationsAdapter(locations, ::removeCallback, ::onItemClick)
        viewManager = LinearLayoutManager(context)

        recycler_view_locations.apply {
            layoutManager = viewManager
            adapter = locationsAdapter
        }
    }

    fun onItemClick(item: Location) {
        val action = LocationsFragmentDirections.actionLocationsToShoppingCartFragment()
        findNavController().navigate(action)
    }

    private fun removeCallback(location: Location){
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle(location.address)
            .setMessage("¿Desea eliminar esta ubicación?")
            .setPositiveButton("Eliminar") { _, _ ->
                remove(location)
            }
        builder.create().show()
    }

    private fun remove(location: Location){
        locations.remove(location)
        activityCommunication.put("locations", locations)
        locationsAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.RC_MAP && resultCode == Activity.RESULT_OK) {
            val location = Location(
                data?.extras?.get("address") as String,
                data.extras?.get("latLng") as LatLng)
            LocationService.save(location)
            locations.add(location)

            val action = LocationsFragmentDirections.actionLocationsToShoppingCartFragment()
            findNavController().navigate(action)
        }
    }
}