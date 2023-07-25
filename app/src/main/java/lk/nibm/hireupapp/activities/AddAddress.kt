package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import lk.nibm.hireupapp.R
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import lk.nibm.hireupapp.model.AddressDataClass

class AddAddress : AppCompatActivity() {

    private lateinit var fullNameEditTxt: EditText
    private lateinit var contactNumberEditTxt: EditText
    private lateinit var provinceEditTxt: EditText
    private lateinit var cityEditTxt: EditText
    private lateinit var addressEditTxt: EditText
    private lateinit var addAddressButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var userAddressRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        fullNameEditTxt = findViewById(R.id.full_name_edit_txt)
        contactNumberEditTxt = findViewById(R.id.contact_number_edit_txt)
        provinceEditTxt = findViewById(R.id.province_edit_txt)
        cityEditTxt = findViewById(R.id.city_edit_txt)
        addressEditTxt = findViewById(R.id.address_edit_txt)
        addAddressButton = findViewById(R.id.add_address_button)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        user = firebaseAuth.currentUser!!
        userAddressRef = database.reference.child("Users").child(user.uid).child("address")

        addAddressButton.setOnClickListener {
            addNewAddress()
        }
    }

    private fun addNewAddress() {
        val fullName = fullNameEditTxt.text.toString()
        val contactNumber = contactNumberEditTxt.text.toString()
        val province = provinceEditTxt.text.toString()
        val city = cityEditTxt.text.toString()
        val address = addressEditTxt.text.toString()

        val addressId = userAddressRef.push().key
        if (addressId != null) {
            val newAddressRef = userAddressRef.child(addressId)
            val addressData = AddressDataClass(fullName, contactNumber, province, city, address, addressId)
            newAddressRef.setValue(addressData)
                .addOnSuccessListener {
                    showToast("New address added successfully!")
                    val intent = Intent(this@AddAddress, ViewAddress::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    showToast("Failed to add new address.")
                }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


