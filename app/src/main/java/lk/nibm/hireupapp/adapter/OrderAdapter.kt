package lk.nibm.hireupapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.model.Order
import lk.nibm.hireupapp.model.ServiceProviders

class OrderAdapter(private val orderList: List<Order>, private val serviceNameList: List<String> , private val providerList: List<ServiceProviders>):RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val spProfilePic : ImageView
        private val spName : TextView
        private val bookingDate : TextView
        private val spCategory : TextView
        private val status : TextView
        private val rate : TextView

        init {
            spProfilePic = itemView.findViewById(R.id.image)
            spName = itemView.findViewById(R.id.name)
            bookingDate = itemView.findViewById(R.id.day)
            spCategory = itemView.findViewById(R.id.desc)
            status = itemView.findViewById(R.id.priority)
            rate = itemView.findViewById(R.id.salary)

        }
        fun bind(order: Order, serviceName: String , provider:ServiceProviders) {
            bookingDate.text = order.bookingDate
            spCategory.text = serviceName
            status.text = order.status
            rate.text = provider.price
            spName.text = provider.full_name
            Glide.with(itemView)
                .load(provider.photoURL)
                .into(spProfilePic)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_card, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        val serviceName = serviceNameList[position]
        val provider = providerList[position]

        holder.bind(order,serviceName, provider)
    }
}