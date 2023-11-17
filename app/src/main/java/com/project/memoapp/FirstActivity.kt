package com.project.memoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.project.memoapp.databinding.FragmentFirstBinding
import MyAdapter
import android.content.ContentValues
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

//testing if changing from fragment to activity fixes problems with changing
class FirstActivity : AppCompatActivity() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private val memoList = mutableListOf<MemoData>()
    private val db = Firebase.firestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(memoList) // Muutetaan adapteri käyttämään Firestoresta haettua listaa
        recyclerView.adapter = adapter

        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
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
}