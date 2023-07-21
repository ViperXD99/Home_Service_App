package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.CategoryAdapter
import lk.nibm.hireupapp.adapter.CategoryRecyclerViewAdapter
import lk.nibm.hireupapp.model.Category

class ServiceCategories : AppCompatActivity() {
    private lateinit var categories : RecyclerView
    private lateinit var adapter: CategoryRecyclerViewAdapter
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var databaseReference: DatabaseReference
    private val itemList = mutableListOf<Category>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_categories)
        initializeComponents()
        loadServiceCategories()
    }

    private fun loadServiceCategories() {
        adapter = CategoryRecyclerViewAdapter(this, itemList)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        categories.layoutManager = layoutManager
        categories.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Service Categories")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children){
                    val item = itemSnapshot.getValue(Category::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
            }
        })
    }

    private fun initializeComponents() {
        categories = findViewById(R.id.categories)
    }
}