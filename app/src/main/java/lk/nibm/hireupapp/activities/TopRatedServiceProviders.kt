package lk.nibm.hireupapp.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.CategoryRecyclerViewAdapter
import lk.nibm.hireupapp.adapter.TopRatedAdapter
import lk.nibm.hireupapp.model.Category
import lk.nibm.hireupapp.model.TopRatedSP

class TopRatedServiceProviders : AppCompatActivity() {
    private var layoutManager : RecyclerView.LayoutManager? = null
    private val topSpList = mutableListOf<TopRatedSP>()
    private lateinit var topAdapter: TopRatedAdapter
    private lateinit var topRatedRecyclerView: RecyclerView
    private lateinit var btnBack : MaterialButton
    private lateinit var serviceProviderRef: DatabaseReference
    private lateinit var dialog: Dialog
    private var isTopRatedDataLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_rated_service_providers)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        topRatedSPRecyclerView()
        clickListeners()
    }

    private fun clickListeners() {
        val btnBack = findViewById<MaterialButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun topRatedSPRecyclerView() {
        val database = FirebaseDatabase.getInstance()
        topRatedRecyclerView = findViewById(R.id.topRatedSPRecyclerView)
        layoutManager = GridLayoutManager(this, 2)
        topRatedRecyclerView.layoutManager = layoutManager
        topAdapter = TopRatedAdapter(topSpList)
        topRatedRecyclerView.adapter = topAdapter
        serviceProviderRef = database.reference.child("Service_Providers")
        serviceProviderRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                topSpList.clear()

                for (serviceProviderSnapshot in snapshot.children) {
                    val serviceProviderId = serviceProviderSnapshot.key.toString()
                    val serviceProviderName =
                        serviceProviderSnapshot.child("full_name").value.toString()
                    val serviceType = serviceProviderSnapshot.child("serviceId").value.toString()
                    val imageUrl = serviceProviderSnapshot.child("photoURL").value.toString()

                    // Fetch all orders related to this service provider
                    val categoryRef = database.reference.child("Service Categories").child(serviceType)
                    val ratingRef = database.reference.child("RatingAndReviews")
                    val serviceProviderRatingRef = ratingRef.orderByChild("providerID").equalTo(serviceProviderId)
                    var serviceName : String? = null


                    categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(ordersSnapshot: DataSnapshot) {
                            if( ordersSnapshot.exists()) {
                                // Fetch the rating from the order node
                                serviceName = ordersSnapshot.child("name").getValue(String()::class.java)
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle any database read errors here
                        }
                    })


                    serviceProviderRatingRef.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(ordersSnapshot: DataSnapshot) {
                            var totalRating = 0.0
                            var totalReviews = 0

                            for (orderSnapshot in ordersSnapshot.children) {
                                // Fetch the rating from the order node
                                val rating =
                                    orderSnapshot.child("ratingValue").getValue(Double::class.java)
                                        ?: 0.0
                                totalRating += rating
                                totalReviews++
                            }

                            // Calculate the average rating for this service provider
                            val averageRating = totalRating / totalReviews

                            val serviceProvider = TopRatedSP(
                                spName = serviceProviderName,
                                spID = serviceProviderId,
                                spCategory = serviceName,
                                spCategoryID = serviceType,
                                spImageURL = imageUrl,
                                ratingValue = averageRating,
                                ratingCount = totalReviews.toString()


                            )

                            topSpList.add(serviceProvider)
                            // Sort the list based on average rating in descending order
                            topSpList.sortBy { it.ratingValue }
                            topAdapter.notifyDataSetChanged()
                            isTopRatedDataLoaded = true
                            onDataLoaded()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle any database read errors here
                            isTopRatedDataLoaded = true
                            onDataLoaded()
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any database read errors here
            }
        })
    }
    private fun onDataLoaded() {
        if ( isTopRatedDataLoaded) {
            dialog.dismiss() // Hide the progress dialog when all data is loaded


        }
    }
}