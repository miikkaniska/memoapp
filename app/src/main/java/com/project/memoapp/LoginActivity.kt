package com.project.memoapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var loginButton : Button

    private lateinit var loginEmailEditText : EditText
    private lateinit var loginPasswordEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        loginEmailEditText = findViewById<EditText>(R.id.loginEmailText)
        loginPasswordEditText = findViewById<EditText>(R.id.loginPasswordText)

        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener{
            signIn(loginEmailEditText.text.toString(), loginPasswordEditText.text.toString())
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        if(validateInput(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        //MOVE TO MAIN MEMO VIEW FROM HERE, LOGIN WAS A SUCCESS
                        loginSuccessful()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        //Stay in the login screen and alert the user something went wrong
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
        // [END sign_in_with_email]
    }

    private fun validateInput(email: String, password: String) : Boolean
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            loginEmailEditText.setError("E-mail address is invalid.")
            return false
        }

        if(password.length < 6)
        {
            loginPasswordEditText.setError("Password is too short.")
            return false
        }
        //If none of the previous checks fail, return true
        return true
    }

    private fun loginSuccessful()
    {
        val currentUserUid = auth.currentUser!!.uid
        //Make sure the uid is not empty before attempting to query db
        if(currentUserUid.isNotEmpty()) {
            // Reference to the "users" collection
            val userCollection = db.collection("users")
            // Query the document with the current user's UID
            val userDocument = userCollection.document(currentUserUid)

            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Document exists, check the "username" field
                        val userUid = documentSnapshot.getString("UID")

                        // Check if the "username" field contains the desired value
                        if (currentUserUid == userUid) {
                            // The document is the current user's
                            val username = documentSnapshot.getString("username")
                            val email = documentSnapshot.getString("email")
                            val intent = Intent(this, FirstActivity::class.java)
                            //Pass extra intent info (the username for the current user for later use)
                            //intent.putExtra("USERNAME_EXTRA", username)
                            UserManager.getInstance().setUserDetails(username, currentUserUid, email, "")
                            startActivity(intent)
                        } else {
                            // The document exists, but the username doesn't match
                            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Document doesn't exist
                        Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failures, such as network errors or permission issues
            }
        }
    }
}