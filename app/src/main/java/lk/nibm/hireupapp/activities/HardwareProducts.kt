package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.ProductAdapter
import lk.nibm.hireupapp.model.HardwareProductsData


class HardwareProducts : AppCompatActivity() {

    private lateinit var productsRecyclerView : RecyclerView
    private lateinit var adapter : ProductAdapter
    private lateinit var hardwareCategoryName : TextView
    private var productList = mutableListOf<HardwareProductsData>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var id : String
    private lateinit var btnBack : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hardware_products)
        initializeComponents()
        setData()
        loadProducts()
        clickListeners()
    }

    private fun clickListeners() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadProducts(){
        adapter = ProductAdapter(productList)
        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        productsRecyclerView.layoutManager = layoutManager
        productsRecyclerView.adapter = adapter

        val categoryId = intent.getStringExtra("HARDWARE_CATEGORY_ID")

        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("item")
        val query: Query = databaseReference.orderByChild("categoryID").equalTo(categoryId)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (itemSnapshot in snapshot.children){
                    val item = itemSnapshot.getValue(HardwareProductsData::class.java)
                    item?.let { productList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
            }
        })

    }

    private fun setData(){
        val categoryName = intent.getStringExtra("HARDWARE_CATEGORY_NAME")
        hardwareCategoryName.text = categoryName
    }

    private fun initializeComponents() {
        productsRecyclerView = findViewById(R.id.hardware_products_recycler_view)
        hardwareCategoryName = findViewById(R.id.hardware_category_name)
        btnBack = findViewById(R.id.btnBack)
    }

}