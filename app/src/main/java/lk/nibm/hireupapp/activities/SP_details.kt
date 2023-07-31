package lk.nibm.hireupapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.ServiceProviderDataManager
import lk.nibm.hireupapp.model.TopRatedSP

class SP_details : AppCompatActivity() {
    private lateinit var spName : TextView
    private lateinit var spRole : TextView
    private lateinit var spAddress : TextView
    private lateinit var spCity : TextView
    private lateinit var spDistrict : TextView
    private lateinit var spPrice : TextView
    private lateinit var spAbout : TextView
    private lateinit var spProPic : ImageView
    private lateinit var spStaringDate : TextView
    private lateinit var spEndingDate : TextView
    private lateinit var spStartingTime : TextView
    private lateinit var spEndingTime : TextView
    private lateinit var spBook : Button
    private lateinit var spCall : ImageButton
    private lateinit var btnBack : ImageButton
    private var totalRating : Double = 0.0
    private var totalReviews : Int = 0
    private var averageRating : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp_details)
        val providerData = ServiceProviderDataManager.getProvider()
        initializeComponents()
        clickListeners()

        spName.text = providerData?.full_name
        spRole.text = CategoryDataManager.name
        spAddress.text = providerData?.address
        spCity.text = providerData?.city
        spDistrict.text = providerData?.district
        spPrice.text= providerData?.price
        spAbout.text = providerData?.about
        spStaringDate.text = providerData?.startingDate
        spEndingDate.text =providerData?.endDate
        spStartingTime.text = providerData?.startingTime
        spEndingTime.text = providerData?.endTime
        Glide.with(this)
            .load(providerData?.photoURL)
            .into(spProPic)
        /*spWorkingHours.text = CategoryDataManager.*/
        val database = FirebaseDatabase.getInstance()
        val ratingRef = database.reference.child("RatingAndReviews")
        val serviceProviderRatingRef = ratingRef.orderByChild("providerID").equalTo(providerData?.providerId)
        serviceProviderRatingRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(ordersSnapshot: DataSnapshot) {
                totalRating = 0.0
                totalReviews = 0

                for (orderSnapshot in ordersSnapshot.children) {
                    // Fetch the rating from the order node
                    val rating =
                        orderSnapshot.child("ratingValue").getValue(Double::class.java)
                            ?: 0.0
                    totalRating += rating
                    totalReviews++
                }

                // Calculate the average rating for this service provider
                averageRating = totalRating / totalReviews
                val spRate = findViewById<TextView>(R.id.sp_rate)
                val spReviews = findViewById<TextView>(R.id.sp_reviews)
                spRate.text = averageRating.toString()
                spReviews.text = totalReviews.toString() + " Reviews"

            }

            override fun onCancelled(error: DatabaseError) {
               //
            }
        })

    }

    private fun clickListeners() {
        spBook.setOnClickListener {
            val intent = Intent(this, BookNow::class.java)
            intent.putExtra("RATING_VALUE", averageRating.toString())
            intent.putExtra("NO_OF_REVIEWS", totalReviews.toString())
            startActivity(intent)
        }
        spCall.setOnClickListener {
            val providerData = ServiceProviderDataManager.getProvider()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+providerData?.contact)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeComponents() {
        spName = findViewById(R.id.sp_name)
        spRole = findViewById(R.id.sp_role)
        spAddress = findViewById(R.id.sp_address)
        spCity = findViewById(R.id.sp_city)
        spDistrict = findViewById(R.id.sp_district)
        spPrice = findViewById(R.id.sp_price)
        spAbout = findViewById(R.id.sp_about)
        spStaringDate = findViewById(R.id.sp_startingDate)
        spEndingDate = findViewById(R.id.sp_endingDate)
        spStartingTime =findViewById(R.id.sp_startingTime)
        spEndingTime = findViewById(R.id.sp_endingTime)
        spBook = findViewById(R.id.sp_book)
        spProPic = findViewById(R.id.sp_profile_pic)
        spCall = findViewById(R.id.call_sp)
        btnBack = findViewById(R.id.btnBack)

    }
}