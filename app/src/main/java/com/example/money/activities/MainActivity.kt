package com.example.money.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.money.R
import com.example.money.databinding.ActivityMainBinding
import com.example.money.model.Money
import com.example.money.util.DateTimeUtil

class MainActivity : AppCompatActivity() {

    private val dateTimeUtil = DateTimeUtil()
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.dateTextView.text = dateTimeUtil.getDateString()

        val currentHour = dateTimeUtil.getHour()
        if (currentHour < 9) {
            binding.greetingTextView.text = getString(R.string.greeting_morning)
        } else if (currentHour < 18) {

            binding.greetingTextView.text = getString(R.string.greeting_afternoon)
        } else {

            binding.greetingTextView.text = getString(R.string.greeting_night)
        }

        if (Money.getGoal() == null || Money.getGoal() == 0) {
            binding.goalTextView.text = getString(R.string.set_goal_warning)
        } else {

            if (Money.getSpendableMoney() == 0) {
                binding.goalTextView.text = getString(R.string.limit_reached_warning)
            } else if (Money.getSpendableMoney() < 0) {
                binding.goalTextView.text = getString(R.string.limit_exceeded_warning)
            } else {
                binding.goalTextView.text = " még ennyit költhetsz:\n" +
                        Money.getGoal() + " " + Money.getCurrency()
            }
        }

        binding.tabImageButton.setOnClickListener {

            binding.goalTextView.text = ""
        }
    }
}