package com.luizrodrigues.timesheetcalculator.mvvm.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.luizrodrigues.timesheetcalculator.R
import com.luizrodrigues.timesheetcalculator.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_dialog_number_picker.view.*
import org.joda.time.LocalTime

class DialogNumberPickerFragment(
    private val mainViewModel: MainViewModel,
    clickedField: String
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.fragment_dialog_number_picker, container, false)
        view.hour.minValue = 0
        view.hour.maxValue = 23
        view.minute.minValue = 0
        view.minute.maxValue = 59

        val localTime = LocalTime.now()

        view.hour.value = localTime.hourOfDay
        view.minute.value = localTime.minuteOfHour

        // to change format of number in number picker
        view.hour.setFormatter { String.format("%02d", it) }
        view.minute.setFormatter { String.format("%02d", it) }

        view.hour.setOnValueChangedListener { _, _, newVal ->
            hourValueChangeListener(newVal)
        }
        view.minute.setOnValueChangedListener { _, _, newVal ->
            minuteValueChangeListener(newVal)
        }

        view.btn_salvar.setOnClickListener { onSalvarClick(it) }

        return view
    }

    private fun hourValueChangeListener(newVal: Int) {
        mainViewModel.horaChegada.value = "00:00"
    }

    private fun minuteValueChangeListener(newVal: Int) {

    }

    private fun onSalvarClick(view: View) {

    }
}