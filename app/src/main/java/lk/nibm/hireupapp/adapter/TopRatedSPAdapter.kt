package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.SP_details
import lk.nibm.hireupapp.common.CategoryDataManager
import lk.nibm.hireupapp.common.ServiceProviderDataManager
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.TopRatedSP

class TopRatedSPAdapter(private val topSpList: List<TopRatedSP>) : RecyclerView.Adapter<TopRatedSPAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: TopRatedSP) {
            spName.text = item.spName
            spReview.text = item.ratingCount
            spRating.text = item.ratingValue.toString()
            spCategory.text = item.spCategory
            spAddress.text = item.spAddress
            spCity.text = item.spCity
            spDistrict.text = item.spDistrict
            spPrice.text = item.spPrice
            Glide.with(itemView)
                .load(item.spImageURL)
                .into(spProfilePhoto)
        }

        var spName : TextView
        var spCategory : TextView
        var spRating : TextView
        var spReview : TextView
        var spProfilePhoto : ImageView
        var topRatedSpCard : CardView
        var spAddress : TextView
        var spCity : TextView
        var spDistrict : TextView
        var spPrice : TextView
        init {
            spName = itemView.findViewById(R.id.txtSPCardServiceProviderName)
            spCategory = itemView.findViewById(R.id.txtSPCardServiceCategory)
            spRating = itemView.findViewById(R.id.spRating)
            spReview = itemView.findViewById(R.id.spReview)
            spProfilePhoto = itemView.findViewById(R.id.imgSPCardProfilePicture)
            topRatedSpCard = itemView.findViewById(R.id.spCard)
            spAddress = itemView.findViewById(R.id.txtSPCardAddress)
            spCity = itemView.findViewById(R.id.txtSPCardCity)
            spDistrict = itemView.findViewById(R.id.txtSpDistrict)
            spPrice = itemView.findViewById(R.id.txtSPCardStartingPrice)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_rated_sp_card_2, parent, false)
        return TopRatedSPAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topSpList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

}