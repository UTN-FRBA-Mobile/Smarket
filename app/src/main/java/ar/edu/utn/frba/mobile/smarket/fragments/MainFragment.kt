package ar.edu.utn.frba.mobile.smarket.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.service.AuthenticationService
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : FragmentCommunication() {

    override fun getFragment(): Int {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogin.setOnClickListener {
            if (this.validateUser()) {
                val action =
                    MainFragmentDirections.actionMainFragmentToPurchaseHistoryFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun validateUser() : Boolean {

        val username = textUsername.text.toString()
        val password = textPassword.text.toString()

        textError.visibility = View.INVISIBLE

        textUsernameError.visibility = getError(username)
        textPasswordError.visibility = getError(password)

        if (username.isNotBlank() || password.isNotBlank()) {
            if (AuthenticationService.authenticate(username, password))
                return true
            else {
                textError.text = resources.getString(R.string.incorrectUsernameOrPassword)
                textError.visibility = View.VISIBLE
            }
        }

        return false
    }

    private fun getError(text: String) : Int {
        return if (text.isBlank()) View.VISIBLE else View.INVISIBLE
    }
}