package lk.nibm.hireupapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.ChatSpAdapter
import lk.nibm.hireupapp.adapter.OrderAdapter
import lk.nibm.hireupapp.model.Category
import lk.nibm.hireupapp.model.ChatSp
import lk.nibm.hireupapp.model.ServiceProviders
import lk.nibm.hireupapp.model.User


class ChatSpFragment : Fragment() {

    private lateinit var spChatRecyclerView : RecyclerView
    private lateinit var view : View
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var adapter : ChatSpAdapter //RecyclerView.Adapter<ChatSpAdapter.ViewHolder>? = null
    private var chatSpList = mutableListOf<ChatSp>()
    private var serviceNameList = mutableListOf<String>()
    private var serviceProviderList = mutableListOf<ServiceProviders>()
    //private var userList = mutableListOf<User>()


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
        view= inflater.inflate(R.layout.fragment_chat, container, false)
        initializeRecyclerView()
        return view
    }

    private fun initializeRecyclerView(){

        spChatRecyclerView = view.findViewById(R.id.spChatRecyclerView)
        layoutManager = LinearLayoutManager(requireContext())
        spChatRecyclerView.layoutManager = layoutManager
        adapter = ChatSpAdapter(chatSpList,serviceNameList, serviceProviderList)
        spChatRecyclerView.adapter = adapter

        val database = FirebaseDatabase.getInstance()
        val chatSpRef = database.getReference("Chat").child("Chat_Sp")
        val serviceCategoriesRef = database.getReference("Service Categories")
        val serviceProviderRef = database.getReference("Service_Providers")
        val userRef = database.getReference("Users")

        chatSpRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatSpList.clear()
                serviceNameList.clear()
                serviceProviderList.clear()
                //userList.clear()


                snapshot.children.forEach { chatSpSnapshot ->
                    val chatSp = chatSpSnapshot.getValue(ChatSp::class.java)
                    chatSp?.let {
                        chatSpList.add(it)
                        val userSpId = chatSp.userId
                        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

                        if (userSpId == currentUserId){
                           // Toast.makeText(requireContext(), "User: ${currentUserId}", Toast.LENGTH_SHORT).show()
                            val serviceID = chatSp.id
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
                                        //adapter.notifyDataSetChanged()

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle error
                                    }
                                })
                            }

                            val serviceProviderId = chatSp.providerId
                            if (serviceProviderId != null) {
                                serviceProviderRef.child(serviceProviderId).addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val serviceProvider = snapshot.getValue(ServiceProviders::class.java)
                                        serviceProvider?.let {
                                            serviceProviderList.add(it)
                                            updateRecyclerView()
                                        }
                                        //adapter.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })
                            }
                        }

//                        val userSpId = chatSp.userId
//                        if (userSpId != null) {
//                            userRef.child(userSpId).addListenerForSingleValueEvent(object : ValueEventListener{
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    val user = snapshot.getValue(User::class.java)
//                                    val userId = user?.userId
//                                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
//                                    if (userId == currentUserId) {
//                                        user?.let {
//                                            Toast.makeText(requireContext(), "User: ${currentUserId}", Toast.LENGTH_SHORT).show()
////                                            userList.add(it)
////                                            updateRecyclerView()
//                                        }
//                                    }
////                                    user?.let {
////                                        userList.add(it)
////                                        updateRecyclerView()
////                                    }
//                                    //adapter.notifyDataSetChanged()
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                    // Handle error
//                                }
//                            })
//                        }


                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    private fun updateRecyclerView() {
        if (chatSpList.isNotEmpty()  && serviceProviderList.isNotEmpty() && serviceNameList.isNotEmpty()  &&
             chatSpList.size == serviceProviderList.size && chatSpList.size == serviceNameList.size
        ) {
            adapter = ChatSpAdapter(chatSpList,serviceNameList, serviceProviderList)
            spChatRecyclerView.adapter = adapter
        }
    }

}