package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.databinding.ActivityHomeBinding
import lk.nibm.hireupapp.fragments.BookingFragment
import lk.nibm.hireupapp.fragments.ChatSpFragment
import lk.nibm.hireupapp.fragments.HomeFragment
import lk.nibm.hireupapp.fragments.OrdersFragment
import lk.nibm.hireupapp.fragments.ProfileFragment
import lk.nibm.hireupapp.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private val orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        initializeComponents()
        fetchOrders()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_btn -> replaceFragment(HomeFragment())
                R.id.profile_btn -> replaceFragment(ProfileFragment())
                R.id.bookings_btn -> replaceFragment(BookingFragment())
                R.id.orders_btn -> replaceFragment(OrdersFragment())
                R.id.chats_btn -> replaceFragment(ChatSpFragment())
            }
            true
        }
    }

    private fun fetchOrders() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val ordersReference = firebaseDatabase.getReference("Orders")
        val dateFormat = SimpleDateFormat("MMM, dd, yyyy", Locale.getDefault())
        val systemDate = Date(System.currentTimeMillis())
        ordersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (orderSnapshot in dataSnapshot.children) {
//                    orderList.clear()
                    // Retrieve the Order object from the snapshot
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null && order.status == "Accepted" && order.bookingDate == dateFormat.format(systemDate).toString() ) {
                        // Update the order status to "In Progress"
                        Toast.makeText(this@Home, "Matched", Toast.LENGTH_SHORT).show()
                        orderSnapshot.ref.child("status").setValue("In Progress")
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    // Order status updated successfully
//                                    // You can perform any additional actions here if needed
//                                } else {
//                                    // Handle the error if update fails
//                                }
//                            }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error if any
            }
        })
    }

    private fun updateOrders(orderList: MutableList<Order>) {
        TODO("Not yet implemented")
    }

    private fun initializeComponents() {

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}