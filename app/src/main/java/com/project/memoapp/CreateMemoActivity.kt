package com.project.memoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class CreateMemoActivity : AppCompatActivity() {


    val db = Firebase.firestore
    val auth = Firebase.auth

    private lateinit var titleEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var shareToggle: Switch

    private lateinit var buttonAddPerson: Button
    private lateinit var buttonConfirm: Button
    private lateinit var buttonCancel: Button

    private lateinit var usernameTextView: TextView
    private lateinit var sharedTextView: TextView
    private lateinit var sharedUsernameListTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memo)

        titleEditText = findViewById(R.id.nameOfMemo)
        usernameEditText = findViewById(R.id.editText_person)
        shareToggle = findViewById(R.id.toggle_share)
        buttonAddPerson = findViewById(R.id.btn_add)
        buttonConfirm = findViewById(R.id.btn_confirm)
        buttonCancel = findViewById(R.id.btn_cancel)
        usernameTextView = findViewById(R.id.textview_nickname)
        sharedTextView = findViewById(R.id.textview_shared)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        shareToggle.setOnCheckedChangeListener { _, isChecked ->
            Log.d("SwitchState", "Switch state changed to: $isChecked")

            usernameTextView.visibility = if (isChecked) View.VISIBLE else View.GONE
            usernameEditText.visibility = if (isChecked) View.VISIBLE else View.GONE
            buttonAddPerson.visibility = if (isChecked) View.VISIBLE else View.GONE
            sharedTextView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        buttonAddPerson.setOnClickListener {
            if (usernameEditText.text.isEmpty()) {
                usernameEditText.setError("Name can not be empty.")
                Toast.makeText(this, "Please fill every field.", Toast.LENGTH_SHORT).show()
            } else {
                sharedTextView.visibility = View.VISIBLE
                usernameTextView.visibility = View.VISIBLE
                sharedTextView.text = "${sharedTextView.text}\n${usernameEditText.text}"
            }
        }

        buttonCancel.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }

        buttonConfirm.setOnClickListener {
            val memoTitle = titleEditText.text.toString()
            val username = usernameEditText.text.toString()
            val sharedWith = sharedTextView.text.toString()

            if (memoTitle.isEmpty()) {
                titleEditText.setError("Memo name can't be empty.")
                Toast.makeText(this, "Memo name can't be empty.", Toast.LENGTH_SHORT).show()
            }

            val userManager = UserManager.getInstance()

            // Luo Firebase-tietokantaan tallennettava olio
            val memoData = MemoData(
                memoTitle,
                "", /*sharedWith*/
                arrayListOf(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                userManager.getUsername().toString()
            )


            // Hae "memos" -kokoelma Firestoresta

            val memosCollection = db.collection("memos").document(userManager.getUid().toString()).collection("userMemos").document(titleEditText.text.toString())

            // Tallenna tietokantaan
            memosCollection.set(memoData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data tallennettu Firestoreen", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, FirstActivity::class.java)

                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Tallennuksessa tapahtui virhe: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}


