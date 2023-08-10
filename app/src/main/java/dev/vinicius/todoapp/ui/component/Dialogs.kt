package dev.vinicius.todoapp.ui.component

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

object Dialogs {
    fun setupDatePickerDialog(
        parentFragmentManager: FragmentManager,
        onPositive: (LocalDate) -> (Unit) = {},
        onNegative: (Unit) -> (Unit) = {}
    ) {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { millisecondsDate ->
            val date = Instant.ofEpochMilli(millisecondsDate)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                .toLocalDate()

            onPositive(date)
        }
    }
}