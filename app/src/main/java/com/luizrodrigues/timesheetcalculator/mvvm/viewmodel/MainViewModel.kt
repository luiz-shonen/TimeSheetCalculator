package com.luizrodrigues.timesheetcalculator.mvvm.viewmodel

import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luizrodrigues.timesheetcalculator.util.MaskWatcher

class MainViewModel : ViewModel() {
    var horaChegada = MutableLiveData<String>()
    var horaSaida = MutableLiveData<String>()
    var horaChegadaAlmoco = MutableLiveData<String>()
    var horaSaidaAlmoco = MutableLiveData<String>()
    var horasTrabalhadas = MutableLiveData<String>()
    var horasExtras = MutableLiveData<String>()
    var horasApontamento = MutableLiveData<String>()
    var corChangeDaynight = MutableLiveData<Int>()
    var showNumberPickerDialog = MutableLiveData<Pair<Boolean, String>>(Pair(false, ""))
    var hourWatcher: TextWatcher = MaskWatcher.buildTime()

    fun onResetClick() = this.clearAllFields()

    private fun clearAllFields() {
        this.horaChegada.value = ""
        this.horaSaida.value = ""
        this.horaChegadaAlmoco.value = ""
        this.horaSaidaAlmoco.value = ""
        this.horasTrabalhadas.value = ""
        this.horasExtras.value = ""
        this.horasApontamento.value = ""
        this.showNumberPickerDialog.value = Pair(false, "")
    }

    fun openNumberPickerDialog(clickedField: String) {
        showNumberPickerDialog.value = Pair(true, clickedField)
    }
}