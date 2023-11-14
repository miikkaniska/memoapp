package com.project.memoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.project.memoapp.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        recyclerView = binding.recyclerView
        val data: List<String> = mutableListOf()
        adapter = MyAdapter(data)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        db.collection("memos")
            .get()
            .addOnSuccessListener { result ->
                val data: MutableList<String> = mutableListOf()

                for (document in result) {
                    // Käsittele dokumentti ja hae halutut tiedot
                    val formattedTime = document.getLong("creationTime")
                    val email = document.getString("email")
                    val nameOfMemo = document.getString("nameOfMemo")
                    val nickname = document.getString("nickname")
                    val shared = document.getBoolean("shared")

                    // Luo merkkijono tiedoista ja lisää se datalistaan
                    val itemString = "Creation Time: $formattedTime\nEmail: $email\nMemo: $nameOfMemo\nName: $nickname\nShared: $shared"
                    data.add(itemString)
                }

                // Päivitä RecyclerView adapterin avulla
                adapter = MyAdapter(data)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Käsittely virhetilanteessa
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}