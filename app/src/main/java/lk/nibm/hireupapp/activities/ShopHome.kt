package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.HardwareAdapter
import lk.nibm.hireupapp.model.Hardware

class ShopHome : AppCompatActivity() {
    private lateinit var hardwarRecyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: HardwareAdapter
    private val itemList = mutableListOf<Hardware>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var seeAllHardware: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_home)
        initializeComponents()
        hardwareRecyclerView()
        clickListeners()
    }

    private fun clickListeners() {
        seeAllHardware.setOnClickListener {
            val intent = Intent(this,AllHardwares::class.java)
            startActivity(intent)
        }
    }

    private fun initializeComponents() {

        seeAllHardware = findViewById(R.id.hardwareSeeAll)
    }



    private fun hardwareRecyclerView() {
        hardwarRecyclerView = findViewById(R.id.hardwareRecyclerView)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hardwarRecyclerView.layoutManager = layoutManager
        adapter = HardwareAdapter(itemList)
        hardwarRecyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Shop").child("Hardware")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Hardware::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
            }
        })
    }
}
