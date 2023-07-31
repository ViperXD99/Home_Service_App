package lk.nibm.hireupapp.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.AppPreferences
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.databinding.ActivityHomeBinding
import lk.nibm.hireupapp.fragments.BookingFragment
import lk.nibm.hireupapp.fragments.ChatSpFragment
import lk.nibm.hireupapp.fragments.HomeFragment
import lk.nibm.hireupapp.fragments.OrdersFragment
import lk.nibm.hireupapp.fragments.ProfileFragment
import lk.nibm.hireupapp.model.Order
import lk.nibm.hireupapp.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var appPreferences: AppPreferences
    private lateinit var database: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        appPreferences = AppPreferences(this)
        setContentView(binding.root)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        checkSignedIn()

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

    private fun checkSignedIn() {
        if (appPreferences.isUserLoggedIn()) {
            // User is already signed in, continue with the home screen logic
            // Retrieve the user ID or token if needed
            val userId = appPreferences.getUserId().toString()
            database = FirebaseDatabase.getInstance()
            usersReference = database.reference.child("Users").child(userId)
            usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        user?.let {
                            UserDataManager.setUser(it)
                            replaceFragment(HomeFragment())
                        }
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@Home, "Failed to load Data!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@Home,
                        "Failed to retrieve user data: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            // Your home screen logic here
        } else {
            // User is not signed in, redirect to the sign-in screen
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish() // Optional: Finish the home activity to prevent the user from going back
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
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}