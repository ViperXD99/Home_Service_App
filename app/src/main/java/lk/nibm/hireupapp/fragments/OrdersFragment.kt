package lk.nibm.hireupapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.Cart
import lk.nibm.hireupapp.adapter.OrderAdapter
import lk.nibm.hireupapp.adapter.ShopOrdersAdapter
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.databinding.FragmentOrdersBinding
import lk.nibm.hireupapp.model.Category
import lk.nibm.hireupapp.model.Hardware
import lk.nibm.hireupapp.model.Order
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.ShopOrderDetails
import lk.nibm.hireupapp.model.ShopOrders

class OrdersFragment : Fragment() {
    private lateinit var binding : FragmentOrdersBinding
    private lateinit var shopOrdersRecyclerView : RecyclerView
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var view : View
    private lateinit var adapter: ShopOrdersAdapter
    private val shopOrderList = mutableListOf<ShopOrderDetails>()
    private val hardwareList = mutableListOf<Hardware>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        onClickListeners()
        ordersRecyclerView()
        return binding.root



    }

    private fun ordersRecyclerView() {
        shopOrdersRecyclerView = binding.ordersRecyclerView
        layoutManager = LinearLayoutManager(requireContext())
        shopOrdersRecyclerView.layoutManager = layoutManager
        adapter = ShopOrdersAdapter(shopOrderList, hardwareList)
        shopOrdersRecyclerView.adapter = adapter
        val userID = UserDataManager.getUser()
        val database = FirebaseDatabase.getInstance()
        val shopOrdersRef = database.getReference("Shop").child("Shop Orders")
        val hardwaresRef = database.getReference("Shop").child("Hardware")
        val query: Query = shopOrdersRef.orderByChild("customerID").equalTo(userID?.userId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                shopOrderList.clear()
                hardwareList.clear()

                dataSnapshot.children.forEach { orderSnapshot ->
                    val shopOrder = orderSnapshot.getValue(ShopOrderDetails::class.java)
                    shopOrder?.let {
                        shopOrderList.add(it)

                        // Fetch serviceName from ServiceCategories based on serviceID
                        val hardwareID = shopOrder.hardwareID
                        if (hardwareID != null) {
                            hardwaresRef.child(hardwareID).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(hardwareSnapshot: DataSnapshot) {
                                    val hardware = hardwareSnapshot.getValue(Hardware::class.java)
                                    hardware?.let {
                                        hardwareList.add(it)
                                        updateRecyclerView()
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateRecyclerView() {
        if (shopOrderList.isNotEmpty() && hardwareList.isNotEmpty()  &&
            shopOrderList.size == hardwareList.size
        ) {
            adapter = ShopOrdersAdapter(shopOrderList, hardwareList)
            shopOrdersRecyclerView.adapter = adapter
        }
    }

    private fun onClickListeners() {
        binding.btnCart.setOnClickListener {
        val  intent = Intent(activity, Cart::class.java)
            startActivity(intent)
        }
    }

}
