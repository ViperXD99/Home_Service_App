package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.HardwareCategoriesRecyclerViewAdapter
import lk.nibm.hireupapp.model.HardwareCategoriesData


class HardwareCategories : AppCompatActivity() {
    private lateinit var hardwares: RecyclerView
    private lateinit var adapter: HardwareCategoriesRecyclerViewAdapter
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var databaseReference: DatabaseReference
    private val itemList = mutableListOf<HardwareCategoriesData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hardware_categories)
        initializeComponents()
        loadServiceCategories()
    }

    private fun loadServiceCategories() {
        adapter = HardwareCategoriesRecyclerViewAdapter(this, itemList)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(this, 3)
        hardwares.layoutManager = layoutManager
        hardwares.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Category")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children){
                    val item = itemSnapshot.getValue(HardwareCategoriesData::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun initializeComponents() {
        hardwares = findViewById(R.id.hardwares)
    }
}