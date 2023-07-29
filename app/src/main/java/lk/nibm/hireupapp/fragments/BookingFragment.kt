package lk.nibm.hireupapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.OrderAdapter
import lk.nibm.hireupapp.model.Category
import lk.nibm.hireupapp.model.Order
import lk.nibm.hireupapp.model.ServiceProviders


class BookingFragment : Fragment() {

    private lateinit var serviceProviderRecyclerView : RecyclerView
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var view : View
    private lateinit var adapter: OrderAdapter
    private val orderList = mutableListOf<Order>()
    private val serviceNameList = mutableListOf<String>()
    private val providerList = mutableListOf<ServiceProviders>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking, container, false)
        initializeRecyclerView()
        return view
    }

    private fun initializeRecyclerView() {
        serviceProviderRecyclerView = view.findViewById(R.id.serviceProviderRecyclerView)
        layoutManager = LinearLayoutManager(requireContext())
        serviceProviderRecyclerView.layoutManager = layoutManager
        adapter = OrderAdapter(orderList, serviceNameList, providerList)
        serviceProviderRecyclerView.adapter = adapter

        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("Orders")
        val serviceCategoriesRef = database.getReference("Service Categories")
        val serviceProviderRef = database.getReference("Service_Providers")


        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                orderList.clear()
                serviceNameList.clear()
                providerList.clear()

                dataSnapshot.children.forEach { orderSnapshot ->
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.let {
                        orderList.add(it)

                        // Fetch serviceName from ServiceCategories based on serviceID
                        val serviceID = order.serviceID
                        if (serviceID != null) {
                            serviceCategoriesRef.child(serviceID).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(serviceCategorySnapshot: DataSnapshot) {
                                    val serviceCategory = serviceCategorySnapshot.getValue(Category::class.java)
                                    val serviceName = serviceCategory?.name
                                    serviceName?.let {
                                        serviceNameList.add(it)
                                        updateRecyclerView()
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }

                        val providerID = order.providerID
                        if (providerID != null) {
                            serviceProviderRef.child(providerID).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(providerSnapshot: DataSnapshot) {
                                    val provider = providerSnapshot.getValue(ServiceProviders::class.java)
                                    provider?.let {
                                        providerList.add(it)
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
        // Check if all lists have data and their sizes match
        if (orderList.isNotEmpty() && serviceNameList.isNotEmpty() && providerList.isNotEmpty() &&
            orderList.size == serviceNameList.size && orderList.size == providerList.size
        ) {
            adapter = OrderAdapter(orderList, serviceNameList, providerList)
            serviceProviderRecyclerView.adapter = adapter
        }
    }
}
