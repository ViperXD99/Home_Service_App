package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.ServiceProviderDataManager

class BookNow : AppCompatActivity() {
    private lateinit var spName : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_now)
        val providerData = ServiceProviderDataManager.getProvider()
        initializeComponents()
        spName.text = providerData?.full_name

    }

    private fun initializeComponents() {
        spName = findViewById(R.id.sp_name)
    }

    fun openDatePicker(view: View) {}
}