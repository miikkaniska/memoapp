package com.project.memoapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
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

    private lateinit var saveNoteBtn : ImageButton

    private lateinit var title : EditText

    private lateinit var content : EditText

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note)

        title = findViewById<EditText>(R.id.notes_title_text)
        content = findViewById<EditText>(R.id.notes_content_text)

        saveNoteBtn = findViewById(R.id.save_note_btn)

        saveNoteBtn.setOnClickListener{
            //view -> startActivity(Intent(this, MainActivity::class.java)).apply {  }
            val intent = Intent(this, MainActivity::class.java)

            val docData = hashMapOf(
                "stringExample" to "Hello world!",
                "Title" to title.text.toString(),
                "Content" to content.text.toString(),
            )

            db.collection("data").document("test")
                .set(docData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    startActivity(intent)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }



}