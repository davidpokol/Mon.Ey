package com.example.money
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.money.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic var goal = null
    }
    var curr = Curreny.FORINT

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.dateTextView.text = getDateString()

        val currentHour = getHour()
        if(currentHour < 9 ){
            binding.greetingTextView.text = getString(R.string.greeting_morning)
        } else if (currentHour < 18) {

            binding.greetingTextView.text = getString(R.string.greeting_afternoon)
        } else {

            binding.greetingTextView.text = getString(R.string.greeting_night)
        }

        if(goal==null) {
            binding.goalTextView.text = getString(R.string.set_goal_warning)
        } else {
            binding.goalTextView.text = getMonth() + " még ennyit költhetsz:\n" + goal +" "+ curr
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateString(): String {

        val sdf = SimpleDateFormat("yyyy.MM.dd.")
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    private fun getHour(): Int {

        val sdf = SimpleDateFormat("HH")
        val currentDate = sdf.format(Date())
        return currentDate.toInt()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMonth(): String {
        val sdf = SimpleDateFormat("MM")
        val currentDate = sdf.format(Date())
        return when(currentDate.toInt()) {
            1 -> getString(R.string.in_month_january)
            2 -> getString(R.string.in_month_february)
            3 -> getString(R.string.in_month_march)
            4 -> getString(R.string.in_month_april)
            5 -> getString(R.string.in_month_may)
            6 -> getString(R.string.in_month_june)
            7 -> getString(R.string.in_month_july)
            8 -> getString(R.string.in_month_august)
            9 -> getString(R.string.in_month_september)
            10 -> getString(R.string.in_month_october)
            11 -> getString(R.string.in_month_november)
            12 -> getString(R.string.in_month_december)
            else -> {""}
        }
    }
}