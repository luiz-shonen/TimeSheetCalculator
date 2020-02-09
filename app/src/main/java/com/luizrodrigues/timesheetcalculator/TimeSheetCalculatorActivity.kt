package com.luizrodrigues.timesheetcalculator

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luizrodrigues.timesheetcalculator.databinding.ActivityTimesheetCalculatorBinding
import com.luizrodrigues.timesheetcalculator.mvvm.viewmodel.MainViewModel

class TimeSheetCalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimesheetCalculatorBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_calculator)

        mainViewModel = ViewModelProviders.of(this)
            .get(MainViewModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityTimesheetCalculatorBinding>(
            this,
            R.layout.activity_timesheet_calculator
        ).apply {
            this.lifecycleOwner = this@TimeSheetCalculatorActivity
            this.viewmodel = mainViewModel
        }

        mainViewModel.horaChegada.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            mainViewModel.horaSaida.value = "123123123"
        })

        binding.changeDaynight.setOnClickListener {
            onDayNightChangeClick()
        }

        verifyDayNight(true)
    }

    private fun onDayNightChangeClick() {
        verifyDayNight(false)
        recreate()
    }

    private fun verifyDayNight(isVerifyingColor: Boolean) {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> { // Night mode is not active, we're using the light theme
                if (isVerifyingColor) {
                    mainViewModel.corChangeDaynight.value = getColor(android.R.color.white)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
            Configuration.UI_MODE_NIGHT_YES -> { // Night mode is active, we're using dark theme
                if (isVerifyingColor) {
                    mainViewModel.corChangeDaynight.value = getColor(android.R.color.black)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}
