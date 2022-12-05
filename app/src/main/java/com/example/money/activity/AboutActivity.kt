package com.example.money.activity

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import com.example.money.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.money.db.MyDBHelper

class AboutActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private lateinit var myDBHelper: MyDBHelper
    private lateinit var myDB: SQLiteDatabase

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.hide()

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val findMyMoneyButton = findViewById<ToggleButton>(R.id.find_my_money_button)
        val emailButton = findViewById<ImageButton>(R.id.email_button)
        myDBHelper = MyDBHelper(this)
        myDB = myDBHelper.readableDatabase
        val rs = myDB.rawQuery("SELECT rating FROM settings", null)

        if (rs.moveToFirst()) {
            try {
                ratingBar.rating = rs.getFloat(0)
            } catch (_: Exception) {
            }

        } else {
            Toast.makeText(
                this,
                getString(R.string.database_error_toast), Toast.LENGTH_LONG
            ).show()
        }
        //ratingbar
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            saveRating(rating)
        }

        //email
        emailButton.setOnClickListener {
            onWriteEmail(it)
        }

        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        findMyMoneyButton.isVisible = isFlashAvailable

        //findMyMoney
        findMyMoneyButton.setOnCheckedChangeListener { _, isChecked ->

            if (!isFlashAvailable) {
                showAlert(getString(R.string.money_find_error))
                return@setOnCheckedChangeListener
            }
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                cameraId = cameraManager.cameraIdList[0]
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
            switchFlashLight(isChecked)
        }

    }

    private fun switchFlashLight(status: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, status)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun showAlert(message: String) {
        val alert = AlertDialog.Builder(this)
            .create()
        alert.setTitle("Oops!")
        alert.setMessage(getString(R.string.money_find_error))
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.positive_button_okay))
        { _, _ -> this.finish() }
        alert.show()
    }

    private fun onWriteEmail(view: View) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:davidpokolol@gmail.com")

            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.bugfix_mail_subject))
        }
        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            showAlert(getString(R.string.email_error))
        }

    }

    override fun onBackPressed() {
        myDB.close()
        myDBHelper.close()
        super.onBackPressed()
        this.finish()
    }

    private fun saveRating(rating : Float) {
        val cv = ContentValues()
        cv.put("rating", rating)
        myDB.update("settings", cv, null, null)
    }
}