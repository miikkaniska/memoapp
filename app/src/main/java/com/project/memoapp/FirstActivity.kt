package com.project.memoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import MyAdapter
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot

class FirstActivity : AppCompatActivity() {


    private val memoList = mutableListOf<MemoData>()
    private val db = Firebase.firestore
    private val currentUsername = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    private lateinit var newMemoButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(memoList) // Muutetaan adapteri käyttämään Firestoresta haettua listaa
        recyclerView.adapter = adapter

        //val username = intent.getStringExtra("USERNAME_EXTRA")

        val username = UserManager.getInstance().getUsername()

        if (username != null) {
            // Do something with the username, e.g., display it in a TextView
            //Toast.makeText(this, "Hello, " + username, Toast.LENGTH_SHORT).show()
        } else {
            // Handle the case where the username is not found
            // This may happen if the intent doesn't have the expected extra
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity or take appropriate action
        }

        newMemoButton = findViewById(R.id.newMemoButton)

        newMemoButton.setOnClickListener {
            // Actions to perform when the button is clicked
            // For example, you can navigate to a new activity

            // Or you can perform other tasks based on your application logic
            // For instance, displaying a Toast message
            //Toast.makeText(this, "New Memo Button Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CreateMemoActivity::class.java)
            finish()
            startActivity(intent)
            // Or any other custom logic you want to execute
        }
        getUserMemos()
    }

    private fun fetchDataFromFirestore() {

        db.collectionGroup("memos")
            .get()
            .addOnSuccessListener { result ->
                val data: MutableList<MemoData> = mutableListOf()

                UserManager.getInstance().setCurrentSnapshot(result)

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

                    val creationTime = if (document.contains("creationTime")) {
                        document.getLong("creationTime") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }

                    val lastEdited = if(document.contains("lastEdited")) {
                        document.getLong("lastEdited") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }
                    val memo = MemoData(title, content, sharedWith/*CHANGE TO sharedWith when reading data is confirmed to be working*/, creationTime, lastEdited)
                    data.add(memo)
                }
                // Päivitä RecyclerView adapterin avulla
                adapter = MyAdapter(data)
                recyclerView.adapter = adapter
            }
    }

    private fun getUserMemos()
    {
        val currentUserName = UserManager.getInstance().getUsername().toString()
        // Query for documents where "owner" equals currentUserName
        val ownerQuery = db.collectionGroup("userMemos")
            .whereEqualTo("owner", currentUserName)

        // Query for documents where "sharedWith" equals currentUserName
        val sharedWithQuery = db.collectionGroup("userMemos")
            .whereArrayContains("sharedWith", currentUserName)

        val combinedList = ArrayList<DocumentSnapshot>()

        ownerQuery.get().addOnSuccessListener { ownerQuerySnapshot ->
            combinedList.addAll(ownerQuerySnapshot.documents)

            sharedWithQuery.get().addOnSuccessListener { sharedWithQuerySnapshot ->
                combinedList.addAll(sharedWithQuerySnapshot.documents)

                Log.w("DOCUMENT AMOUNT", "CombinedList size: ${combinedList.size}")
                val data: MutableList<MemoData> = mutableListOf()
                // Process the combinedList containing documents from both queries
                for (document in combinedList) {
                    val docData = document.data
                    // Process the data as needed
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

                    val creationTime = if (document.contains("creationTime")) {
                        document.getLong("creationTime") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }

                    val lastEdited = if(document.contains("lastEdited")) {
                        document.getLong("lastEdited") ?: System.currentTimeMillis()
                    } else {
                        System.currentTimeMillis() // Tai anna oletusaika, jos creationTime-kenttä ei ole saatavilla
                    }
                    val memo = MemoData(title, content, sharedWith, creationTime, lastEdited)
                    data.add(memo)

                }
                UserManager.getInstance().setDocumentSnapshotList(combinedList)
                adapter = MyAdapter(data)
                recyclerView.adapter = adapter
            }.addOnFailureListener { exception ->
                Log.e("Query Error", "Querying for sharedWith data failed!")
                // Handle errors for sharedWithQuery
            }
        }.addOnFailureListener { exception ->
            // Handle errors for ownerQuery
            Log.e("Query Error", "Querying for owner data failed!")
        }
    }
}