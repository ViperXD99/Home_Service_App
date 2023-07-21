package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.UserDataManager

class Home : AppCompatActivity() {
    private lateinit var imgProfile : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initializeComponents()
        val userdata = UserDataManager.getUser()
        if (userdata != null){
            val imageUrl = userdata.photoUrl
            Glide.with(this)
                .load(imageUrl)
                .into(imgProfile)
        }
        else{
            Toast.makeText(this, "thota pissu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeComponents() {
        imgProfile = findViewById(R.id.imgProfile)
    }
}