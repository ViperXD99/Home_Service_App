package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.ReviewsAndRatings

object ReviewRating {
    private var review: ReviewsAndRatings? = null
    fun setReview(reviewData: ReviewsAndRatings){
        review = reviewData
    }
    fun getReview(): ReviewsAndRatings?{
        return review
    }
}