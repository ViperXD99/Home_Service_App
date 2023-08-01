package lk.nibm.hireupapp.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.adapter.CartItemAdapter
import lk.nibm.hireupapp.databinding.ActivityCartBinding
import lk.nibm.hireupapp.model.CartItem

class Cart : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter
    private val cartItems: MutableList<CartItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView and its adapter
        cartItemAdapter = CartItemAdapter(cartItems)
        binding.CartItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Cart)
            adapter = cartItemAdapter
        }

        // Load cart data of the current user
        loadCartData()


    }

    private fun loadCartData() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val databaseReference = FirebaseDatabase.getInstance().reference
            val cartReference = databaseReference.child("Shop").child("Cart").child(currentUser.uid)

            // Add a ValueEventListener to read cart data from Firebase
            cartReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Clear the previous cart data
                    cartItems.clear()

                    // Loop through each cart item in the snapshot and add it to the list
                    for (itemSnapshot in snapshot.children) {
                        val cartItem = itemSnapshot.getValue(CartItem::class.java)
                        cartItem?.let {
                            cartItems.add(it)
                        }
                    }



                    // Calculate the total value of items and update the totalPrice TextView
                    val totalPrice = calculateTotalPrice(cartItems)
                    binding.totalPrice.text = "Rs. $totalPrice" // Update the TextView

                    // Notify the adapter that the data has changed and update the RecyclerView
                    cartItemAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any database errors here
                }
            })
        }
    }

    private fun calculateTotalPrice(cartItems: List<CartItem>): Double {
        var totalPrice = 0.0
        for (cartItem in cartItems) {
            val productPrice = cartItem.productPrice.toDoubleOrNull() ?: 0.0
            totalPrice += productPrice * cartItem.quantity
        }
        return totalPrice
    }
}
