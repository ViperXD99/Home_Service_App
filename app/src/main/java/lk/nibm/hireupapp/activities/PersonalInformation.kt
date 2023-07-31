package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.databinding.ActivityPersonalInformationBinding

class PersonalInformation : AppCompatActivity() {

    private lateinit var binding : ActivityPersonalInformationBinding
    private lateinit var fullName_edit_txt : EditText
    private lateinit var contact_edit_txt : EditText
    private lateinit var email_edit_txt : EditText
    private lateinit var gender_edit_txt : EditText
    private lateinit var view : View
    private lateinit var database : FirebaseDatabase
    private lateinit var usersReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initializeComponents()
        loadProfileData()
        updateData()
        clickListeners()

    }

    private fun clickListeners() {
       binding.fullNameEdit.setOnClickListener{
           fullName_edit_txt.isEnabled = true
       }
        binding.contactEdit.setOnClickListener{
            binding.contactNumberEditTxt.isEnabled = true
        }
        binding.emailEdit.setOnClickListener{
            binding.emailEdtTxt.isEnabled = true
        }
        binding.genderEdit.setOnClickListener{
            binding.genderEdtTxt.isEnabled = true
        }
    }

    private fun updateData() {
        binding.saveButton.setOnClickListener{
            val displayName = fullName_edit_txt.text.toString().trim()
            val displayContact = contact_edit_txt.text.toString().trim()
            val displayEmail = email_edit_txt.text.toString().trim()
            val displayGender = gender_edit_txt.text.toString().trim()

            val user = UserDataManager.getUser()
            database = FirebaseDatabase.getInstance()
            usersReference =database.reference.child("Users").child(user?.userId.toString())
            usersReference.child("displayName").setValue(displayName)
            usersReference.child("mobileNumber").setValue(displayContact)
            usersReference.child("email").setValue(displayEmail)
            usersReference.child("gender").setValue(displayGender)
                .addOnSuccessListener{
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Profile update failed!", Toast.LENGTH_SHORT).show()
                }


        }
    }

    private fun loadProfileData() {
        val userData = UserDataManager.getUser()
        fullName_edit_txt.setText(userData?.displayName)
        contact_edit_txt.setText(userData?.mobileNumber)
        email_edit_txt.setText(userData?.email)
        gender_edit_txt.setText(userData?.gender)
    }

    private fun initializeComponents() {
        fullName_edit_txt =findViewById(R.id.fullName_edit_txt)
        contact_edit_txt =findViewById(R.id.contactNumber_edit_txt)
        email_edit_txt =findViewById(R.id.email_edt_txt)
        gender_edit_txt =findViewById(R.id.gender_edt_txt)
    }
}