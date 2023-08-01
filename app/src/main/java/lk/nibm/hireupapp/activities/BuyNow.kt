package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.HardwareProductsDataManager
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.databinding.ActivityBuyNowBinding
import lk.nibm.hireupapp.databinding.ActivityProductDetailsBinding
import lk.nibm.hireupapp.model.AddressDataClass
import lk.nibm.hireupapp.model.Hardware
import lk.nibm.hireupapp.model.HardwareProductsData
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.ShopOrderItems
import lk.nibm.hireupapp.model.ShopOrders
import lk.nibm.hireupapp.model.User

class BuyNow : AppCompatActivity() {
    private lateinit var binding: ActivityBuyNowBinding
    private lateinit var productData: HardwareProductsData
    private lateinit var userData: User
    private lateinit var database: FirebaseDatabase
    private lateinit var userAddressRef: DatabaseReference
    private lateinit var hardwareRef: DatabaseReference
    private lateinit var orderRef: DatabaseReference
    private val addressList = mutableListOf<AddressDataClass>()
    private val shopOrderItemList = mutableListOf<ShopOrderItems>()
    private lateinit var userAddressID : String
    private lateinit var dialog: Dialog
    private lateinit var hardware : Hardware
    private var isAddressDataLoaded = false
    private var isProductDataLoaded = false
    private var isHardwareDataLoaded = false
    private var productQty : Int = 1
    private var totalPrice : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyNowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        productData = HardwareProductsDataManager.getHardwareProduct()!!
        userData = UserDataManager.getUser()!!
        database = FirebaseDatabase.getInstance()
        getUserAddress()
        checkRadioButtons()
        getHardwareDetails()
        loadProductDetails()
        clickListeners()
    }

    private fun clickListeners() {

        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()

        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.changeAddressTextView.setOnClickListener {
            showAddressSelectionDialog()
        }
    }

    private fun showAddressSelectionDialog() {

        val addressDialog = AlertDialog.Builder(this)
            .setTitle("Select Address")
            .setSingleChoiceItems(getAddressList(), -1) { dialog, which ->
                val selectedAddress = addressList[which]
                updateDisplayedAddress(selectedAddress)
                dialog.dismiss()
            }
            .create()
        addressDialog.show()
    }



    private fun getAddressList(): Array<String> {
        // Create a list of address strings to be displayed in the dialog
        val addressStrings = mutableListOf<String>()
        for (address in addressList) {
            addressStrings.add(address.fullName + ", " + address.address + ", " + address.city + ", " + address.province)
        }
        return addressStrings.toTypedArray()
    }

    private fun updateDisplayedAddress(selectedAddress: AddressDataClass) {
        // Update the address displayed in the BuyNow activity with the selected address
        binding.txtAddress.text = selectedAddress.address
        binding.txtAddressCity.text = selectedAddress.city
        binding.txtAddressDistrict.text = selectedAddress.province
        binding.txtAddressName.text = selectedAddress.fullName
        binding.txtAddressContact.text = selectedAddress.contactNumber
    }

    private fun placeOrder() {
        if (binding.radioBtnCash.isChecked){
            MaterialAlertDialogBuilder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Yes") { dialog, _ ->
                    val paymentOption = "Cash"
                    val orderStatus = "Pending"
                    orderRef = database.getReference("Shop").child("Shop Orders")
                    val shopOrderId = orderRef.push().key

                    val shopOrderItem = ShopOrderItems(productData.id,productQty,totalPrice)
                    shopOrderItemList.clear()
                    shopOrderItemList.add(shopOrderItem)

                    if (shopOrderId != null) {
                        val newOrderRef = orderRef.child(shopOrderId)
                        val shopOrder = ShopOrders(shopOrderId,userData.userId,productData.hardwareID,totalPrice,orderStatus,paymentOption,shopOrderItemList)
                        newOrderRef.setValue(shopOrder)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Your order has been placed successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to place order!", Toast.LENGTH_SHORT).show()
                            }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    // Add the action to be performed when the user clicks "No" or presses the back button.
                    // For example, you can just dismiss the dialog without performing any action.
                    dialog.dismiss()
                }
//                .setPositiveButtonStyle(positiveButtonStyle) // Set the custom style to the positive button
                .show()
            finish()
        }



    }

    @SuppressLint("SetTextI18n")
    private fun loadProductDetails() {
        val extras = intent.extras
        if (extras != null) {
            productQty = extras.getInt("PRODUCT_QTY")
        }
        binding.txtProductName.text = productData.name
        binding.txtProductDescription.text = productData.description
        binding.txtProductPrice.text = "Rs. " + productData.price
        binding.txtQuantity.text = productQty.toString()
        totalPrice = (productQty * productData.price!!.toDouble())
        val formattedPrice = String.format("%.2f", totalPrice)
        binding.txtTotalAmount.text = "Rs. " + formattedPrice
        binding.txtTotalPrice.text = "Rs. " + formattedPrice
        Glide.with(this)
            .load(productData.productImage)
            .into(binding.imgProduct)
        isProductDataLoaded = true
        onDataLoaded()
    }

    private fun getHardwareDetails() {
        hardwareRef = database.reference.child("Shop").child("Hardware").child(productData.hardwareID!!)
        hardwareRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    hardware = dataSnapshot.getValue(Hardware::class.java)!!
                    binding.txtHardwareName.text = hardware.name
                    isHardwareDataLoaded = true
                    onDataLoaded()
                } else {
                    Toast.makeText(applicationContext, "There is no such hardware", Toast.LENGTH_SHORT).show()
                    isHardwareDataLoaded = true
                    onDataLoaded()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "check database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkRadioButtons() {
        binding.radioBtnCash.setOnClickListener {
            binding.radioBtnCard.isChecked = false
        }
        binding.radioBtnCard.setOnClickListener {
            binding.radioBtnCash.isChecked = false
        }
    }

    private fun getUserAddress() {
        userAddressRef = database.reference.child("Users").child(userData.userId!!).child("address")
        userAddressRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (addressSnapshot in dataSnapshot.children) {
                    val address = addressSnapshot.getValue(AddressDataClass::class.java)
                    address?.let {
                        addressList.add(it)
                    }
                }
                if (addressList.isNotEmpty()) {
                    val firstAddress = addressList[1]
                    binding.txtAddress.text = firstAddress.address
                    binding.txtAddressCity.text = firstAddress.city
                    binding.txtAddressDistrict.text = firstAddress.province
                    binding.txtAddressName.text = firstAddress.fullName
                    binding.txtAddressContact.text = firstAddress.contactNumber
                }
                isAddressDataLoaded = true
                onDataLoaded()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun onDataLoaded() {
        if (isAddressDataLoaded && isHardwareDataLoaded && isProductDataLoaded) {
            dialog.dismiss() // Hide the progress dialog when all data is loaded


        }
    }
}