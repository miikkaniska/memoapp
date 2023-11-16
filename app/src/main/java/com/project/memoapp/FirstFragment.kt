package com.project.memoapp

import MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.memoapp.databinding.FragmentFirstBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private val memoList = mutableListOf<MemoData>()
    private val db = Firebase.firestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        adapter = MyAdapter(memoList) // Muutetaan adapteri käyttämään Firestoresta haettua listaa
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchDataFromFirestore() // Kutsutaan funktiota, joka hakee tiedot Firestoresta
    }

    private fun fetchDataFromFirestore() {
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
                // Päivitä RecyclerView adapterin avulla
                adapter = MyAdapter(data)
                recyclerView.adapter = adapter
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}