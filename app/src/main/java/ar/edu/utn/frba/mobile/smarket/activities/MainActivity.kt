package ar.edu.utn.frba.mobile.smarket.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ar.edu.utn.frba.mobile.smarket.R

class MainActivity : AppCompatActivity() , Communication {

    private val cache = HashMap<String, Any>()

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,host).setPrimaryNavigationFragment(host).commit()
    }


    override fun put(key: String, value: Any) {
        cache[key] = value
    }

    override fun get(key: String): Any? {
        return cache[key]
    }

    override fun remove(key: String) {
        cache.remove(key)
    }

    override fun exist(key: String): Boolean {
        return cache.containsKey(key)
    }

}
