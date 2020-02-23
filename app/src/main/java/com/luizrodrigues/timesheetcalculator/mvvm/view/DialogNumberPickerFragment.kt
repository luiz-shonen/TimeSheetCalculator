package com.luizrodrigues.timesheetcalculator.mvvm.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.luizrodrigues.timesheetcalculator.R
import com.luizrodrigues.timesheetcalculator.mvvm.model.enums.FieldsEnum
import com.luizrodrigues.timesheetcalculator.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_dialog_number_picker.view.*
import org.joda.time.LocalTime

class DialogNumberPickerFragment(
    private val mainViewModel: MainViewModel,
    private val clickedField: String
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

        // Change format of number in number picker
        view.hour.setFormatter { String.format("%02d", it) }
        view.minute.setFormatter { String.format("%02d", it) }

        initTime(view)

        view.btn_salvar.setOnClickListener { onSalvarClick(view) }

        return view
    }

    private fun getClickedField(): MutableLiveData<String>? {
        return when (clickedField) {
            FieldsEnum.HORA_CHEGADA.value -> mainViewModel.horaChegada
            FieldsEnum.HORA_SAIDA_ALMOCO.value -> mainViewModel.horaSaidaAlmoco
            FieldsEnum.HORA_CHEGADA_ALMOCO.value -> mainViewModel.horaChegadaAlmoco
            FieldsEnum.HORA_SAIDA.value -> mainViewModel.horaSaida
            else -> null
        }
    }

    private fun initTime(view: View) {
        // Initializes hour and minute with LocalTime if it don't have a value
        // or initializes with it's value if it has a value
        if (getClickedField()!!.value!!.isBlank()) {
            val localTime = LocalTime.now()
            view.hour.value = localTime.hourOfDay
            view.minute.value = localTime.minuteOfHour
        } else {
            val clickedFieldValue = getClickedField()!!.value!!.split(":")
            view.hour.value = clickedFieldValue[0].toInt()
            view.minute.value = clickedFieldValue[1].toInt()
        }
    }

    private fun onSalvarClick(view: View) {
        val hour = String.format("%02d", view.hour.value)
        val minute = String.format("%02d", view.minute.value)
        getClickedField()!!.value = "$hour:$minute"
        dialog!!.cancel()
    }
}