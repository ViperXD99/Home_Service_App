package lk.nibm.hireupapp.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.common.HardwareProductsDataManager
import lk.nibm.hireupapp.databinding.ActivityProductDetailsBinding
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
            incrementSoldItemCountByOne()
        }
    }

    private fun clickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
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

    private fun incrementSoldItemCountByOne() {

    }

    private fun updateQuantityText() {
        binding.qty.text = currentQuantity.toString()
    }

}