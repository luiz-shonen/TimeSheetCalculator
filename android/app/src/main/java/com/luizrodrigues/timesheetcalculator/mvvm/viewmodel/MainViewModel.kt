package com.luizrodrigues.timesheetcalculator.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var horaChegada = MutableLiveData<String>()
    var horaSaida = MutableLiveData<String>()
    var horaChegadaAlmoco = MutableLiveData<String>()
    var horaSaidaAlmoco = MutableLiveData<String>()
    var horasTrabalhadas = MutableLiveData<String>()
    var horasExtras = MutableLiveData<String>()
    var horasApontamento = MutableLiveData<String>()
    var corChangeDaynight = MutableLiveData<Int>()
    val mask: String = "##:##"

    fun onResetClick() = this.clearAllFields()

    private fun clearAllFields() {
        this.horaChegada.value = ""
        this.horaSaida.value = ""
        this.horaChegadaAlmoco.value = ""
        this.horaSaidaAlmoco.value = ""
        this.horasTrabalhadas.value = ""
        this.horasExtras.value = ""
        this.horasApontamento.value = ""
    }

}