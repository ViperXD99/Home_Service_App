package lk.nibm.hireupapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.adapter.BookingAdapter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BookingFragment : Fragment() {

    private lateinit var serviceProviderRecyclerView : RecyclerView
    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<BookingAdapter.ViewHolder>? = null

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_booking, container, false)
        initializeRecyclerView(view)
        return view
    }
    private fun initializeRecyclerView(view: View) {
        serviceProviderRecyclerView = view.findViewById(R.id.serviceProviderRecyclerView)
        layoutManager = LinearLayoutManager(requireContext())
        serviceProviderRecyclerView.layoutManager = layoutManager
        adapter = BookingAdapter(requireContext())
        serviceProviderRecyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}