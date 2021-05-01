package com.example.Main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.Main.ExpenseRoom.Expense;
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

    private TextView expenseNameInputEditText;

    private TextView expenseAmountInputEditText;

    private TextView expenseDateInputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Create all the input objects.
        expenseNameInputEditText = findViewById(R.id.expenseNameInput);
        expenseAmountInputEditText = findViewById(R.id.expenseAmountInput);
        expenseDateInputTextView = findViewById(R.id.expenseDateInput);
    }

    public void addExpense(View view) {

        // Ensure the information provided by the user is valid.
        if ( !expenseInputIsValid() ) {
            return;
        } else {

            // Create the new Expense.
            String expenseNameInputAsString = expenseNameInputEditText.getText().toString();
            double expenseAmountInputAsDouble = Double.parseDouble(expenseAmountInputEditText.getText().toString());
            String expenseDateInputAsString = expenseDateInputTextView.getText().toString();

            Expense newExpense = new Expense(expenseNameInputAsString, expenseAmountInputAsDouble, expenseDateInputAsString);

            // Create a new Intent as a container for the new Expense.
            Intent intent = new Intent(this, MainActivity.class);

            // Add newExpense to intent.
            intent.putExtra("newExpense", newExpense);

            // Set the resultCode to RESULT_OK to indicate a success and attach the intent.
            setResult(MainActivity.RESULT_OK, intent);

            // Return to MainActivity.
            finish();
        }

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

    private boolean expenseInputIsValid() {

        // Check if expenseNameInputEditText is valid.
        String expenseNameInputAsString = expenseNameInputEditText.getText().toString();
        if ( expenseNameInputAsString.isEmpty() ) {
            Toast.makeText(this, "Please enter an expense name", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if expenseAmountInputEditText is valid.
        String expenseAmountInputAsString = expenseAmountInputEditText.getText().toString();
        if ( expenseAmountInputAsString.isEmpty() ) {
            Toast.makeText(this, "Please enter an expense amount", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if expenseDateInputTextView is valid.
        String expenseDateInputAsString = expenseDateInputTextView.getText().toString();
        if ( expenseDateInputAsString.isEmpty() ) {
            Toast.makeText(this, "Please select an expense date", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If all are valid...
        return true;
    }

}