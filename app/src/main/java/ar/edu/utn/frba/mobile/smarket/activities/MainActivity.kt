package ar.edu.utn.frba.mobile.smarket.activities

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.model.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var mViewModel : MainViewModel

    val permissions = HashMap<Int, () -> Unit>()

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, host)
            .setPrimaryNavigationFragment(host).commit()

        mViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            this.permissions[requestCode]?.let { it() }
    }
}
