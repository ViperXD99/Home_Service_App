package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lk.nibm.hireupapp.R
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UpdateAddress : AppCompatActivity() {

    private lateinit var fullNameEditTxt: EditText
    private lateinit var contactNumberEditTxt: EditText
    private lateinit var provinceEditTxt: EditText
    private lateinit var cityEditTxt: EditText
    private lateinit var addressEditTxt: EditText
    private lateinit var updateAddressButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var userAddressRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_address)

        fullNameEditTxt = findViewById(R.id.full_name_edit_txt)
        contactNumberEditTxt = findViewById(R.id.contact_number_edit_txt)
        provinceEditTxt = findViewById(R.id.province_edit_txt)
        cityEditTxt = findViewById(R.id.city_edit_txt)
        addressEditTxt = findViewById(R.id.address_edit_txt)
        updateAddressButton = findViewById(R.id.update_address_button)

        val addressId = intent.getStringExtra("addressId")
        if (addressId == null) {
            showToast("Invalid address ID.")
            finish()
        } else {
            firebaseAuth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance()
            user = firebaseAuth.currentUser!!
            userAddressRef = database.reference.child("Users").child(user.uid).child("address").child(addressId)

            updateAddressButton.setOnClickListener {
                updateAddress()
            }

            retrieveAddress()
        }
    }

    private fun updateAddress() {
        val fullName = fullNameEditTxt.text.toString()
        val contactNumber = contactNumberEditTxt.text.toString()
        val province = provinceEditTxt.text.toString()
        val city = cityEditTxt.text.toString()
        val address = addressEditTxt.text.toString()

        userAddressRef.child("fullName").setValue(fullName)
        userAddressRef.child("contactNumber").setValue(contactNumber)
        userAddressRef.child("province").setValue(province)
        userAddressRef.child("city").setValue(city)
        userAddressRef.child("address").setValue(address)
            .addOnSuccessListener {
                showToast("Address updated successfully!")
                val intent = Intent(this@UpdateAddress, ViewAddress::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                showToast("Failed to update address.")
            }
    }

    private fun retrieveAddress() {
        userAddressRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullName = snapshot.child("fullName").getValue(String::class.java)
                    val contactNumber = snapshot.child("contactNumber").getValue(String::class.java)
                    val province = snapshot.child("province").getValue(String::class.java)
                    val city = snapshot.child("city").getValue(String::class.java)
                    val address = snapshot.child("address").getValue(String::class.java)

                    fullNameEditTxt.setText(fullName)
                    contactNumberEditTxt.setText(contactNumber)
                    provinceEditTxt.setText(province)
                    cityEditTxt.setText(city)
                    addressEditTxt.setText(address)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to retrieve address.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
