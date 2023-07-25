package lk.nibm.hireupapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import lk.nibm.hireupapp.R
import lk.nibm.hireupapp.activities.PersonalInformation

class ProfileFragment : Fragment() {
    private lateinit var view : View
    private lateinit var personalInfoTab : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_profile, container, false)
        initializeComponents()
        clickListeners()
        return view
    }

    private fun clickListeners() {
        personalInfoTab.setOnClickListener {
            val intent = Intent(requireContext(), PersonalInformation::class.java)
            startActivity(intent)
        }

    }

    private fun initializeComponents() {
        personalInfoTab = view.findViewById(R.id.personalInfoTab)

    }


}