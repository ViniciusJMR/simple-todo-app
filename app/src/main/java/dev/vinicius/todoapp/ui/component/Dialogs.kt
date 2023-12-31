package dev.vinicius.todoapp.ui.component

import android.content.Context
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.vinicius.todoapp.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

object Dialogs {
    fun setupDatePickerDialog(
        parentFragmentManager: FragmentManager,
        onPositive: (String) -> (Unit) = {},
        onNegative: () -> (Unit) = {}
    ) {
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")

        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setNegativeButtonText(R.string.txt_erase)
            .build()
        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { millisecondsDate ->
            val date = Instant.ofEpochMilli(millisecondsDate)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                .toLocalDate()

            onPositive(date.format(dtf))
        }

        datePicker.addOnNegativeButtonClickListener {
            onNegative()
        }
    }

    /* Scraped idea for now
    fun setupTimePicker(
        parentFragmentManager: FragmentManager,
        onPositive: (String) -> (Unit) = { },
        onNegative: () -> (Unit) = {}

    ){
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(0)
            .setMinute(0)
            .build()

        timePicker.show(parentFragmentManager, "MATERIAL_TIME_PICKER")
        timePicker.addOnPositiveButtonClickListener {
            val dtf = DateTimeFormatter.ofPattern("HH:mm")
            val localTime = LocalTime.of(timePicker.hour, timePicker.minute).format(dtf)
            onPositive(localTime)
        }
        timePicker.addOnNegativeButtonClickListener{
            onNegative()
        }
    }
     */

    fun setupEditDialog(activity: FragmentActivity?,
                        context: Context?,
                        textOnEditText: String,
                        onChange: (EditText) -> (Unit)
    ){
        val editText = EditText(activity)
        editText.setText(textOnEditText)
        context?.let {
            val dialog = MaterialAlertDialogBuilder(it)
                .setTitle(R.string.txt_new_sub_todo_label)
                .setView(editText)
                .setPositiveButton("OK") { _, _ ->
                    onChange(editText)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

    fun setupDialog(
                    context: Context?,
                    title : String,
                    onChange: () -> (Unit)
    ){
        context?.let {
            val dialog = MaterialAlertDialogBuilder(it)
                .setTitle(title)
                .setPositiveButton("Continue") { _, _ ->
                    onChange()
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

}