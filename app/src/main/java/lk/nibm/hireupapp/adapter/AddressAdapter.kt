package lk.nibm.hireupapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lk.nibm.hireupapp.R
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import lk.nibm.hireupapp.activities.UpdateAddress
import lk.nibm.hireupapp.model.AddressDataClass

class AddressAdapter(private val addressList: List<AddressDataClass>) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameTextView: TextView = itemView.findViewById(R.id.full_name)
        val contactNumberTextView: TextView = itemView.findViewById(R.id.contact_number)
        val provinceTextView: TextView = itemView.findViewById(R.id.province)
        val cityTextView: TextView = itemView.findViewById(R.id.city)
        val addressTextView: TextView = itemView.findViewById(R.id.address)
        val editAddressButton: TextView = itemView.findViewById(R.id.edit_address)
        val deleteAddressButton: TextView = itemView.findViewById(R.id.delete_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid).child("address")

        return AddressViewHolder(itemView)
    }

    private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete Address")
            .setMessage("Are you sure you want to delete this address?")
            .setPositiveButton("Delete") { _, _ ->
                // User clicked the "Delete" button
                onConfirm.invoke() // Execute the onConfirm callback
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }


    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentAddress = addressList[position]
        holder.fullNameTextView.text = currentAddress.fullName
        holder.contactNumberTextView.text = currentAddress.contactNumber
        holder.provinceTextView.text = currentAddress.province
        holder.cityTextView.text = currentAddress.city
        holder.addressTextView.text = currentAddress.address

        holder.editAddressButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateAddress::class.java)
            intent.putExtra("addressId", currentAddress.addressId) // Pass the address ID to UpdateAddress activity
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteAddressButton.setOnClickListener {
            // Retrieve the current address data from the adapter
            val currentAddress = addressList[holder.adapterPosition]
            val addressId = currentAddress.addressId
            if (addressId != null) {
                showDeleteConfirmationDialog(holder.itemView.context) {
                    // This block will be executed if the user confirms the delete action
                    val addressRef = databaseReference.child(addressId)
                    addressRef.removeValue()
                        .addOnSuccessListener {
                            showToast(holder.itemView.context, "Address deleted successfully!")
                        }
                        .addOnFailureListener {
                            showToast(holder.itemView.context, "Failed to delete address.")
                        }
                }
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return addressList.size
    }
}
