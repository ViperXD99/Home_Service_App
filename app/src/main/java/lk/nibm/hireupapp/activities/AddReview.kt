package lk.nibm.hireupapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import lk.nibm.hireupapp.R

class AddReview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        val btnSubmit = findViewById<Button>(R.id.submit) as Button
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar) as RatingBar

        btnSubmit.setOnClickListener {
            val getRatingValue = ratingBar.rating
            Toast.makeText(
                this@AddReview, "Rating =" + getRatingValue, Toast.LENGTH_LONG
            ).show()

        }
    }
}