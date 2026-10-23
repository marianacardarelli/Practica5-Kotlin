package com.example.practica5

import android.app.DatePickerDialog
import android.icu.util.Calendar
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
    //declaramos la variable binding
    private lateinit var binding: ActivityMainBinding
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //y aquí también
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fun onResume(){
            super.onResume()
                super.onResume()
                if (::binding.isInitialized) actualizarFecha()
        }
    }
    private fun actualizarFecha() {
        //LocalDate.monthValue
        val hoy= LocalDate.now()
        val formateador = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault())
        binding.txtFechaActual.text = getString(R.string.hoy_es, formateador.format(hoy))
    }
}