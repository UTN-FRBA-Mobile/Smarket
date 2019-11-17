package ar.edu.utn.frba.mobile.smarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.activities.MainActivity
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.enums.RequestCode
import ar.edu.utn.frba.mobile.smarket.service.AuthenticationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.RuntimeException

class MainFragment : FragmentCommunication() {

    private lateinit var auth: FirebaseAuth

    private lateinit var mGoogleSignInClient : GoogleSignInClient
    
    override fun getFragment(): Int {
        return R.layout.fragment_main
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null)
            login()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        buttonLogin.setOnClickListener {
            if (this.validateUser())
                login()
        }

        buttonLoginGoogle.setOnClickListener {
            signIn()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle("Smarket")
    }

    private fun login() {
        val action =
            MainFragmentDirections.actionMainFragmentToPurchaseHistoryFragment()
        findNavController().navigate(action)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(activity, "Falló la autenticación con Google :(", Toast.LENGTH_LONG).show()
                throw RuntimeException(e)
            }
        }
    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RequestCode.RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    activityCommunication.put("user", auth.currentUser!!)
                    login()
                } else {
                    Toast.makeText(activity, "Google sign in failed:(", Toast.LENGTH_LONG).show()
                }
            }
    }
}