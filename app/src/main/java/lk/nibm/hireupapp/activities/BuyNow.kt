package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.HardwareProductsDataManager
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.databinding.ActivityBuyNowBinding
import lk.nibm.hireupapp.databinding.ActivityProductDetailsBinding
import lk.nibm.hireupapp.model.AddressDataClass
import lk.nibm.hireupapp.model.Hardware
import lk.nibm.hireupapp.model.HardwareProductsData
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.User

class BuyNow : AppCompatActivity() {
    private lateinit var binding: ActivityBuyNowBinding
    private lateinit var productData: HardwareProductsData
    private lateinit var userData: User
    private lateinit var database: FirebaseDatabase
    private lateinit var userAddressRef: DatabaseReference
    private lateinit var hardwareRef: DatabaseReference
    private val addressList = mutableListOf<AddressDataClass>()
    private lateinit var userAddressID : String
    private lateinit var dialog: Dialog
    private lateinit var hardware : Hardware
    private var isAddressDataLoaded = false
    private var isProductDataLoaded = false
    private var isHardwareDataLoaded = false
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
        getHardwareDetails()
    }

    private fun getHardwareDetails() {
        hardwareRef = database.reference.child("Shop").child("Hardware").child(productData.hardwareID!!)
        hardwareRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    hardware = dataSnapshot.getValue(Hardware::class.java)!!
                    binding.txtHardwareName.text = hardware.name
                } else {
                    Toast.makeText(applicationContext, "There is no such hardware", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "check database error", Toast.LENGTH_SHORT).show()
            }
        })
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
                    val firstAddress = addressList[0]
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