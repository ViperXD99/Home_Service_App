package lk.nibm.hireupapp.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import lk.nibm.hireupapp.common.HardwareProductsDataManager
import lk.nibm.hireupapp.databinding.ActivityProductDetailsBinding
import lk.nibm.hireupapp.model.CartItem
import lk.nibm.hireupapp.model.HardwareProductsData

class ProductDetails : AppCompatActivity() {
    private var currentQuantity: Int = 0
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productData: HardwareProductsData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productData = HardwareProductsDataManager.getHardwareProduct()!!
        loadProductDetails()
        setupQuantity()
        clickListeners()

        binding.buttonBuy.setOnClickListener {
            val intent = Intent(this , BuyNow::class.java)
            startActivity(intent)
        }

    }

    private fun clickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.buttonAddToCart.setOnClickListener {
            updateCart()
        }
    }


    private fun updateCart() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val cartItem = productData.productImage?.let {
                CartItem(
                    productData.id!!,
                    productData.name!!,
                    productData.price!!,
                    currentQuantity,
                    it // Include product image URL
                )
            }
            val databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.child("Shop").child("Cart").child(currentUser.uid)
                .push().setValue(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add to cart.", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            // Handle the case when the user is not logged in
            Toast.makeText(this, "Please log in to add to cart.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun loadProductDetails() {
        binding.productDescription.text = productData.description
        binding.productName.text = productData.name
        binding.productPrice.text = productData.price
        Glide.with(this)
            .load(productData.productImage)
            .into(binding.productImage)
    }


    private fun setupQuantity() {
        currentQuantity = 1 // Initialize the current quantity

        // Set the initial quantity text
        updateQuantityText()

        binding.qtyMinus.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                updateQuantityText()
            }
        }

        binding.qtyAdd.setOnClickListener {
            currentQuantity++
            updateQuantityText()
        }
    }

    private fun updateQuantityText() {
        binding.qty.text = currentQuantity.toString()
    }

}