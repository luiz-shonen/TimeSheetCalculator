package com.luizrodrigues.timesheetcalculator.mvvm.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.luizrodrigues.timesheetcalculator.R
import com.luizrodrigues.timesheetcalculator.mvvm.model.enums.FieldsEnum
import com.luizrodrigues.timesheetcalculator.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_dialog_number_picker.view.*
import org.joda.time.LocalTime

class DialogNumberPickerFragment(
    private val mainViewModel: MainViewModel,
    private val clickedField: String
) : DialogFragment() {

    private val hour = MutableLiveData<Int>()
    private val minute = MutableLiveData<Int>()

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

        // Change format of number in number picker
        view.hour.setFormatter { String.format("%02d", it) }
        view.minute.setFormatter { String.format("%02d", it) }

        view.btn_salvar.setOnClickListener { onSalvarClick(it) }

        // Initialize hour and minute with LocalTime
        val localTime = LocalTime.now()
        view.hour.value = localTime.hourOfDay
        view.minute.value = localTime.minuteOfHour

        hour.observe(this, Observer { buildTime(it) })
        minute.observe(this, Observer { buildTime(it) })

        view.hour.setOnValueChangedListener { _, _, newVal -> hour.value = newVal }
        view.minute.setOnValueChangedListener { _, _, newVal -> minute.value = newVal }

        return view
    }

    private fun buildTime(newVal: Int) {
        val field = when (clickedField) {
            FieldsEnum.HORA_CHEGADA.value -> mainViewModel.horaChegada
            FieldsEnum.HORA_SAIDA_ALMOCO.value -> mainViewModel.horaSaidaAlmoco
            FieldsEnum.HORA_CHEGADA_ALMOCO.value -> mainViewModel.horaChegadaAlmoco
            FieldsEnum.HORA_SAIDA.value -> mainViewModel.horaSaida
            else -> null
        }
        field!!.value = newVal.toString()
    }

    private fun onSalvarClick(view: View) {

    }
}