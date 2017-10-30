package org.zapto.hazgepszerv.hazgepszervlev_android

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.indeterminateProgressDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        var btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener({_ -> login()})
    }

    fun login() {
        //indeterminateProgressDialog("Bejelentkezés").show()

        val email = input_email.text.toString()
        val password = input_password.text.toString()

        Toast.makeText(baseContext, email, Toast.LENGTH_LONG).show()
        Toast.makeText(baseContext, password, Toast.LENGTH_LONG).show()


        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if (task.isSuccessful) {
                indeterminateProgressDialog("Bejelentkezés").hide()
                onLoginSuccess()
            } else {
                onLoginFailed()
            }
        })
    }

    fun onLoginSuccess() {
        btn_login.setEnabled(true)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(baseContext, "A bejelentkezés sikertelen.", Toast.LENGTH_LONG).show()
        btn_login.setEnabled(true)
    }

}