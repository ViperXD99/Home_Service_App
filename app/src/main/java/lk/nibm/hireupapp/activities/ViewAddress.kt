package lk.nibm.hireupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lk.nibm.hireupapp.R
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import lk.nibm.hireupapp.adapter.AddressAdapter
import lk.nibm.hireupapp.databinding.ActivityViewAddressBinding
import lk.nibm.hireupapp.model.AddressDataClass

class ViewAddress : AppCompatActivity() {

    private lateinit var binding: ActivityViewAddressBinding
    private lateinit var dataList: ArrayList<AddressDataClass>
    private lateinit var adapter: AddressAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid).child("address")

        val gridLayoutManager = GridLayoutManager(this@ViewAddress, 1)
        binding.addressRecyclerView.layoutManager = gridLayoutManager

        dataList = ArrayList()
        adapter = AddressAdapter(dataList)
        binding.addressRecyclerView.adapter = adapter

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(AddressDataClass::class.java)
                    data?.let {
                        dataList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        }
        databaseReference.addValueEventListener(eventListener as ValueEventListener)

        // Handle addAddressButton click event
        val addAddressButton: Button = findViewById(R.id.addAddressButton)
        addAddressButton.setOnClickListener {
            val intent = Intent(this@ViewAddress, AddAddress::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventListener?.let {
            databaseReference.removeEventListener(it)
        }
    }
}
