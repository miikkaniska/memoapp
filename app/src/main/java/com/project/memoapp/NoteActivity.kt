package com.project.memoapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.memoapp.databinding.ActivityNoteBinding

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class NoteActivity : AppCompatActivity() {

    private lateinit var saveNoteBtn : Button

    private lateinit var title : TextView

    private lateinit var content : EditText

    private lateinit var createdDate : TextView

    private lateinit var editedText : TextView

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note)

        title = findViewById(R.id.memo_title)
        createdDate = findViewById(R.id.created_date_text)
        editedText = findViewById(R.id.edited_date_text)
        content = findViewById<EditText>(R.id.notes_content_text)

        saveNoteBtn = findViewById(R.id.save_note_btn)

        saveNoteBtn.setOnClickListener{
            //view -> startActivity(Intent(this, MainActivity::class.java)).apply {  }
            val intent = Intent(this, MainActivity::class.java)

            val docData = hashMapOf(
                "Title" to title.text.toString(),
                "Content" to content.text.toString(),
            )

            db.collection("data").document(title.text.toString())
                .set(docData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    startActivity(intent)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
        getFirebaseData()
    }

    private fun getFirebaseData() {
        db.collection("memos")
            .get()
            .addOnSuccessListener { result ->
                val data: MutableList<MemoData> = mutableListOf()

                for (document in result) {
                    // Käsittele dokumentti ja hae halutut tiedot
                    val email = document.getString("email") ?: ""
                    val nameOfMemo = document.getString("nameOfMemo") ?: ""
                    val nickname = document.getString("nickname") ?: ""
                    val isShared = document.getBoolean("isShared") ?: false

                    val creationTime = if (document.contains("creationTime")) {
                        document.getLong("creationTime") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }

                    val memo = MemoData(nameOfMemo, nickname, email, isShared, creationTime)
                    data.add(memo)
                }
            }
    }


}