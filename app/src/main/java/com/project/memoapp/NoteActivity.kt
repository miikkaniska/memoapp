package com.project.memoapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteActivity : AppCompatActivity() {

    private lateinit var saveNoteBtn : Button
    private lateinit var deleteBtn : Button

    private lateinit var title : TextView

    private lateinit var content : EditText

    private lateinit var createdDate : TextView
    private lateinit var editedDate : TextView
    private lateinit var checkIfSharedText : TextView

    val db = Firebase.firestore

    private var documentSnapshot: DocumentSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note)

        title = findViewById(R.id.memo_title)
        createdDate = findViewById(R.id.created_date_text)
        editedDate = findViewById(R.id.edited_date_text)
        checkIfSharedText = findViewById(R.id.check_shared_text)
        content = findViewById<EditText>(R.id.notes_content_text)

        saveNoteBtn = findViewById(R.id.save_note_btn)
        deleteBtn = findViewById(R.id.delete_btn)

        deleteBtn.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this note")
                .setPositiveButton("Yes") { _, _ ->
                    // User confirmed, so allow the default behavior
                    deleteNote()
                }
                .setNegativeButton("No") { dialog, _ ->
                    // User canceled, so dismiss the dialog
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }

        saveNoteBtn.setOnClickListener{
            val updates = hashMapOf<String, Any>(
                "title" to title.text.toString(),
                "content" to content.text.toString()
            )

            val tempReference = documentSnapshot!!.reference

            tempReference.update(updates)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    Log.d(TAG, "Updated Values - Title: ${title.text}, Content: ${content.text}")
                    onBackPressed()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
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
                    if(userManager.getUsername() == documentSnapshot.getString("owner"))
                    {
                        deleteBtn.visibility = View.VISIBLE
                    }
                    else
                    {
                        deleteBtn.visibility = View.INVISIBLE
                    }
                    break // Break out of the loop since we found the document
                }
            }
        }

        title.text = documentSnapshot!!.getString("title")
        content.setText(documentSnapshot!!.getString("content"))

        val shareList = documentSnapshot!!.get("sharedWith") as? List<String>

        if (shareList != null && shareList.isNotEmpty()) {
            val arrayData = shareList.joinToString(", ") // Join array elements into a string
            checkIfSharedText.text = "Shared with: $arrayData"
        } else {
            checkIfSharedText.text = "Private"
        }

        val creationTime = documentSnapshot!!.getLong("creationTime")
        //val lastEdited = documentSnapshot!!.getLong("lastEdited")

        if (creationTime != null) {
            val formattedTime = formatTime(creationTime)
            createdDate.text = "Created: $formattedTime"
        } else {
            createdDate.text = "Time: Unknown"
        }
    }
    private fun formatTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy ", Locale.getDefault())
        val date = Date(timeInMillis)
        return dateFormat.format(date)
    }

    override fun onBackPressed() {
        // Show a custom dialog or perform some other action
        val intent = Intent(this, FirstActivity::class.java)
        finish()
        startActivity(intent)

        // If you want to call the default behavior after your custom logic, uncomment the next line
        // super.onBackPressed()
    }

    private fun deleteNote() {
        val tempReference = documentSnapshot!!.reference

        tempReference.delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                onBackPressed()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}