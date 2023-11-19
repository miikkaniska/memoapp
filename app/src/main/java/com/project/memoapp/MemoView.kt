package com.project.memoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.memoapp.databinding.ActivityMainBinding

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MemoView : AppCompatActivity() {


    private lateinit var addNoteBtn : FloatingActionButton

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        //setContentView(R.layout.activity_note)

        addNoteBtn = findViewById(R.id.add_note_btn)

        addNoteBtn.setOnClickListener{ view -> startActivity(Intent(this, NoteActivity::class.java)).apply {  }}

        //val intent = Intent(this, NewAccountActivity::class.java)
        //startActivity(intent)
    }
}

