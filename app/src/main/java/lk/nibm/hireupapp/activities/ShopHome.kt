package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.HardwareAdapter
import lk.nibm.hireupapp.adapter.HardwareCategoriesAdapter
import lk.nibm.hireupapp.adapter.HardwareProductsAdapter
import lk.nibm.hireupapp.model.Hardware
import lk.nibm.hireupapp.model.HardwareCategoriesData
import lk.nibm.hireupapp.model.HardwareProductsData

class ShopHome : AppCompatActivity() {
    private lateinit var hardwarRecyclerView: RecyclerView
    private lateinit var hardwarCategoryRecyclerView: RecyclerView
    private lateinit var itemRecyclerView : RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var HardwareAdapter: HardwareAdapter
    private lateinit var HardwareCategoryAdapter: HardwareCategoriesAdapter
    private lateinit var hardwareProductsAdapter: HardwareProductsAdapter
    private val hardwareItemList = mutableListOf<Hardware>()
    private val hardwareCategoryItemList = mutableListOf<HardwareCategoriesData>()
    private val hardwareProducts = mutableListOf<HardwareProductsData>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var seeAllHardware: TextView
    private lateinit var seeAllHardwareCategories: TextView
    private lateinit var backArrow :ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_home)

        initializeComponents()
        hardwareRecyclerView()
        hardwareCategoryRecyclerView()
        itemRecyclerView()
        clickListeners()

    }

    private fun itemRecyclerView() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("item")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                hardwareProducts.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(HardwareProductsData::class.java)
                    item?.let { hardwareProducts.add(it) }
                    hardwareProducts.size
                }
                hardwareProducts.shuffle()
                itemRecyclerView = findViewById(R.id.itemRecyclerView)
                itemRecyclerView.layoutManager = GridLayoutManager(this@ShopHome,2)
                hardwareProductsAdapter = HardwareProductsAdapter(hardwareProducts)
                itemRecyclerView.adapter = hardwareProductsAdapter
                hardwareProductsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun clickListeners() {
        seeAllHardware.setOnClickListener {
            val intent = Intent(this,AllHardwares::class.java)
            startActivity(intent)
        }
        seeAllHardwareCategories.setOnClickListener {
            val intent = Intent(this,HardwareCategories::class.java)
            startActivity(intent)
        }
        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeComponents() {

        seeAllHardware = findViewById(R.id.hardwareSeeAll)
        seeAllHardwareCategories = findViewById(R.id.hardwareCategorySeeAll)
        backArrow = findViewById(R.id.backArrow)
    }



    private fun hardwareRecyclerView() {
        hardwarRecyclerView = findViewById(R.id.hardwareRecyclerView)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hardwarRecyclerView.layoutManager = layoutManager
        HardwareAdapter = HardwareAdapter(hardwareItemList)
        hardwarRecyclerView.adapter = HardwareAdapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Hardware")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                hardwareItemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Hardware::class.java)
                    item?.let { hardwareItemList.add(it) }
                }
                HardwareAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
            }
        })
    }



 private fun hardwareCategoryRecyclerView() {
        hardwarCategoryRecyclerView = findViewById(R.id.hardwareCategoryRecyclerView)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hardwarCategoryRecyclerView.layoutManager = layoutManager
     HardwareCategoryAdapter = HardwareCategoriesAdapter(hardwareCategoryItemList)
        hardwarCategoryRecyclerView.adapter = HardwareCategoryAdapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Category")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                hardwareCategoryItemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(HardwareCategoriesData::class.java)
                    item?.let { hardwareCategoryItemList.add(it) }
                }
                HardwareCategoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
            }
        })
    }
}
