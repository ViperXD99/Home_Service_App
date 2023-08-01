package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.HardwareCategoriesNamesAdapter
import lk.nibm.hireupapp.adapter.HardwareProductsAdapter
import lk.nibm.hireupapp.common.HardwareCategoriesDataManager
import lk.nibm.hireupapp.common.HardwareDataManager
import lk.nibm.hireupapp.common.ShopDataManager
import lk.nibm.hireupapp.databinding.ActivityInsideHardwareBinding
import lk.nibm.hireupapp.model.Hardware
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
    private lateinit var binding : ActivityInsideHardwareBinding
    private  var shopDetails : Hardware? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsideHardwareBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        shopDetails = ShopDataManager.getShop()
        initializeComponents()
        loadTopScreen()
        loadHardwareCategoriesRecyclerView()
        loadHardwareProductsRecyclerView()
    }

    private fun loadTopScreen() {
        binding.hardwaresName.text = shopDetails?.name
        binding.hardwaresAddress.text = shopDetails?.address +"," + shopDetails?.city + "," + shopDetails?.district
        Glide.with(this)
            .load(shopDetails?.image)
            .into(binding.imgShop)
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
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("item")
        val query: Query = databaseReference.orderByChild("hardwareID").equalTo(shopDetails?.id.toString())
        //val query: Query = databaseReference.orderByChild("hardwareID").equalTo(id)
        query.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                HardwareProducts.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(HardwareProductsData::class.java)
                    item?.let { HardwareProducts.add(it) }
                    HardwareProducts.size
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