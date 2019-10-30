package ar.edu.utn.frba.mobile.smarket.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.smarket.activities.Communication
import ar.edu.utn.frba.mobile.smarket.enums.RequestCode

abstract class FragmentCommunication : Fragment() {

    lateinit var activityCommunication: Communication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        activityCommunication = activity as Communication

        return inflater.inflate(getFragment(), container, false)
    }

    protected abstract fun getFragment() : Int

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, permission) == PackageManager.PERMISSION_GRANTED
    }

    protected fun getPermission(permission: String, requestCode: Int,  action : () -> Unit) {
        if (!hasPermission(permission))
            ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
        else
            action()
    }
}