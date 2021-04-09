package com.techpassmaster.inappreviewapi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.techpassmaster.inappreviewapi.databinding.ActivityMainBinding

/**
 * Created by Techpass Master on 9-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        requestReviewInfo() // fun for Request a ReviewInfo object

        //  btn for stat review flow
        //  Shows rate app bottom sheet
        binding.btnInAppReview.setOnClickListener {
            startReviewFlow()
        }

    }

    private fun requestReviewInfo() {
        reviewManager = ReviewManagerFactory.create(this)

        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                reviewInfo = task.result    // We got the ReviewInfo object

            } else {
                // There was some problem,
                // just show alert dialog on error.
                showRateAppAlertDialog()
            }
        }
    }

    private fun startReviewFlow() {
        val flow = reviewManager.launchReviewFlow(this, reviewInfo)
        flow.addOnCompleteListener { _ ->
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
            Toast.makeText(this, "Reviwe flow start", Toast.LENGTH_SHORT).show()

        }
    }

    // show alert dialog and redirect user to play store for review the app.
    private fun showRateAppAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Rate App")
        alertDialog.setMessage("If you like our app, please rate this app on play store.")

        alertDialog.setPositiveButton("Rate App") { _, _ -> }
        alertDialog.setNegativeButton("No, Thanks") { _, _ -> }

        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}