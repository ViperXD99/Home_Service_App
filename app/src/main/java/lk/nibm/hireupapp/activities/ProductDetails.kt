package lk.nibm.hireupapp.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.HardwareProductsDataManager
import lk.nibm.hireupapp.common.ServiceProviderDataManager

class ProductDetails : AppCompatActivity() {
    private lateinit var productImage : ImageView
    private lateinit var productPrice : TextView
    private lateinit var productName : TextView
    private lateinit var productDescription : TextView
    private lateinit var productRate : TextView
    private lateinit var productSold : TextView
    private lateinit var buttonBuy: Button
    private lateinit var quantityTextView: TextView
    private lateinit var qtyMinusButton: CardView
    private lateinit var qtyAddButton: CardView
    private var currentQuantity: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        val productData = HardwareProductsDataManager.getHardwareProduct()
        initializeComponents()
        setupQuantity()

        productPrice.text = productData?.price
        productName.text = productData?.name
        productDescription.text = productData?.description
        productRate.text = productData?.rating
        productSold.text = productData?.sold_item_count.toString()

        Glide.with(this)
            .load(productData?.image)
            .into(productImage)

        // Set the click listener for the "Buy" button
        buttonBuy.setOnClickListener {
            incrementSoldItemCountByOne()
        }
    }


    private fun setupQuantity() {
        quantityTextView = findViewById(R.id.qty)
        qtyMinusButton = findViewById(R.id.qty_minus)
        qtyAddButton = findViewById(R.id.qty_add)

        currentQuantity = 1 // Initialize the current quantity

        // Set the initial quantity text
        updateQuantityText()

        qtyMinusButton.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                updateQuantityText()
            }
        }

        qtyAddButton.setOnClickListener {
            currentQuantity++
            updateQuantityText()
        }
    }

    private fun incrementSoldItemCountByOne() {

//data not sending to firebase
        val productData = HardwareProductsDataManager.getHardwareProduct()
        val currentSoldQty = productData?.sold_item_count
        val updatedQty = currentSoldQty?.plus(currentQuantity)
        productData?.sold_item_count = updatedQty
        productSold.text = productData?.sold_item_count.toString()
    }


    private fun updateQuantityText() {
        quantityTextView.text = currentQuantity.toString()
    }

    /*    private fun clickListeners() {
            spBook.setOnClickListener {
                val intent = Intent(this, BookNow::class.java)
                startActivity(intent)
            }
            spCall.setOnClickListener {
                val providerData = ServiceProviderDataManager.getProvider()
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+providerData?.contact)
                startActivity(intent)
            }
            btnBack.setOnClickListener {
                val intent = Intent(this, SP_details::class.java)
                startActivity(intent)
            }
        }*/

    private fun initializeComponents() {
        productPrice = findViewById(R.id.product_price)
        productSold = findViewById(R.id.sold_product_count)
        productRate = findViewById(R.id.product_rating)
        productDescription = findViewById(R.id.product_description)
        productName = findViewById(R.id.product_name)
        productImage = findViewById(R.id.product_image)
        buttonBuy = findViewById(R.id.button_buy)



    }
}