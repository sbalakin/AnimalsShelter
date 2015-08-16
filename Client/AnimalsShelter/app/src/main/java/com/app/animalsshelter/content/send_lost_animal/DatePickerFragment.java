package com.app.animalsshelter.content.send_lost_animal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.app.animalsshelter.R;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.setCancelable(true);
        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        int monthOfYear = month + 1;
        ((EditText) getActivity().findViewById(R.id.editTextDate)).setText(day + "/" + monthOfYear + "/" + year);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        ((EditText) getActivity().findViewById(R.id.editTextDate)).setText("");
    }
}
