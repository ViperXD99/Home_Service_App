package lk.nibm.hireupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.User
import lk.nibm.hireupapp.common.UserDataManager

const val REQUEST_CODE_SIGN_IN = 0

class SignIn : AppCompatActivity() {

    private lateinit var btnNormalSignIn: MaterialButton
    private lateinit var btnGoogleSignIn: MaterialButton
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var txtSignUp: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        usersReference = database.reference.child("Users")
        initializeComponents()
        clickListeners()
    }

    private fun clickListeners() {
        btnGoogleSignIn.setOnClickListener {
            googleSignIn()
        }
        txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        btnNormalSignIn.setOnClickListener {
            normalSignIn()
        }

    }

    private fun normalSignIn() {
        val email = txtEmail.text.toString().trim()
        val password = txtPassword.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val userId = currentUser.uid
                        retrieveUserData(userId)

                    }
                } else {
                    Toast.makeText(this, "Sign In Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun googleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(this, options)
        signInClient.signInIntent.also {
            startActivityForResult(it, REQUEST_CODE_SIGN_IN)
        }
    }

    private fun retrieveUserData(userId: String) {
        val userRef = usersReference.child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(this@SignIn, "Sign In SuccessFul!", Toast.LENGTH_SHORT).show()
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let {
                        UserDataManager.setUser(it)
                    }
                    val intent = Intent(this@SignIn , Home::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignIn, "Sign In Failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@SignIn,
                    "Failed to retrieve user data: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    initial()
                } else {
                    Toast.makeText(this@SignIn, "Authentication Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initial() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val displayName = currentUser?.displayName
        val email = currentUser?.email
        val photoUrl = currentUser?.photoUrl?.toString()

        if (userId != null) {
            checkIfUserExists(userId, displayName, email, photoUrl)
        } else {
            // Handle error case when user ID is null
        }

    }

    private fun checkIfUserExists(
        userId: String,
        displayName: String?,
        email: String?,
        photoUrl: String?
    ) {
        usersReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User already exists in the Realtime Database
                    // Log in to the application
                    // Handle your login logic here
                    Toast.makeText(this@SignIn, "Sign In SuccessFul!", Toast.LENGTH_SHORT).show()
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let {
                        UserDataManager.setUser(it)
                    }
                    val intent = Intent(this@SignIn , Home::class.java)
                    startActivity(intent)
                } else {
                    // User does not exist in the Realtime Database
                    // Save user details to the database
                    saveUserToDatabase(userId, displayName, email, photoUrl)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(applicationContext, "check User error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserToDatabase(
        userId: String,
        displayName: String?,
        email: String?,
        photoUrl: String?
    ) {
        val mobileNumber = "Not Set"
        val gender = "Not Set"
        val password = "Not Set"

        val user = User(userId, displayName, email, photoUrl, mobileNumber, gender, password)
        usersReference.child(userId).setValue(user)
            .addOnSuccessListener {
                // User data saved successfully
                // Log in to the application
                // Handle your login logic here
                Toast.makeText(applicationContext, "Logged in", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle error case when data saving fails
                Toast.makeText(applicationContext, "Database Failure", Toast.LENGTH_SHORT).show()
            }
    }

    private fun initializeComponents() {
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        btnNormalSignIn = findViewById(R.id.btnNormalSignIn)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
    }
}