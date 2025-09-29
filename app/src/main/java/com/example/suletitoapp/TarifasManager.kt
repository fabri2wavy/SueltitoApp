package com.example.suletitoapp

import java.util.Calendar

object TarifasManager {
    fun esHorarioNocturno(): Boolean {
        val calendar = Calendar.getInstance()
        val hora = calendar.get(Calendar.HOUR_OF_DAY)
        return hora >= 21 || hora < 6 // De 9 PM a 6 AM
    }

    object Minibus {
        fun getPasajeCorto(): Double = if (esHorarioNocturno()) 2.5 else 2.4
        fun getPasajeLargo(): Double = if (esHorarioNocturno()) 3.2 else 3.0
        const val PASAJE_CORTO_PREFERENCIAL = 2.0
        const val PASAJE_LARGO_PREFERENCIAL = 2.6
    }

    object Trufi {
        fun getZonal(): Double = if (esHorarioNocturno()) 3.0 else 2.5
        fun getCorto(): Double = if (esHorarioNocturno()) 3.3 else 2.8
        fun getLargo(): Double = if (esHorarioNocturno()) 3.5 else 3.3
        fun getExtraLargo(): Double = if (esHorarioNocturno()) 4.0 else 3.5

        const val ZONAL_PREFERENCIAL = 2.0
        const val CORTO_PREFERENCIAL = 2.5
        const val LARGO_PREFERENCIAL = 3.0
        const val EXTRA_LARGO_PREFERENCIAL = 3.5
    }

    fun getHorarioTexto(): String {
        return if (esHorarioNocturno()) "Tarifa Nocturna" else "Tarifa Diurna"
    }

}