package ar.edu.utn.frba.mobile.smarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogin.setOnClickListener {
            if (this.validateUser()) {
                val action = MainFragmentDirections.actionMainFragmentToOrderFragment()
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
            if (authenticate(username, password))
                return true
            else {
                textError.text = resources.getString(R.string.incorrectUsernameOrPassword)
                textError.visibility = View.VISIBLE
            }
        }

        return false
    }

    private fun authenticate(username: String, password: String) : Boolean {
        return username.toUpperCase() == "ADMIN" && password == "admin"

    }

    private fun getError(text: String) : Int {
        return if (text.isBlank()) View.VISIBLE else View.INVISIBLE
    }
}