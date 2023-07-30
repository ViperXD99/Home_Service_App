package lk.nibm.hireupapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.ReviewRating
import lk.nibm.hireupapp.databinding.ActivityAddReviewBinding
import lk.nibm.hireupapp.model.AddressDataClass
import lk.nibm.hireupapp.model.ReviewsAndRatings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddReview : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var ratingData: ReviewsAndRatings
    private lateinit var addRating: ReviewsAndRatings
    private lateinit var reviewDate: String
    private lateinit var database: FirebaseDatabase
    private lateinit var userRatingRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        val view = binding.root
        ratingData = ReviewRating.getReview()!!
        setContentView(view)
        loadReviewData()
        clickListeners()
    }

    private fun isValidated(): Boolean {
        val rating = binding.ratingBar.rating.toDouble()
        val feedback = binding.feedbackEditTxt.text.toString().trim()
        if (feedback.isEmpty()) {
            binding.feedbackEditTxt.error = " Please add your feedback"
            binding.feedbackEditTxt.requestFocus()
            return false
        }
        addRating = ReviewsAndRatings(
            ratingData.bookingDate,
            ratingData.customerID,
            feedback,
            ratingData.orderID,
            ratingData.providerID,
            rating,
            reviewDate,
            ratingData.serviceID
        )
        return true
    }

    private fun clickListeners() {

        binding.submit.setOnClickListener {
            if (isValidated()) {
                database = FirebaseDatabase.getInstance()
                userRatingRef = database.reference.child("RatingAndReviews")
                val ratingId = userRatingRef.push().key
                if (ratingId != null) {
                    val newAddressRef = userRatingRef.child(ratingId)
                    newAddressRef.setValue(addRating)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Your review added successfully!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to add review!", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
    }

    private fun loadReviewData() {
        val extras = intent.extras
        if (extras != null) {
            // Retrieve the "CATEGORY_NAME" extra using the key "CATEGORY_NAME"
            binding.orderIdTxt.text = ratingData.orderID
            binding.paymentTxt.text = extras.getString("PAID_AMOUNT").toString()
            binding.serviceTypeTxt.text = extras.getString("SERVICE_NAME").toString()
            val photoURl = extras.getString("SP_PHOTO_URL").toString()
            Glide.with(this)
                .load(photoURl)
                .into(binding.image)
            binding.spNameTxt.text = extras.getString("PROVIDER_NAME").toString()
            binding.txtOrderDate.text = ratingData.bookingDate

            val dateFormat = SimpleDateFormat("MMM, dd, yyyy", Locale.getDefault())
            val systemDate = Date(System.currentTimeMillis())
            reviewDate = dateFormat.format(systemDate).toString()
            binding.dateTxt.text = dateFormat.format(systemDate).toString()
        }
    }
}