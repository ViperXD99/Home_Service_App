package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.Home
import lk.nibm.hireupapp.activities.SP_details
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.ServiceProviderDataManager
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.TopRatedSP
import lk.nibm.hireupapp.model.User

class TopRatedAdapter(private val topSpList: List<TopRatedSP>) : RecyclerView.Adapter<TopRatedAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var imageURL : ImageView
        var spName : TextView
        var spCategory : TextView
        var spRating : TextView
        var spReview : TextView
        var spProfilePhoto : ImageView
        var topRatedSpCard : CardView


        init {
            spName = itemView.findViewById(R.id.spName)
            spCategory = itemView.findViewById(R.id.spCategory)
            spRating = itemView.findViewById(R.id.spRating)
            spReview = itemView.findViewById(R.id.spReview)
            spProfilePhoto = itemView.findViewById(R.id.spProfilePhoto)
            topRatedSpCard = itemView.findViewById(R.id.topRatedSpCard)
        }
        fun bind(item: TopRatedSP) {
            spName.text = item.spName
            spReview.text = item.ratingCount
            spRating.text = item.ratingValue.toString()
            spCategory.text = item.spCategory
            Glide.with(itemView)
                .load(item.spImageURL)
                .into(spProfilePhoto)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_rated_service_provider_card, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopRatedAdapter.ViewHolder, position: Int) {
        val item = topSpList[position]
        holder.topRatedSpCard.setOnClickListener {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val providerRef: DatabaseReference =
                database.reference.child("Service_Providers").child(item.spID!!)
            providerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val provider = dataSnapshot.getValue(ServiceProviders::class.java)
                        provider?.let {
                            ServiceProviderDataManager.setProvider(it)
                        }
                        CategoryDataManager.id = item.spCategoryID!!
                        CategoryDataManager.name = item.spCategory!!
                        val intent = Intent(holder.itemView.context , SP_details::class.java)
                        holder.itemView.context.startActivity(intent)

                    } else {
                        Toast.makeText(holder.itemView.context, "Failed to Load Data!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        holder.itemView.context,
                        "Failed to retrieve user data: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return topSpList.size
    }


}