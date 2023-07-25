package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.databinding.ActivityPersonalInformationBinding

class PersonalInformation : AppCompatActivity() {

    private lateinit var binding : ActivityPersonalInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}