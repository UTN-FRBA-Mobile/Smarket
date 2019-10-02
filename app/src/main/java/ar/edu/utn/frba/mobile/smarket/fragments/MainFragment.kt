package ar.edu.utn.frba.mobile.smarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.utn.frba.mobile.smarket.R
import ar.edu.utn.frba.mobile.smarket.service.AuthenticationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : FragmentCommunication() {

    private val RC_SIGN_IN = 1

    private lateinit var auth: FirebaseAuth

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("146266657134-k66q1vuj4qnjgrcontj6sejn6eguietq.apps.googleusercontent.com")
        .requestEmail()
        .build()
    
    override fun getFragment(): Int {
        return R.layout.fragment_main
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null)
            login()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        buttonLogin.setOnClickListener {
            if (this.validateUser())
                login()
        }

        buttonLoginGoogle.setOnClickListener {
            signIn()
        }
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

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        }
    }
    private fun signIn() {
        val mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    activityCommunication.put("user", auth.currentUser!!)
                    login()
                } else {
                    println("ERROR AL VALIDAR CON FIREBASE")
                }
            }
    }

}