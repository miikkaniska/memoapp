package com.project.memoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.project.memoapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toggleShare.setOnCheckedChangeListener { _, isChecked ->
            //used in testing phase to check swicth state changes, remove later
            Log.d("SwitchState", "Switch state changed to: $isChecked")

            // Update the visibility of textViewNickname based on the switch state
            binding.textviewNickname.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.editTextPerson.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.btnAdd.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.textviewShared.visibility = if (isChecked) View.VISIBLE else View.GONE
            binding.btnAdd.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.btnAdd.setOnClickListener {
            if(binding.editTextPerson.text.isEmpty()){
                binding.editTextPerson.setError("Name can not be empty.")
                Toast.makeText(requireContext(), "Please fill every field.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                binding.textviewShared.visibility = View.VISIBLE
                binding.textviewNicknameList.visibility = View.VISIBLE
                binding.textviewNicknameList.text = "${binding.textviewNicknameList.text}\n${binding.editTextPerson.text}"
            }

        }

        // Määritä painikkeelle toiminnallisuus
        binding.btnConfirm.setOnClickListener {
            // Hae syötteet käyttäjältä
            val nameOfMemo = binding.nameOfMemo.text.toString()
            val nickname = binding.editTextPerson.text.toString()
            val email = binding.editTextEmail.text.toString()
            val shareEnabled = binding.toggleShare.isChecked

            if(nameOfMemo.isEmpty()){
                binding.nameOfMemo.setError("Memo name can not be empty.")
                Toast.makeText(requireContext(), "Memo name can not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Luo Firebase-tietokantaan tallennettava olio
            val memoData = MemoData(nameOfMemo, nickname, email, shareEnabled)

            // Hae "memos" -kokoelma Firestoresta
            val memosCollection = db.collection("memos")

            // Tallenna tietokantaan, jos "Share" on päällä
            if (shareEnabled) {
                memosCollection.add(memoData)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Data tallennettu Firestoreen", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Tallennuksessa tapahtui virhe: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                memosCollection.add(memoData)
                Toast.makeText(requireContext(), "Dataa ei tallennettu, koska 'Share' on pois päältä", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        //binding.buttonSecond.setOnClickListener {
            //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//Tallennettujen tietojen haku firestoresta
//val db = Firebase.firestore
//val memosCollection = db.collection("memos")

//memosCollection.get()
//    .addOnSuccessListener { result ->
//        for (document in result) {
//            // document.id on dokumentin tunniste (voit käyttää sitä tarvittaessa)
//            val memoData = document.toObject(MemoData::class.java)
//            // Käsittele memoData tässä, esim. lisää se listaan
//        }
//    }
//    .addOnFailureListener { exception ->
//        // Käsittely epäonnistumistilanteessa
//    }