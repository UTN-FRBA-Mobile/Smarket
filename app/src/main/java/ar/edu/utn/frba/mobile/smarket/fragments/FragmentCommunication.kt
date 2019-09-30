package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.smarket.Communication

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
}