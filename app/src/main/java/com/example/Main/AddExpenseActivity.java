package com.example.Main;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.example.monthlyexpensestracker.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public TextView expenseDateInputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Create the expenseDateInputTextView object.
        expenseDateInputTextView = findViewById(R.id.expenseDateInputTextView);
    }

    public void addNewExpense(View view) {
        Toast.makeText(this, "TEST ADD", Toast.LENGTH_SHORT).show();
    }

    public void openDatePickerDialog(View view) {

        // Create a new DialogFragment object.
        DialogFragment dialogFragment = new DatePickerFragment();

        // Display the dialog, adding the fragment to the given FragmentManager.
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    // The method onDateSet will be called when the user clicks "OK" on the DatePickerDialog.
    // This method will set the text of expenseDateInputTextView to the date chosen by the user.
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        // Use the parameters passed (year, month, & dayOfMonth) to create a new Calendar object.
        Calendar calendar = Calendar.getInstance();     // getInstance() - Gets a calender using the default time and locale.
        calendar.set(year, month, dayOfMonth);     // Sets the Calendar time to the passed parameters.

        // Create a new DateFormat object.
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);     // getDateInstance() - Gets the normal date format for that country.

        // Create a String variable that holds the date selected by the user.
        String dateSelected = dateFormat.format( calendar.getTime() );

        // Set the text of expenseDateInputTextView to dateSelected.
        expenseDateInputTextView.setText(dateSelected);
    }
}