package com.luizrodrigues.timesheetcalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luizrodrigues.timesheetcalculator.databinding.ActivityTimesheetCalculatorBinding
import com.luizrodrigues.timesheetcalculator.mvvm.viewmodel.MainViewModel

class TimeSheetCalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimesheetCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_calculator)

        val mainViewModel = ViewModelProviders.of(this)
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
    }
}
