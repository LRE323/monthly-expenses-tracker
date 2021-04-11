package com.example.monthlyexpensestracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.monthlyexpensestracker.AddExpenseActivity;

import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private TextView textView;
    private View view;
    private LayoutInflater inflater;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}
