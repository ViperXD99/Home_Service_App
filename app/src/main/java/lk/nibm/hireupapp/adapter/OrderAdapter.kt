package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.ViewAddress
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
        val cardView : CardView = itemView.findViewById(R.id.card)


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
        holder.cardView.setOnClickListener {
//            Toast.makeText(getActivity, categoryList[position].name, Toast.LENGTH_SHORT).show()
            if (order.status == "In Progress"){
                val context = holder.itemView.context
                val bottomSheetDialog = BottomSheetDialog(context)
                val view = LayoutInflater.from(context).inflate(R.layout.booking_bottom_sheet, null)
                bottomSheetDialog.setContentView(view)
                val arrival = view.findViewById<LinearLayout>(R.id.arrivalLayout)
                val arrivalConfirm = view.findViewById<LinearLayout>(R.id.arrivalConfirmationLayout)
                val arrivalConfirmed = view.findViewById<LinearLayout>(R.id.arrivalConfirmedLayout)
                val serviceCompletion = view.findViewById<LinearLayout>(R.id.serviceCompleteLayout)
                val serviceCompletionConfirm = view.findViewById<LinearLayout>(R.id.serviceCompletionConfirmLayout)
                val serviceCompleted = view.findViewById<LinearLayout>(R.id.serviceCompletedLayout)
                val payment = view.findViewById<LinearLayout>(R.id.paymentLayout)
                val paymentConfirm = view.findViewById<LinearLayout>(R.id.paymentConfirm)
                val paymentCashCard = view.findViewById<LinearLayout>(R.id.paymentCashCardLayout)
                val btnArrivalConfirm = view.findViewById<MaterialButton>(R.id.btnArrivalConfirm)
                val btnServiceComplete = view.findViewById<MaterialButton>(R.id.btnServiceComplete)
                val txtPayment = view.findViewById<TextInputEditText>(R.id.txtPayment)
                val spProPic = view.findViewById<ImageView>(R.id.spProfilePic)
                val spName = view.findViewById<TextView>(R.id.spName)
                val category = view.findViewById<TextView>(R.id.category)
                var database : FirebaseDatabase = FirebaseDatabase.getInstance()
                var orderRef : DatabaseReference = database.reference.child("Orders").child(order.orderID!!)


                spName.text = provider.full_name
                category.text = serviceName
                val imageUrl = provider.photoURL
                        Glide.with(view)
                            .load(imageUrl)
                            .into(spProPic)

                if (order.arrivalConfirm == "No"){
                    arrivalConfirm.visibility = LinearLayout.GONE
                    arrivalConfirmed.visibility = LinearLayout.GONE
                    serviceCompletionConfirm.visibility = LinearLayout.GONE
                    serviceCompleted.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    paymentCashCard.visibility = LinearLayout.GONE
                    arrival.visibility = LinearLayout.VISIBLE
                    serviceCompletion.visibility = LinearLayout.VISIBLE
                    payment.visibility =  LinearLayout.VISIBLE

                }
                else if(order.arrivalConfirm == "Yes" && order.completeConfirm == "No"){
                    arrival.visibility = LinearLayout.GONE
                    arrivalConfirm.visibility = LinearLayout.GONE
                    serviceCompleted.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    paymentCashCard.visibility = LinearLayout.GONE
                    serviceCompletion.visibility = LinearLayout.GONE
                    payment.visibility =  LinearLayout.VISIBLE
                    serviceCompletionConfirm.visibility = LinearLayout.VISIBLE
                    arrivalConfirmed.visibility = LinearLayout.VISIBLE
                }
                else if(order.arrivalConfirm == "Yes" && order.completeConfirm == "Yes"){
                    arrival.visibility = LinearLayout.GONE
                    arrivalConfirm.visibility = LinearLayout.GONE
                    serviceCompletionConfirm.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    payment.visibility = LinearLayout.GONE
                    serviceCompletion.visibility = LinearLayout.GONE
                    paymentCashCard.visibility =  LinearLayout.VISIBLE
                    serviceCompleted.visibility = LinearLayout.VISIBLE
                    arrivalConfirmed.visibility = LinearLayout.VISIBLE
                    txtPayment.setText(provider.price)
                    txtPayment.setOnClickListener{
                        txtPayment.requestFocus()
                    }
                }

                arrival.setOnClickListener {
                    arrival.visibility = LinearLayout.GONE
                    arrivalConfirm.visibility = LinearLayout.VISIBLE
                }
                arrivalConfirm.setOnClickListener {
                    arrival.visibility = LinearLayout.VISIBLE
                    arrivalConfirm.visibility = LinearLayout.GONE

                }
                btnArrivalConfirm.setOnClickListener {

                    orderRef.child("arrivalConfirm").setValue("Yes").addOnSuccessListener {
                        val context = holder.itemView.context
                        Toast.makeText(context, "Arrival Confirmed Successfully!", Toast.LENGTH_LONG).show()
                        arrivalConfirm.visibility = LinearLayout.GONE
                        arrivalConfirmed.visibility = LinearLayout.VISIBLE
                        serviceCompletion.visibility = LinearLayout.GONE
                        serviceCompletionConfirm.visibility = LinearLayout.VISIBLE
                    }
                        .addOnFailureListener {
                            Toast.makeText(context, "Arrival Confirmation Failed!", Toast.LENGTH_LONG).show()
                        }
                }
                btnServiceComplete.setOnClickListener{
                    orderRef.child("completeConfirm").setValue("Yes").addOnSuccessListener {
                        val context = holder.itemView.context
                        Toast.makeText(context, "Service Completion Confirmed Successfully!", Toast.LENGTH_LONG).show()
                        serviceCompletionConfirm.visibility = LinearLayout.GONE
                        payment.visibility = LinearLayout.GONE
                        serviceCompleted.visibility = LinearLayout.VISIBLE
                        paymentCashCard.visibility = LinearLayout.VISIBLE
                    }
                        .addOnFailureListener {
                            Toast.makeText(context, "Arrival Confirmation Failed!", Toast.LENGTH_LONG).show()
                        }
                }

                bottomSheetDialog.show()
            }

        }

        holder.bind(order,serviceName, provider)
    }
}