package lk.nibm.hireupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.SignUpData
import lk.nibm.hireupapp.model.User

class SignUp : AppCompatActivity() {
    private lateinit var txtSignIn: TextView
    private lateinit var btnSignUp: MaterialButton
    private lateinit var txtFullName: TextInputEditText
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtMobileNumber: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var txtConfirmPassword: TextInputEditText

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var signUpData : SignUpData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        initializeComponents()
        clickListeners()

    }

    private fun clickListeners() {
        txtSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
        btnSignUp.setOnClickListener {
            signUp()
        }


    }

    private fun isValidated(): Boolean {
        val displayName = txtFullName.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val mobileNumber = txtMobileNumber.text.toString().trim()
        val password = txtPassword.text.toString().trim()
        val confirmPassword = txtConfirmPassword.text.toString().trim()
        if (displayName.isEmpty()) {
            txtFullName.error = " Please enter your full name"
            txtFullName.requestFocus()
            return false
        }
        if (mobileNumber.isEmpty()) {
            txtMobileNumber.error = "Please enter your mobile number"
            txtMobileNumber.requestFocus()
            return false
        } else if (mobileNumber.length != 10) {
            txtMobileNumber.error = "Please enter a valid mobile number"
            txtMobileNumber.requestFocus()
            return false
        }
        if (email.isEmpty()) {
            txtEmail.error = "Please enter your email address"
            txtEmail.requestFocus()
            return false
        } else if (!isValidEmail(email)) {
            txtEmail.error = "Please enter a valid email address"
            txtEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            txtPassword.error = "Please enter your password"
            txtPassword.requestFocus()
            return false
        }
        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.error = "Please confirm your password"
        } else if (confirmPassword != password) {
            txtConfirmPassword.error = "Password does not match"
            txtConfirmPassword.requestFocus()
            return false
        }
        signUpData = SignUpData(displayName,email,mobileNumber,password)
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@gmail\\.com$")
        return email.matches(emailRegex)
    }

    private fun signUp() {
        if (isValidated()) {
            firebaseAuth.createUserWithEmailAndPassword(signUpData.email.toString(), signUpData.password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign up successful, add user data to Firebase database
                        val user = firebaseAuth.currentUser
                        val userId = user?.uid
                        val photoUrl = user?.photoUrl
                        val gender = "Not Set"
                        if (userId != null) {
                            val userRef = database.getReference("Users").child(userId)

                            val userData = User(userId,signUpData.displayName , signUpData.email, photoUrl.toString(), signUpData.mobileNumber, gender, signUpData.password)
                            userRef.setValue(userData)

                            Toast.makeText(this, "Successfully Registered!", Toast.LENGTH_SHORT).show()

                            // Start the sign-in activity
                            val intent = Intent(this, SignIn::class.java)
                            startActivity(intent)

                            // Finish the current activity to prevent going back to sign-up screen
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Sign Up failed!", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun initializeComponents() {
        txtSignIn = findViewById(R.id.txtSignIn)
        btnSignUp = findViewById(R.id.btnSignUp)
        txtFullName = findViewById(R.id.txtFullName)
        txtEmail = findViewById(R.id.txtEmail)
        txtMobileNumber = findViewById(R.id.txtMobileNumber)
        txtPassword = findViewById(R.id.txtPassword)
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword)
    }
}