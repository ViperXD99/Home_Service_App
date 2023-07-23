package lk.nibm.hireupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.ServiceProviderDataManager

class SP_details : AppCompatActivity() {
    private lateinit var spName : TextView
    private lateinit var spBook : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp_details)
        val providerData = ServiceProviderDataManager.getProvider()
        initializeComponents()
        clickListeners()
        spName.text = providerData?.full_name
        Toast.makeText(this, providerData?.about, Toast.LENGTH_LONG).show()
    }

    private fun clickListeners() {
        spBook.setOnClickListener {
            val intent = Intent(this, BookNow::class.java)
            startActivity(intent)
        }
    }

    private fun initializeComponents() {
        spName = findViewById(R.id.sp_name)
        spBook = findViewById(R.id.sp_book)
    }
}