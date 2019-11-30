package ar.edu.utn.frba.mobile.smarket.activities

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import android.content.Intent
import ar.edu.utn.frba.mobile.smarket.R
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var selectedLatLng: LatLng
    private lateinit var selectedAddress: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationSearch.setOnClickListener{ searchLocation() }
        confirm.setOnClickListener{ confirmLocation() }
        confirm.isEnabled = false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)

        setUpMap()

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                placeMarkerOnMap(LatLng(location.latitude, location.longitude))
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun placeMarkerOnMap(latLng: LatLng, title: String = "") {
        val markerOptions = MarkerOptions().position(latLng)
        if(!title.isNullOrBlank()) {
            markerOptions.title(title)
        }
        mMap.addMarker(markerOptions)

        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng, 14f)))
    }

    private fun confirmLocation() {
        val intent = Intent(this, MainActivity::class.java) //ShoppingCartFragment
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.putExtra("latLng", selectedLatLng)
        intent.putExtra("address", selectedAddress)
        setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

    private fun searchLocation() {
        confirm.isEnabled = false
        var location = editText.text.toString()

        if (location.isNullOrBlank()) {
            Toast.makeText(applicationContext,"Tenés que insertar la dirección",Toast.LENGTH_SHORT).show()
        }
        else{
            val geoCoder = Geocoder(this)
            try {
                var addressList: List<Address> = geoCoder.getFromLocationName(location, 1)
                if(!addressList.isNullOrEmpty()) {
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.clear()
                    placeMarkerOnMap(latLng, location)
                    selectedLatLng = latLng
                    selectedAddress = location
                    confirm.isEnabled = true
                }
                else {
                    Toast.makeText(applicationContext,"No existe la dirección",Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(applicationContext,"No se pudo obtener la dirección",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
