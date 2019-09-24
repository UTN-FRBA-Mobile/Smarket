package ar.edu.utn.frba.mobile.smarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.utn.frba.mobile.smarket.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
