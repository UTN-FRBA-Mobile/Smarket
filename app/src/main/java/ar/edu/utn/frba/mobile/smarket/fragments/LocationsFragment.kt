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

        if (mainActivity.mViewModel.locations != null) {
            locations = mainActivity.mViewModel.locations!!
            showLocations()
        } else {
            locations = LocationService.getLocations { showLocations() }
            mainActivity.mViewModel.locations = locations
        }

        buttonNew.setOnClickListener {
            showMap()
        }
    }

    private fun showMap() {
        super.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, RequestCode.RC_PERMISSION_LOCATION) {
            val intent = Intent(activity!!, MapActivity::class.java)
            startActivityForResult(intent, RequestCode.RC_MAP)
        }
    }

    private fun showLocations() {
        locationsAdapter =
            LocationsAdapter(locations, ::removeCallback, ::goShopping)
        viewManager = LinearLayoutManager(context)

        recycler_view_locations.apply {
            layoutManager = viewManager
            adapter = locationsAdapter
        }

        if(locations.isEmpty()) {
            showMap()
        }
    }

    private fun goShopping(location: Location) {
        mainActivity.mViewModel.location = location
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
        mainActivity.mViewModel.locations = locations
        locationsAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.RC_MAP && resultCode == Activity.RESULT_OK) {
            val location = Location(
                (data?.extras?.get("address") as String).toUpperCase(),
                data.extras?.get("latLng") as LatLng)
            if (locations.all { it.address != location.address}) {
                LocationService.save(location)
                locations.add(location)
            }
            goShopping(location)
        }
    }
}
