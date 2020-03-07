package com.luizrodrigues.timesheetcalculator

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luizrodrigues.timesheetcalculator.databinding.ActivityTimesheetCalculatorBinding
import com.luizrodrigues.timesheetcalculator.mvvm.view.DialogNumberPickerFragment
import com.luizrodrigues.timesheetcalculator.mvvm.viewmodel.MainViewModel
import org.joda.time.LocalTime
import java.text.DecimalFormat
import kotlin.math.absoluteValue

class TimeSheetCalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimesheetCalculatorBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var horaChegada: LocalTime
    private lateinit var horaChegadaAlmoco: LocalTime
    private lateinit var horaSaidaAlmoco: LocalTime
    private lateinit var horaSaida: LocalTime
    private lateinit var horaBase: LocalTime
    private lateinit var horasTrabalhadas: LocalTime

    private var shouldCalculate = true

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

        binding.changeDaynight.setOnClickListener {
            onDayNightChangeClick()
        }

        verifyDayNight(true)

        mainViewModel.showNumberPickerDialog.observe(this, Observer {
            if (it.first) showNumberPickerDialog(it.second)
        })

        initSpinner()
        initObservable()
    }

    private fun onDayNightChangeClick() {
        verifyDayNight(false)
        recreate()
    }

    private fun verifyDayNight(isVerifyingColor: Boolean) {
        mainViewModel.showNumberPickerDialog.value = Pair(false, "")
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> { // Night mode is not active, we're using the light theme
                if (isVerifyingColor) {
                    binding.changeDaynight.setColorFilter(getColor(android.R.color.white))
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
            Configuration.UI_MODE_NIGHT_YES -> { // Night mode is active, we're using dark theme
                if (isVerifyingColor) {
                    binding.changeDaynight.setColorFilter(getColor(android.R.color.black))
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun showNumberPickerDialog(clickedField: String) {
        DialogNumberPickerFragment(mainViewModel, clickedField).show(
            supportFragmentManager,
            "numberPickerDialog"
        )
    }

    private fun initSpinner() {
        binding.horasBase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.horasBase.selectedItem == "06:00") {
                    binding.inputHoraSaidaAlmoco.setText("00:00")
                    binding.inputHoraChegadaAlmoco.setText("00:00")
                } else {
                    binding.inputHoraSaidaAlmoco.setText("12:00")
                    binding.inputHoraChegadaAlmoco.setText("13:00")
                }
                calculate()
            }
        }

        binding.horasTolerancia.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    calculate()
                }
            }

        // Tolerância e horas base default
        binding.horasBase.setSelection(1, false)
        binding.horasTolerancia.setSelection(1, false)
    }

    private fun initObservable() {
        mainViewModel.horaChegada.observe(this, Observer {
            calculate()
        })
        mainViewModel.horaChegadaAlmoco.observe(this, Observer {
            calculate()
        })
        mainViewModel.horaSaidaAlmoco.observe(this, Observer {
            calculate()
        })
        mainViewModel.horaSaida.observe(this, Observer {
            if (shouldCalculate) {
                calculate()
            } else {
                if (isValid()) {
                    if (initTime()) {
                        if (validateTime()) {
                            calcularHorasTrabalhadas()
                            calcularHorasExtrasNegativas()
                            calcularHorasApontamento()
                        }
                    }
                }
            }
        })
    }

    private fun calculate() {
        if (isValid()) {
            if (initTime()) {
                calcularHoraSaida()
                if (validateTime()) {
                    calcularHorasTrabalhadas()
                    calcularHorasExtrasNegativas()
                    calcularHorasApontamento()
                } else {
                    resetHorasTrabalhadasExtrasApontamento()
                }
            }
        }
    }

    private fun resetHorasTrabalhadasExtrasApontamento() {
        mainViewModel.horasTrabalhadas.value = ""
        mainViewModel.horasExtras.value = ""
        mainViewModel.horasApontamento.value = ""
    }

    private fun calcularHoraSaida() {
        shouldCalculate = false
        val horasChegadaSaidaAlmoco = horaChegadaAlmoco.minusMillis(horaSaidaAlmoco.millisOfDay)
        val totalHoras = horaChegada.plusMillis(horasChegadaSaidaAlmoco.millisOfDay)

        horaSaida = totalHoras.plusMillis(horaBase.millisOfDay)

        val hour = String.format("%02d", horaSaida.hourOfDay)
        val minute = String.format("%02d", horaSaida.minuteOfHour)
        mainViewModel.horaSaida.value = "$hour:$minute"
    }

    private fun isValid(): Boolean {
        var valid = false
        if (binding.inputHoraChegada.text.isNullOrBlank()) {
            binding.labelHoraChegada.error = getString(R.string.msg_erro_campo_obrigatorio)
        } else {
            binding.labelHoraChegada.error = null
            valid = true
        }
        if (binding.inputHoraChegadaAlmoco.text.isNullOrBlank()) {
            binding.labelHoraChegadaAlmoco.error = getString(R.string.msg_erro_campo_obrigatorio)
        } else {
            binding.labelHoraChegadaAlmoco.error = null
            valid = true
        }
        if (binding.inputHoraSaidaAlmoco.text.isNullOrBlank()) {
            binding.labelHoraSaidaAlmoco.error = getString(R.string.msg_erro_campo_obrigatorio)
        } else {
            binding.labelHoraSaidaAlmoco.error = null
            valid = true
        }
        if (binding.inputHoraSaida.text.isNullOrBlank()) {
            binding.labelHoraSaida.error = getString(R.string.msg_erro_campo_obrigatorio)
        } else {
            binding.labelHoraSaida.error = null
            valid = true
        }
        return valid
    }

    private fun initTime(): Boolean {
        try {
            // Hora e minuto base
            val horasBase = binding.horasBase.selectedItem.toString().trim().split(":")[0].toInt()
            val minutoBase = binding.horasBase.selectedItem.toString().trim().split(":")[1].toInt()
            horaBase = LocalTime(horasBase, minutoBase)

            // Hora e minuto chegado na empresa
            val horasChegada = mainViewModel.horaChegada.value!!.trim().split(":")[0].toInt()
            val minutoChegada =
                mainViewModel.horaChegada.value!!.trim().split(":")[1].toInt()
            horaChegada = LocalTime(horasChegada, minutoChegada)

            // Hora e minuto da saída para o almoço
            val horasSaidaAlmoco =
                mainViewModel.horaSaidaAlmoco.value!!.trim().split(":")[0].toInt()
            val minutoSaidaAlmoco =
                mainViewModel.horaSaidaAlmoco.value!!.trim().split(":")[1].toInt()
            horaSaidaAlmoco = LocalTime(horasSaidaAlmoco, minutoSaidaAlmoco)

            // Hora e minuto da chegada do almoço
            val horasChegadaAlmoco =
                mainViewModel.horaChegadaAlmoco.value!!.trim().split(":")[0].toInt()
            val minutoChegadaAlmoco =
                mainViewModel.horaChegadaAlmoco.value!!.trim().split(":")[1].toInt()
            horaChegadaAlmoco = LocalTime(horasChegadaAlmoco, minutoChegadaAlmoco)

            if (!mainViewModel.horaSaida.value!!.isBlank()) {
                // Hora e minuto da saída da empresa
                val horasSaida = mainViewModel.horaSaida.value!!.trim().split(":")[0].toInt()
                val minutoSaida =
                    mainViewModel.horaSaida.value!!.trim().split(":")[1].toInt()
                horaSaida = LocalTime(horasSaida, minutoSaida)
            }

            return true
        } catch (ex: Exception) {
            return false
        }
    }

    @Suppress("UNUSED_VALUE", "VARIABLE_WITH_REDUNDANT_INITIALIZER")
    private fun validateTime(): Boolean {
        var valid = false
        try {
            val horaChegadaInt =
                binding.inputHoraChegada.text.toString().trim().replace(":", "").toInt()
            val horaSaidaInt =
                binding.inputHoraSaida.text.toString().trim().replace(":", "").toInt()
            val horaChegadaAlmocoInt =
                binding.inputHoraChegadaAlmoco.text.toString().trim().replace(":", "").toInt()
            val horaSaidaAlmocoInt =
                binding.inputHoraSaidaAlmoco.text.toString().trim().replace(":", "").toInt()

            if (horaChegadaInt > horaSaidaInt || (horaChegadaInt > horaChegadaAlmocoInt && horaChegadaAlmocoInt != 0) || (horaChegadaInt > horaSaidaAlmocoInt && horaSaidaAlmocoInt != 0)) {
                binding.labelHoraChegada.error = "Hora chegada deve ser o menor valor!"
                valid = false
            } else {
                binding.labelHoraChegada.error = null
                valid = true
            }
            if (horaSaidaInt < horaChegadaInt || (horaSaidaInt < horaChegadaAlmocoInt && horaChegadaAlmocoInt != 0) || (horaSaidaInt < horaSaidaAlmocoInt && horaSaidaAlmocoInt != 0)) {
                binding.labelHoraSaida.error = "Hora saída deve ser o maior valor!"
                valid = false
            } else {
                binding.labelHoraSaida.error = null
                valid = true
            }
        } catch (ex: Exception) {
            valid = false
        }

        return valid
    }

    private fun calcularHorasTrabalhadas() {
        val horasChegadaSaida = horaSaida.minusMillis(horaChegada.millisOfDay)
        val horasChegadaSaidaAlmoco = horaChegadaAlmoco.minusMillis(horaSaidaAlmoco.millisOfDay)

        horasTrabalhadas = horasChegadaSaida.minusMillis(horasChegadaSaidaAlmoco.millisOfDay)

        horasTrabalhadas = calcularTolerancia()

        val hour = String.format("%02d", horasTrabalhadas.hourOfDay)
        val minute = String.format("%02d", horasTrabalhadas.minuteOfHour)
        mainViewModel.horasTrabalhadas.value = "$hour:$minute"
    }

    private fun calcularTolerancia(): LocalTime {
        val tolerancia =
            binding.horasTolerancia.selectedItem.toString().trim().replace(" min", "").toInt()
        val diferenca = calcularHorasExtrasNegativas()
        val totalHoras =
            "${diferenca.hourOfDay.absoluteValue}${diferenca.minuteOfHour.absoluteValue}".toInt()

        return if (totalHoras < tolerancia) {
            horaBase
        } else {
            horasTrabalhadas
        }
    }

    private fun calcularHorasExtrasNegativas(): LocalTime {
        val horasExtrasNegativas =
            if ((horasTrabalhadas.hourOfDay * 100 + horasTrabalhadas.minuteOfHour) >= (horaBase.hourOfDay * 100 + horaBase.minuteOfHour)) {
                binding.valueMinus.visibility = View.INVISIBLE
                horasTrabalhadas.minusMillis(horaBase.millisOfDay)
            } else {
                binding.valueMinus.visibility = View.VISIBLE
                horaBase.minusMillis(horasTrabalhadas.millisOfDay)
            }
        val hour = String.format("%02d", horasExtrasNegativas.hourOfDay)
        val minute = String.format("%02d", horasExtrasNegativas.minuteOfHour)
        mainViewModel.horasExtras.value = "$hour:$minute"
        return horasExtrasNegativas
    }

    private fun calcularHorasApontamento() {
        val minutosTrabalhados =
            DecimalFormat(".##").format(horasTrabalhadas.minuteOfHour.toDouble() / 60)

        mainViewModel.horasApontamento.value = "${horasTrabalhadas.hourOfDay}$minutosTrabalhados"
    }
}
