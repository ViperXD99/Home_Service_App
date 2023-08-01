package lk.nibm.hireupapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.Cart

class OrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        val shopHomeButton = view.findViewById<Button>(R.id.shopHomeButton)
        shopHomeButton.setOnClickListener {
            navigateToShopHome()
        }

        return view
    }

    private fun navigateToShopHome() {
        // Add code to navigate to the ShopHome activity here

        val intent = Intent(activity, Cart::class.java)
        startActivity(intent)
    }
}
