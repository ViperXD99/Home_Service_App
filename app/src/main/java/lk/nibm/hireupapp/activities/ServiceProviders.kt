package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import lk.nibm.hireupapp.R

class ServiceProviders : AppCompatActivity() {
    private lateinit var txtCategoryType : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_providers)
        initializeComponents()
        loadCategory()
    }

    private fun loadCategory() {
        val extras = intent.extras
        if (extras != null) {
            // Retrieve the "CATEGORY_NAME" extra using the key "CATEGORY_NAME"
            txtCategoryType.text = extras.getString("CATEGORY_NAME")
        }
    }

    private fun initializeComponents() {
        txtCategoryType = findViewById(R.id.txtCategoryType)
    }
}