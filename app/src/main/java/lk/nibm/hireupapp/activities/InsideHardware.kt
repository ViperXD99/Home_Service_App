package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.HardwareCategoriesNamesAdapter
import lk.nibm.hireupapp.adapter.HardwareProductsAdapter
import lk.nibm.hireupapp.common.HardwareCategoriesDataManager
import lk.nibm.hireupapp.common.HardwareDataManager
import lk.nibm.hireupapp.model.HardwareCategoriesData
import lk.nibm.hireupapp.model.HardwareProductsData

class InsideHardware : AppCompatActivity() {
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var productsRecyclerView:RecyclerView
    private lateinit var id: String
    private lateinit var HardwareCategoriesNamesAdapter: HardwareCategoriesNamesAdapter
    private lateinit var HardwareProductsAdapter:HardwareProductsAdapter
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var databaseReference: DatabaseReference
    private val categoryNames = mutableListOf<HardwareCategoriesData>()
    private val HardwareProducts = mutableListOf<HardwareProductsData>()
    private lateinit var hardwareCategoryName :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_hardware)

        initializeComponents()
        loadTopScreen()
        loadHardwareCategoriesRecyclerView()
        loadHardwareProductsRecyclerView()
    }

    private fun loadTopScreen() {
        val extras = intent.extras
        if (extras != null) {
            hardwareCategoryName.text = extras.getString("HARDWARE_NAME")
            id = extras.getString("HARDWARE_ID").toString()
            HardwareDataManager.name = extras.getString("HARDWARE_NAME").toString()
            HardwareDataManager.id = extras.getString("HARDWARE_ID").toString()
            //HardwareCategoriesDataManager.name = extras.getString("HARDWARE_CATEGORY_NAME").toString()
            //HardwareCategoriesDataManager.id = extras.getString("HARDWARE_CATEGORY_ID").toString()
        }
    }

    private fun loadHardwareCategoriesRecyclerView() {


        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView)
        // layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        HardwareCategoriesNamesAdapter = HardwareCategoriesNamesAdapter(categoryNames)
        categoriesRecyclerView.adapter = HardwareCategoriesNamesAdapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Category")
        //val query: Query = databaseReference.orderByChild("hardwareID").equalTo(id)
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryNames.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(HardwareCategoriesData::class.java)
                    item?.let { categoryNames.add(it) }
                }
                HardwareCategoriesNamesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadHardwareProductsRecyclerView() {

        productsRecyclerView = findViewById(R.id.itemRecyclerView)
        // layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        productsRecyclerView.layoutManager = GridLayoutManager(this,2)
        HardwareProductsAdapter = HardwareProductsAdapter(HardwareProducts)
        productsRecyclerView.adapter = HardwareProductsAdapter
        //databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Category")
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Hardware").child(id).child("Product")
        //val query: Query = databaseReference.orderByChild("hardwareID").equalTo(id)
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                HardwareProducts.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(HardwareProductsData::class.java)
                    item?.let { HardwareProducts.add(it) }
                }
                HardwareProductsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun handleCategoryClick(categoryName: String, categoryId: String?) {
        // Handle the click event here as needed
        // You can access the categoryName and categoryId parameters
        // that were passed from the adapter's onClickListener
    }




    private fun initializeComponents() {
        hardwareCategoryName = findViewById(R.id.hardwares_name)
    }
}