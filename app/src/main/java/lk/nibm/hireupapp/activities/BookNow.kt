package lk.nibm.hireupapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.ServiceProviderDataManager

class BookNow : AppCompatActivity() {
    private lateinit var spName : TextView
    private lateinit var spCategory : TextView
    private lateinit var spAddress : TextView
    private lateinit var spCity : TextView
    private lateinit var spDistrict : TextView
    private lateinit var btnBack : MaterialButton
    private lateinit var spProPic : ImageView
    private lateinit var spCall : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_now)

        initializeComponents()
        loadServiceProviderDetails()
        clickListeners()


    }

    private fun clickListeners() {
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
    }

    fun openDatePicker(view: View) {}
}