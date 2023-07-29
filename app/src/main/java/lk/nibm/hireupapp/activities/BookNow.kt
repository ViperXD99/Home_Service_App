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
import lk.nibm.hireupapp.common.ServiceProviderDataManager
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.model.AddressDataClass
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookNow : AppCompatActivity() {
    private lateinit var spName : TextView
    private lateinit var spCategory : TextView
    private lateinit var spAddress : TextView
    private lateinit var spCity : TextView
    private lateinit var spDistrict : TextView
    private lateinit var btnBack : MaterialButton
    private lateinit var spProPic : ImageView
    private lateinit var spCall : ImageButton
    private lateinit var selectDate : TextInputEditText
    private lateinit var txtOrderDescription : TextInputEditText
    private lateinit var btnBook : Button
    private lateinit var database: FirebaseDatabase
    private lateinit var userAddressRef: DatabaseReference
    private lateinit var txtAddressName : TextView
    private lateinit var txtAddressContact : TextView
    private lateinit var txtAddress : TextView
    private lateinit var txtAddressCity : TextView
    private lateinit var txtAddressDistrict : TextView


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
//        val userID = UserDataManager.getUser()
//        Toast.makeText(this@BookNow, userID?.userId.toString(), Toast.LENGTH_SHORT).show()
//        userAddressRef = database.reference.child("Users").child(userID?.userId.toString()).child("address")
//        userAddressRef.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                if (dataSnapshot.exists()) {
//                    val address = dataSnapshot.getValue(AddressDataClass::class.java)
//                    address?.let {
//                        AddressDataManager.setAddress(it)
//
//                    }
//                  val getAddress = AddressDataManager.getAddress()
//                    txtAddressName.text = getAddress?.fullName
//                    txtAddress.text = getAddress?.address
//                    txtAddressContact.text = getAddress?.contactNumber
//                    txtAddressCity.text = getAddress?.city
//                    txtAddressDistrict.text = getAddress?.province
//
//                } else {
//                    Toast.makeText(this@BookNow, "Failed to load address!", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle any errors that may occur while fetching data
//            }
//        })
    }

    private fun clickListeners() {
        txtOrderDescription.setOnClickListener {
            txtOrderDescription.requestFocus()
        }
        selectDate.setOnClickListener {
            showDatePicker()
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, SP_details::class.java)
            startActivity(intent)
        }
        spCall.setOnClickListener {
            val providerData = ServiceProviderDataManager.getProvider()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+providerData?.contact)
            startActivity(intent)
        }
        btnBook.setOnClickListener {
            addBooking()
        }
    }

    private fun addBooking() {

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

        val dateFormat = SimpleDateFormat("EEE, dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun loadServiceProviderDetails() {
        val providerData = ServiceProviderDataManager.getProvider()
        spName.text = providerData?.full_name
        spCategory.text = CategoryDataManager.name
        spAddress.text = providerData?.address
        spCity.text = providerData?.city
        spDistrict.text = providerData?.district
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
    }

    fun openDatePicker(view: View) {}
}