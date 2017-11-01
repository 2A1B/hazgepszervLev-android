package org.zapto.hazgepszerv.hazgepszervlev_android

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.indeterminateProgressDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var fbAuth = FirebaseAuth.getInstance()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login)

        if (fbAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener({_ -> login()})
    }

    private fun login() {
        val dialog = indeterminateProgressDialog("Bejelentkezés...")

        dialog.show()

        val email = input_email.text.toString()
        val password = input_password.text.toString()

        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, { task ->
            dialog.hide()
            if (task.isSuccessful) {
                onLoginSuccess()
            } else {
                onLoginFailed()
            }
        })
    }

    private fun onLoginSuccess() {
        btn_login.isEnabled = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginFailed() {
        Toast.makeText(baseContext, "A bejelentkezés sikertelen.", Toast.LENGTH_LONG).show()
        btn_login.isEnabled = true
    }

}