package com.example.money.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.money.R


class ExchangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)
        supportActionBar?.hide()

    }
}