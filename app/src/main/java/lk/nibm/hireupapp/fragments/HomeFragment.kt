package lk.nibm.hireupapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
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
import lk.nibm.hireupapp.activities.SignIn
import lk.nibm.hireupapp.adapter.CategoryAdapter
import lk.nibm.hireupapp.common.UserDataManager
import lk.nibm.hireupapp.databinding.FragmentHomeBinding
import lk.nibm.hireupapp.model.Category


class HomeFragment : Fragment() {
    private lateinit var categoryRecyclerView : RecyclerView
    private var layoutManager : RecyclerView.LayoutManager? = null
    private lateinit var view : View
    private lateinit var adapter: CategoryAdapter
    private val itemList = mutableListOf<Category>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var imgProfile : ImageView
    private lateinit var loggedUserName : TextView
    private lateinit var seeAllCategories : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)
        initializeComponents()
        categoryRecyclerView()
        loadProfileDetails()
        clickListeners()
        return view
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
    }

    private fun loadProfileDetails() {
        val userdata = UserDataManager.getUser()
        if (userdata != null){
            val imageUrl = userdata.photoUrl
            Glide.with(this)
                .load(imageUrl)
                .into(imgProfile)
            loggedUserName.text = userdata.displayName
        }
        else{
            Toast.makeText(requireContext(), "Sign In SuccessFul!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun categoryRecyclerView() {
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.layoutManager = layoutManager
        adapter = CategoryAdapter(itemList)
        categoryRecyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().reference.child("Service Categories")
        databaseReference.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
               itemList.clear()
                for (itemSnapshot in snapshot.children){
                    val item = itemSnapshot.getValue(Category::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that may occur while fetching data
            }
        })
    }

}