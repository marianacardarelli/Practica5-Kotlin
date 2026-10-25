package com.example.practica5

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practica5.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Evento del botón: abre el calendario
        binding.btnFecha.setOnClickListener {
            val hoy = LocalDate.now()
            DatePickerDialog(
                this,
                { _, anioSel, mesSel, diaSel ->
                    // mesSel va de 0 a 11, por eso mesSel + 1
                    mostrarEdad(diaSel, mesSel + 1, anioSel)
                },
                hoy.year, hoy.monthValue - 1, hoy.dayOfMonth
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }.show()
        }
    }

    // Se ejecuta cada vez que la app vuelve a primer plano
    override fun onResume() {
        super.onResume()
        actualizarFecha()
    }

    private fun actualizarFecha() {
        val hoy = LocalDate.now()
        val formateador = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault())
        binding.txtFechaActual.text = getString(R.string.hoy_es, formateador.format(hoy))
    }

    private fun mostrarEdad(diaNac: Int, mesNac: Int, anioNac: Int) {
        val hoy = LocalDate.now()
        val resultado = calculaDias(
            hoy.monthValue, mesNac,
            hoy.dayOfMonth, diaNac,
            hoy.year, anioNac
        )
        binding.txtFechaNacimiento.text =
            getString(R.string.naciste_el, "$diaNac/$mesNac/$anioNac")
        binding.txtEdad.text =
            getString(R.string.edad_resultado, resultado[0], resultado[1])
    }

    private fun calculaDias(
        mesActual: Int,
        mesNacimiento: Int,
        diaActual: Int,
        diaNacimiento: Int,
        anhoActual: Int,
        anhoNacimiento: Int
    ): IntArray {
        var dias = 0
        val calculo = IntArray(2)
        var anhos = anhoActual - anhoNacimiento
        if (mesActual >= mesNacimiento) {
            for (i in mesNacimiento until mesActual) dias += numeroDeDias(i, anhoActual)
            if (diaActual < diaNacimiento && mesActual == mesNacimiento) {
                anhos--
                dias = 365 + diaActual - diaNacimiento
            } else {
                dias += diaActual - diaNacimiento
            }
        } else {
            anhos--
            dias = diasRestantes(diaNacimiento, mesNacimiento, anhoActual - 1)
            val veces = 12 + mesActual - mesNacimiento - 1
            var mesVer = mesNacimiento + 1
            var anhoVer = anhoActual - 1
            for (i in 1..veces) {
                if (mesVer > 12) {
                    mesVer = 1
                    anhoVer++
                }
                dias += numeroDeDias(mesVer, anhoVer)
                mesVer++
            }
            dias += diaActual
        }
        calculo[0] = anhos
        calculo[1] = dias
        return calculo
    }

    private fun esBisiesto(anho: Int): Boolean {
        return anho > 1582 && (anho % 400 == 0 || (anho % 4 == 0 && anho % 100 != 0))
    }

    private fun numeroDeDias(m: Int, a: Int): Int {
        return when (m) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            else -> if (esBisiesto(a)) 29 else 28
        }
    }

    private fun diasRestantes(dia: Int, mes: Int, anho: Int): Int {
        return numeroDeDias(mes, anho) - dia
    }
}