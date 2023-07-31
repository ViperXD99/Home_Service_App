package lk.nibm.hireupapp.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.AddressDataManager
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.OrderStatuses
import lk.nibm.hireupapp.common.ServiceProviderDataManager
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.model.AddressDataClass
import lk.nibm.hireupapp.model.Order
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.SignUpData
import lk.nibm.hireupapp.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookNow : AppCompatActivity() {
    private lateinit var spName: TextView
    private lateinit var spCategory: TextView
    private lateinit var spAddress: TextView
    private lateinit var spCity: TextView
    private lateinit var spDistrict: TextView
    private lateinit var btnBack: MaterialButton
    private lateinit var spProPic: ImageView
    private lateinit var spCall: ImageButton
    private lateinit var selectDate: TextInputEditText
    private lateinit var txtOrderDescription: TextInputEditText
    private lateinit var btnBook: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var userAddressRef: DatabaseReference
    private lateinit var userOrderRef: DatabaseReference
    private lateinit var txtAddressName: TextView
    private lateinit var txtAddressContact: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtAddressCity: TextView
    private lateinit var txtAddressDistrict: TextView
    private val addressList = mutableListOf<AddressDataClass>()
    private lateinit var txtRate : TextView
    private var addressID: String? = null
    private lateinit var orderData: Order


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_now)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        database = FirebaseDatabase.getInstance()
        initializeComponents()
        loadServiceProviderDetails()
        loadAddress()
        clickListeners()
    }

    private fun loadAddress() {
        val userID = UserDataManager.getUser()
        userAddressRef =
            database.reference.child("Users").child(userID?.userId.toString()).child("address")
        userAddressRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (addressSnapshot in dataSnapshot.children) {
                    val address = addressSnapshot.getValue(AddressDataClass::class.java)
                    address?.let {
                        addressList.add(it)
                    }
                }

                // Now that you have the list of addresses, display the first one in the TextView
                if (addressList.isNotEmpty()) {
                    val firstAddress = addressList[0]
                    txtAddressCity.text = firstAddress.city
                    txtAddress.text = firstAddress.address
                    txtAddressContact.text = firstAddress.contactNumber
                    txtAddressDistrict.text = firstAddress.province
                    txtAddressName.text = firstAddress.fullName
                    addressID = firstAddress.addressId

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if any
            }
        })
    }

    private fun clickListeners() {
        txtOrderDescription.setOnClickListener {
            txtOrderDescription.requestFocus()
        }
        selectDate.setOnClickListener {
            showDatePicker()
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
        spCall.setOnClickListener {
            val providerData = ServiceProviderDataManager.getProvider()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + providerData?.contact)
            startActivity(intent)
        }
        btnBook.setOnClickListener {
            if (isValidated()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to add a booking?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        // Add the action to be performed when the user clicks "Yes"
                        // For example, you can put some code here to save the address data to another node with a unique key.
                        addBooking()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        // Add the action to be performed when the user clicks "No" or presses the back button.
                        // For example, you can just dismiss the dialog without performing any action.
                        dialog.dismiss()
                    }
//                .setPositiveButtonStyle(positiveButtonStyle) // Set the custom style to the positive button
                    .show()
            }
        }
    }

    private fun isValidated(): Boolean {
        val customer = UserDataManager.getUser()
        val provider = ServiceProviderDataManager.getProvider()
        val addressID = addressID.toString()
        val arrivalConfirm = ""
        val bookingDate = selectDate.text.toString().trim()
        val completeConfirm = ""
        val customerID = customer?.userId
        val description = txtOrderDescription.text.toString().trim()
        val orderID = userAddressRef.push().key
        val providerID = provider?.providerId
        val serviceID = CategoryDataManager.id
        val spArrivalConfirm = ""
        val spCompleteConfirm = ""
        val isPaid = ""
        val status = OrderStatuses.pending
        if (bookingDate.isEmpty()) {
            selectDate.error = " Please select a date"
            selectDate.requestFocus()
            return false
        }
        if (description.isEmpty()) {
            txtOrderDescription.error = " Please add a description"
            txtOrderDescription.requestFocus()
            return false
        }
        orderData = Order(
            addressID,
            arrivalConfirm,
            bookingDate,
            completeConfirm,
            customerID,
            description,
            isPaid,
            orderID,
            providerID,
            serviceID,
            spArrivalConfirm,
            spCompleteConfirm,
            status
        )
        return true
    }

    private fun addBooking() {
        if (orderData.orderID != null) {
            userOrderRef = database.reference.child("Orders").child(orderData.orderID.toString())
            userOrderRef.setValue(orderData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Order Added Successfully!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add Order!", Toast.LENGTH_LONG).show()
                }
        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                val selectedDate = formatDate(year, month, day)
                selectDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("MMM, dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun loadServiceProviderDetails() {
        val providerData = ServiceProviderDataManager.getProvider()
        spName.text = providerData?.full_name
        spCategory.text = CategoryDataManager.name
        spAddress.text = providerData?.address
        spCity.text = providerData?.city
        spDistrict.text = providerData?.district
        txtRate.text = providerData?.price
        Glide.with(this)
            .load(providerData?.photoURL)
            .into(spProPic)

    }

    private fun initializeComponents() {
        spName = findViewById(R.id.sp_name)
        spCategory = findViewById(R.id.sp_serviceName)
        spAddress = findViewById(R.id.sp_address)
        spCity = findViewById(R.id.sp_city)
        spDistrict = findViewById(R.id.sp_district)
        btnBack = findViewById(R.id.btnBack)
        spProPic = findViewById(R.id.sp_image)
        spCall = findViewById(R.id.call_sp)
        selectDate = findViewById(R.id.txtSelectDate)
        txtOrderDescription = findViewById(R.id.txtOrderDescription)
        btnBook = findViewById(R.id.btnBook)
        txtAddressName = findViewById(R.id.full_name_txt)
        txtAddressCity = findViewById(R.id.txtCity)
        txtAddress = findViewById(R.id.address_txt)
        txtAddressContact = findViewById(R.id.tel_number_txt)
        txtAddressDistrict = findViewById(R.id.txtDistrict)
        txtRate = findViewById(R.id.txtRate)
    }
}