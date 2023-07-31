package lk.nibm.hireupapp.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.ServiceCategories
import lk.nibm.hireupapp.adapter.CategoryAdapter
import lk.nibm.hireupapp.adapter.TopRatedAdapter
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.model.Category
import lk.nibm.hireupapp.model.TopRatedSP
import java.util.Calendar


class HomeFragment : Fragment() {
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var topRatedRecyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var view: View
    private lateinit var adapter: CategoryAdapter
    private lateinit var topAdapter: TopRatedAdapter
    private val itemList = mutableListOf<Category>()
    private val topSpList = mutableListOf<TopRatedSP>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var topRatedRef: DatabaseReference
    private lateinit var serviceProviderRef: DatabaseReference
    private lateinit var categoryRef: DatabaseReference
    private lateinit var imgProfile: ImageView
    private lateinit var loggedUserName: TextView
    private lateinit var seeAllCategories: TextView
    private lateinit var imgWelcome: ImageView
    private lateinit var txtWelcome: TextView
    private lateinit var dialog: Dialog
    private var isCategoryDataLoaded = false
    private var isTopRatedDataLoaded = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.loading_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        initializeComponents()
        categoryRecyclerView()
        loadProfileDetails()
        clickListeners()
        topRatedSPRecyclerView()
        return view
    }

    private fun topRatedSPRecyclerView() {
        val database = FirebaseDatabase.getInstance()
        topRatedRecyclerView = view.findViewById(R.id.topRatedRecyclerView)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

    private fun clickListeners() {
        seeAllCategories.setOnClickListener {
            val intent = Intent(requireContext(), ServiceCategories::class.java)
            startActivity(intent)
        }
    }

    private fun initializeComponents() {
        imgProfile = view.findViewById(R.id.imgProfile)
        loggedUserName = view.findViewById(R.id.logged_user_name)
        seeAllCategories = view.findViewById(R.id.seeAllCategories)
        imgWelcome = view.findViewById(R.id.imgWelcome)
        txtWelcome = view.findViewById(R.id.txtWelcome)
    }

    private fun loadProfileDetails() {
        val userdata = UserDataManager.getUser()
        if (userdata != null) {
            val imageUrl = userdata.photoUrl
            Glide.with(this)
                .load(imageUrl)
                .into(imgProfile)
            loggedUserName.text = userdata.displayName
        } else {
            Toast.makeText(requireContext(), "Sign In SuccessFul!", Toast.LENGTH_SHORT).show()
        }
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        when (hourOfDay) {
            in 6..11 -> {
                // Show morning image
                imgWelcome.setImageResource(R.drawable.ic_fluent_weather_sunny_high_24_filled)
                txtWelcome.text = "Good Morning!"
            }

            in 12..16 -> {
                imgWelcome.setImageResource(R.drawable.ic_fluent_weather_sunny_24_filled)
                txtWelcome.text = "Good Afternoon!"
            }

            in 17..20 -> {
                imgWelcome.setImageResource(R.drawable.ic_fluent_weather_sunny_low_24_filled)
                txtWelcome.text = "Good Evening!"
            }

            else -> {
                imgWelcome.setImageResource(R.drawable.ic_fluent_weather_moon_24_filled)
                txtWelcome.text = "Good Night!"
            }
        }

    }

    private fun categoryRecyclerView() {
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.layoutManager = layoutManager
        adapter = CategoryAdapter(itemList)
        categoryRecyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Service Categories")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Category::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
                isCategoryDataLoaded = true
                onDataLoaded()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
                isCategoryDataLoaded = true
                onDataLoaded()
            }
        })
    }

    private fun onDataLoaded() {
        if (isCategoryDataLoaded && isTopRatedDataLoaded) {
            dialog.dismiss() // Hide the progress dialog when all data is loaded


        }
    }
}