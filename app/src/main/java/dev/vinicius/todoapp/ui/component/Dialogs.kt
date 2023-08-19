package dev.vinicius.todoapp.ui.component

import android.app.Activity
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.vinicius.todoapp.R
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