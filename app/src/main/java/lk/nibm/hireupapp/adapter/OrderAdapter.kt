package lk.nibm.hireupapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.ViewAddress
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.model.AddressDataClass
import lk.nibm.hireupapp.model.BookingPayment
import lk.nibm.hireupapp.model.Order
import lk.nibm.hireupapp.model.ServiceProviders

class OrderAdapter(
    private val orderList: List<Order>,
    private val serviceNameList: List<String>,
    private val providerList: List<ServiceProviders>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val spProfilePic: ImageView
        private val spName: TextView
        private val bookingDate: TextView
        private val spCategory: TextView
        private val status: TextView
        private val rate: TextView
        val cardView: CardView = itemView.findViewById(R.id.card)


        init {
            spProfilePic = itemView.findViewById(R.id.image)
            spName = itemView.findViewById(R.id.name)
            bookingDate = itemView.findViewById(R.id.day)
            spCategory = itemView.findViewById(R.id.desc)
            status = itemView.findViewById(R.id.priority)
            rate = itemView.findViewById(R.id.salary)

        }

        fun bind(order: Order, serviceName: String, provider: ServiceProviders) {
            bookingDate.text = order.bookingDate
            spCategory.text = serviceName
            status.text = order.status
            rate.text = "Rs. " + "${provider.price}" + " /Day"
            spName.text = provider.full_name
            Glide.with(itemView)
                .load(provider.photoURL)
                .into(spProfilePic)
            if (order.status == "In Progress") {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.accent_color))
            } else if (order.status == "Pending") {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
            } else if (order.status == "Accepted") {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.light_blue))
            } else if (order.status == "Completed") {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.light_purple))
            }
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
        val context = holder.itemView.context
        val bottomSheetDialog = BottomSheetDialog(context)
        holder.cardView.setOnClickListener {
            if (order.status == "In Progress") {
                val view = LayoutInflater.from(context).inflate(R.layout.booking_bottom_sheet, null)
                bottomSheetDialog.setContentView(view)
                val arrival = view.findViewById<LinearLayout>(R.id.arrivalLayout)
                val arrivalConfirm = view.findViewById<LinearLayout>(R.id.arrivalConfirmationLayout)
                val arrivalConfirmed = view.findViewById<LinearLayout>(R.id.arrivalConfirmedLayout)
                val serviceCompletion = view.findViewById<LinearLayout>(R.id.serviceCompleteLayout)
                val serviceCompletionConfirm =
                    view.findViewById<LinearLayout>(R.id.serviceCompletionConfirmLayout)
                val serviceCompleted = view.findViewById<LinearLayout>(R.id.serviceCompletedLayout)
                val payment = view.findViewById<LinearLayout>(R.id.paymentLayout)
                val paymentConfirm = view.findViewById<LinearLayout>(R.id.paymentConfirm)
                val paymentCashCard = view.findViewById<LinearLayout>(R.id.paymentCashCardLayout)
                val btnArrivalConfirm = view.findViewById<MaterialButton>(R.id.btnArrivalConfirm)
                val btnServiceComplete = view.findViewById<MaterialButton>(R.id.btnServiceComplete)
                val txtSpRate = view.findViewById<TextView>(R.id.txtSpRate)
                val txtPayment = view.findViewById<TextInputEditText>(R.id.txtPayment)
                val spProPic = view.findViewById<ImageView>(R.id.spProfilePic)
                val spName = view.findViewById<TextView>(R.id.spName)
                val category = view.findViewById<TextView>(R.id.category)
                val bookingDate = view.findViewById<TextView>(R.id.bookingDate)
                val btnCash = view.findViewById<MaterialButton>(R.id.btnCash)
                val btnCard = view.findViewById<MaterialButton>(R.id.btnCard)
                val btnProceed = view.findViewById<MaterialButton>(R.id.btnProceed)
                val paymentConfirmed = view.findViewById<LinearLayout>(R.id.paymentConfirmed)
                val txtPaidAmount = view.findViewById<TextView>(R.id.txtPaidAmount)
                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val orderRef: DatabaseReference =
                    database.reference.child("Orders").child(order.orderID!!)
                val paymentRef: DatabaseReference = database.reference.child("Payment")


                spName.text = provider.full_name
                category.text = serviceName
                bookingDate.text = order.bookingDate
                val imageUrl = provider.photoURL
                Glide.with(view)
                    .load(imageUrl)
                    .into(spProPic)

                if (order.arrivalConfirm == "") {
                    arrivalConfirm.visibility = LinearLayout.GONE
                    arrivalConfirmed.visibility = LinearLayout.GONE
                    paymentConfirmed.visibility = LinearLayout.GONE
                    serviceCompletionConfirm.visibility = LinearLayout.GONE
                    serviceCompleted.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    paymentCashCard.visibility = LinearLayout.GONE
                    arrival.visibility = LinearLayout.VISIBLE
                    serviceCompletion.visibility = LinearLayout.VISIBLE
                    payment.visibility = LinearLayout.VISIBLE

                } else if (order.arrivalConfirm == "Yes" && order.completeConfirm == "") {
                    arrival.visibility = LinearLayout.GONE
                    arrivalConfirm.visibility = LinearLayout.GONE
                    serviceCompleted.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    paymentCashCard.visibility = LinearLayout.GONE
                    serviceCompletion.visibility = LinearLayout.GONE
                    paymentConfirmed.visibility = LinearLayout.GONE
                    payment.visibility = LinearLayout.VISIBLE
                    serviceCompletionConfirm.visibility = LinearLayout.VISIBLE
                    arrivalConfirmed.visibility = LinearLayout.VISIBLE
                } else if (order.arrivalConfirm == "Yes" && order.completeConfirm == "Yes" && order.paid == "") {
                    arrival.visibility = LinearLayout.GONE
                    arrivalConfirm.visibility = LinearLayout.GONE
                    serviceCompletionConfirm.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    payment.visibility = LinearLayout.GONE
                    serviceCompletion.visibility = LinearLayout.GONE
                    paymentConfirmed.visibility = LinearLayout.GONE
                    paymentCashCard.visibility = LinearLayout.VISIBLE
                    serviceCompleted.visibility = LinearLayout.VISIBLE
                    arrivalConfirmed.visibility = LinearLayout.VISIBLE
                    bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    txtSpRate.text = "Rs. " + "${provider.price}"
                    txtPayment.setOnClickListener {
                        txtPayment.requestFocus()
                    }
                } else if (order.arrivalConfirm == "Yes" && order.completeConfirm == "Yes" && order.paid == "Pending") {
                    arrival.visibility = LinearLayout.GONE
                    arrivalConfirm.visibility = LinearLayout.GONE
                    serviceCompletionConfirm.visibility = LinearLayout.GONE
                    paymentConfirm.visibility = LinearLayout.GONE
                    payment.visibility = LinearLayout.GONE
                    serviceCompletion.visibility = LinearLayout.GONE
                    paymentConfirmed.visibility = LinearLayout.VISIBLE
                    paymentCashCard.visibility = LinearLayout.GONE
                    serviceCompleted.visibility = LinearLayout.VISIBLE
                    arrivalConfirmed.visibility = LinearLayout.VISIBLE
                    txtPaidAmount.text = "Rs. " + "${provider.price}"
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
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to confirm service provider's arrival?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            // Add the action to be performed when the user clicks "Yes"
                            // For example, you can put some code here to save the address data to another node with a unique key.
                            orderRef.child("arrivalConfirm").setValue("Yes").addOnSuccessListener {
                                val con = holder.itemView.context
                                Toast.makeText(
                                    con,
                                    "Arrival Confirmed Successfully!",
                                    Toast.LENGTH_LONG
                                ).show()
                                arrivalConfirm.visibility = LinearLayout.GONE
                                arrivalConfirmed.visibility = LinearLayout.VISIBLE
                                serviceCompletion.visibility = LinearLayout.GONE
                                serviceCompletionConfirm.visibility = LinearLayout.VISIBLE
                            }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Arrival Confirmation Failed!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            // Add the action to be performed when the user clicks "No" or presses the back button.
                            // For example, you can just dismiss the dialog without performing any action.
                            dialog.dismiss()
                        }
//                .setPositiveButtonStyle(positiveButtonStyle) // Set the custom style to the positive button
                        .show()

                }
                btnServiceComplete.setOnClickListener {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to add a booking?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            // Add the action to be performed when the user clicks "Yes"
                            // For example, you can put some code here to save the address data to another node with a unique key.
                            orderRef.child("completeConfirm").setValue("Yes").addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Service Completion Confirmed Successfully!",
                                    Toast.LENGTH_LONG
                                ).show()
                                serviceCompletionConfirm.visibility = LinearLayout.GONE
                                payment.visibility = LinearLayout.GONE
                                serviceCompleted.visibility = LinearLayout.VISIBLE
                                paymentCashCard.visibility = LinearLayout.VISIBLE
                                txtPayment.setText(provider.price)
                            }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Arrival Confirmation Failed!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            // Add the action to be performed when the user clicks "No" or presses the back button.
                            // For example, you can just dismiss the dialog without performing any action.
                            dialog.dismiss()
                        }
//                .setPositiveButtonStyle(positiveButtonStyle) // Set the custom style to the positive button
                        .show()

                }
                btnCash.setOnClickListener {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to make the payment with cash?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            paymentCashCard.visibility = LinearLayout.GONE
                            paymentConfirm.visibility = LinearLayout.VISIBLE
                            txtPayment.setText(provider.price)
                            // Add the action to be performed when the user clicks "Yes"
                            // For example, you can put some code here to save the address data to another node with a unique key.
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            // Add the action to be performed when the user clicks "No" or presses the back button.
                            // For example, you can just dismiss the dialog without performing any action.
                            dialog.dismiss()
                        }
//                .setPositiveButtonStyle(positiveButtonStyle) // Set the custom style to the positive button
                        .show()
                }
                btnProceed.setOnClickListener {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to make the payment?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val paymentCash = txtPayment.text.toString().trim()
                            val paymentID = paymentRef.push().key
                            if (paymentID != null) {
                                val newAddressRef = paymentRef.child(paymentID)
                                val paymentData = BookingPayment(
                                    paymentID,
                                    order.customerID,
                                    order.providerID,
                                    order.serviceID,
                                    paymentCash
                                )
                                newAddressRef.setValue(paymentData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Payment Successful!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Payment Failed!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                orderRef.child("paid").setValue("Pending").addOnSuccessListener {
                                    Toast.makeText(context, "Service Completed!", Toast.LENGTH_LONG)
                                        .show()
                                }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Failed!", Toast.LENGTH_LONG).show()
                                    }
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            // Add the action to be performed when the user clicks "No" or presses the back button.
                            // For example, you can just dismiss the dialog without performing any action.
                            dialog.dismiss()
                        }
//                .setPositiveButtonStyle(positiveButtonStyle) // Set the custom style to the positive button
                        .show()
                }


                bottomSheetDialog.show()


            }

        }
        if (order.arrivalConfirm == "Yes" && order.completeConfirm == "Yes" && order.paid == "Yes") {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val orderRef: DatabaseReference =
                database.reference.child("Orders").child(order.orderID!!)
            orderRef.child("status").setValue("Completed").addOnSuccessListener {
                Toast.makeText(context, "Service Completed!", Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed!", Toast.LENGTH_LONG).show()
                }
            bottomSheetDialog.dismiss()
        }
        holder.bind(order, serviceName, provider)
    }
}