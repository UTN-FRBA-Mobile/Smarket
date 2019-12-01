package ar.edu.utn.frba.mobile.smarket.activities

import android.app.Activity
import ar.edu.utn.frba.mobile.smarket.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_new_account.*
class NewAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        buttonCreateUser.isEnabled = false
        buttonCreateUser.setOnClickListener {
            createAccount()
        }

        textUsername.addTextChangedListener{
            setCreateUserBtnEnabled()
        }

        textPassword.addTextChangedListener {
            if(!TextUtils.isEmpty(textPasswordConfirm.text.toString()) &&!validatePassword()){
                textPasswordConfirm.setError("La contraseña no coincide")
            }

            setCreateUserBtnEnabled()
        }

        textPasswordConfirm.addTextChangedListener {
            if(!validatePassword()){
                textPasswordConfirm.setError("La contraseña no coincide")
            }

            setCreateUserBtnEnabled()
        }
    }

    private fun setCreateUserBtnEnabled(){
        buttonCreateUser.isEnabled = validatePassword() && validateEmail()
    }

    private fun validateEmail(): Boolean{
        val emailPattern = Regex(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        return textUsername.text.toString().trim().matches(emailPattern)
    }

    private fun validatePassword(): Boolean{
        return textPassword.text!!.length >= 6
                && textPasswordConfirm.text!!.length >= 6
                && textPassword.text.toString() == textPasswordConfirm.text.toString()
    }


    private fun createAccount(){
        buttonCreateUser.isEnabled = false
        Toast.makeText(this, "Creando usuario", Toast.LENGTH_LONG).show()
        val username = textUsername.text.toString()
        val password = textPassword.text.toString()

        val authCreateUser = FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(username, password)

        authCreateUser.addOnCompleteListener {
            if(it.isSuccessful){
                finishActivity()
            }else{
                handleAuthException(it)
            }
            setCreateUserBtnEnabled()
        }
    }

    private fun handleAuthException(task: Task<AuthResult>){
        if(task.exception!!.javaClass == FirebaseAuthUserCollisionException::class.java){
            Toast.makeText(this,
                "Ya existe una cuenta asociada a este correo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun finishActivity(){
        setResult(Activity.RESULT_OK)
        FirebaseAuth.getInstance().signOut()
        this.finish()
    }
}
