package com.project.memoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class NewAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // Initialize Firebase Auth


    private lateinit var createAccountButton : Button

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var confirmPasswordEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        auth = Firebase.auth

        emailEditText = findViewById<EditText>(R.id.emailAddressText)
        passwordEditText = findViewById<EditText>(R.id.passwordText)
        confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordText)

        createAccountButton = findViewById(R.id.createAccountButton)

        createAccountButton.setOnClickListener{
            createAccount(emailEditText.text.toString(), passwordEditText.text.toString(), confirmPasswordEditText.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //Check if user is already logged in at start, if not, do something about it?
        }
    }

    private fun createAccount(email: String, password: String, confirmPassword: String) {
        // [START create_user_with_email]
        if(validateInput(email, password, confirmPassword)) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        //User creation is successful -> Transfer to Memo View or whatever?
                        setContentView(R.layout.fragment_second)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        //Account creation failed, stay in the view and say maybe try again?
                    }
                }
        }
        else
        {
            Toast.makeText(
                baseContext,
                "Check input.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String) : Boolean
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("E-mail address is invalid.")
            return false
        }

        if(password.length < 6)
        {
            passwordEditText.setError("Password is too short.")
            return false
        }

        if(!password.equals(confirmPassword))
        {
            passwordEditText.setError("Passwords don't match.")
            return false
        }
        //If none of the previous checks fail, return true
        return true
    }


}