package com.project.memoapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class NoteActivity : AppCompatActivity() {

    private lateinit var saveNoteBtn : Button

    private lateinit var title : TextView

    private lateinit var content : EditText

    private lateinit var createdDate : TextView
    private lateinit var editedText : TextView
    private lateinit var checkIfSharedText : TextView

    val db = Firebase.firestore

    private var documentSnapshot: DocumentSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note)

        title = findViewById(R.id.memo_title)
        createdDate = findViewById(R.id.created_date_text)
        editedText = findViewById(R.id.edited_date_text)
        checkIfSharedText = findViewById(R.id.check_shared_text)
        content = findViewById<EditText>(R.id.notes_content_text)

        saveNoteBtn = findViewById(R.id.save_note_btn)

        saveNoteBtn.setOnClickListener{
            //view -> startActivity(Intent(this, MainActivity::class.java)).apply {  }

            /*
            val docData = hashMapOf(
                "title" to title.text.toString(),
                "content" to content.text.toString(),
            )
            */
            val updates = hashMapOf<String, Any>(
                "title" to title.text.toString(),
                "content" to content.text.toString()
            )

            val tempReference = documentSnapshot!!.reference

            tempReference.update(updates)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    onBackPressed()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            /*
            db.collection("data").document(title.text.toString())
                .set(docData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    startActivity(intent)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            */
        }
        //getFirebaseData()

        initializeNote()
    }

    private fun initializeNote()
    {
        val userManager = UserManager.getInstance()
        val tempID = userManager.getCurrentDocumentID()
        val tempDocuments = userManager.getDocumentSnapshotList()
        val tempSnapshot = userManager.getCurrentSnapshot()

        if (tempDocuments != null) {
            for (documentSnapshot in tempDocuments) {
                if (documentSnapshot.id == tempID) {
                    // Found the document with the matching ID
                    this.documentSnapshot = documentSnapshot
                    break // Break out of the loop since we found the document
                }
            }
        }

/*
        for (documentSnapshot in tempSnapshot!!.documents) {
            if (documentSnapshot.id == tempID) {
                // Found the document with the matching ID
                this.documentSnapshot = documentSnapshot
                break // Break out of the loop since we found the document
            }
        }
*/
        title.text = documentSnapshot!!.getString("title")
        content.setText(documentSnapshot!!.getString("content"))
    }

    private fun getFirebaseData() {
        db.collection("memos")
            .get()
            .addOnSuccessListener { result ->
                val data: MutableList<MemoData> = mutableListOf()

                for (document in result) {
                    // Käsittele dokumentti ja hae halutut tiedot
                    val title = document.getString("title") ?: ""
                    val content = document.getString("content") ?: ""
                    var sharedWith = arrayListOf("")
                    val tempData = document.get("sharedWith")
                    if(tempData != null){
                        Log.d(ContentValues.TAG, "sharedWith content: " + tempData.toString())
                        var x = tempData as ArrayList<String>
                        for(i in x)
                        {
                            sharedWith.add(i)
                        }
                    }

                    val createdBy = document.getString("createdBy") ?: ""

                    val creationTime = if (document.contains("creationTime")) {
                        document.getLong("creationTime") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }

                    val lastEdited = if (document.contains("lastEdited")) {
                        document.getLong("lastEdited") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }

                    val memo = MemoData(title, content, sharedWith, creationTime, lastEdited)
                    data.add(memo)
                }
            }
    }
}