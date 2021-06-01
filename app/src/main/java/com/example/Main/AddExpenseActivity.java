package com.example.Main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.monthlyexpensestracker.R;

import java.text.DateFormat;
import java.util.Calendar;

import RoomDatabase.Expense;

/**
 * This class allows the user to enter information needed create an Expense, creates it, and then
 * passes it to MainActivity.
 */
public class AddExpenseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /**
     * Variables that hold the input information.
     */
    private EditText etExpenseNameInput;
    private EditText etExpenseAmountInput;
    private TextView tvExpenseDateInput;
    /**
     * Default dateFormat
     */
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Create all the input objects.
        this.etExpenseNameInput = findViewById(R.id.expenseNameInput);
        this.etExpenseAmountInput = findViewById(R.id.expenseAmountInput);
        this.tvExpenseDateInput = findViewById(R.id.expenseDateInput);
    }

    /**
     * Takes the input information and attempts to create an new Expense object that is then passed
     * to MainActivity.
     */
    public void addExpense(View view) {

        // If the input information is not valid
        if (!isExpenseInputValid()) {
            // Do nothing.
        } else {

            // Create the new Expense from the input information.
            String expenseNAme = etExpenseNameInput.getText().toString();
            double expenseAmount = Double.parseDouble(etExpenseAmountInput.getText().toString());
            String expenseDate = tvExpenseDateInput.getText().toString();

            Expense newExpense = new Expense(expenseNAme, expenseAmount, expenseDate);

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

    // TODO: User should not be able to create an already out of date expense.

    /**
     * Determines whether the the user input is valid for an Expense.
     *
     * @return boolean Whether the Expense is valid or not.
     */
    private boolean isExpenseInputValid() {

        // Get all the required input values.
        String expenseName = etExpenseNameInput.getText().toString();
        String expenseAmountString = etExpenseAmountInput.getText().toString();
        String expenseDate = tvExpenseDateInput.getText().toString();

        // If the expenseName field is empty, it is invalid.
        if (expenseName.isEmpty()) {
            Toast.makeText(this, "Please enter an expense name",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the expenseAmount field is empty, it is invalid.
        if (expenseAmountString.isEmpty()) {
            Toast.makeText(this, "Please enter an expense amount",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the expenseDate field is empty, it is invalid.
        if (expenseDate.isEmpty()) {
            Toast.makeText(this, "Please select an expense date",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // If all are valid...
        return true;
    }

    /**
     * Opens the DatePickerDialog used for expenseDate input
     */
    public void openDatePickerDialog(View view) {

        // Create a new DialogFragment object.
        DialogFragment dialogFragment = new DatePickerFragment();

        // Display the dialog, adding the fragment to the given FragmentManager.
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    /**
     * Called when the user clicks "OK" on the DatePickerDialog.
     *
     * Sets the chosen date as the text for tvExpenseDateInput
     */
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        // Use the parameters passed (year, month, & dayOfMonth) to create a new Calendar object.

        // Create a new Calendar object.
        Calendar calendar = Calendar.getInstance();

        // Set the time.
        calendar.set(year, month, dayOfMonth);     // Sets the Calendar time to the passed parameters.

        // Create a String variable that holds the date selected by the user.
        String dateSelected = this.dateFormat.format(calendar.getTime());

        // Set the text of tvExpenseDateInput to dateSelected.
        this.tvExpenseDateInput.setText(dateSelected);
    }


}