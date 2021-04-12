package com.example.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.icu.util.Calendar;

import com.example.Main.Room.Expense;
import com.example.monthlyexpensestracker.R;

import java.text.DateFormat;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    // Stores the Expense objects created by the user.
    private final LinkedList<Expense> expenses = new LinkedList<>();
    private TextView headerTotal;
    private TextView headerPaycheck;
    private TextView headerPercent;

    // Development branch test

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle to all the TextView objects in LinearLayout headersHolder.
        headerTotal = findViewById(R.id.headerTotal);
        headerPaycheck = findViewById(R.id.headerPaycheck);
        headerPercent = findViewById(R.id.headerPercent);

        // Add dummy values to expenses.
        addDummyExpenses();

        // Set the text for the TextView objects in headersHolder.
        setHeadersHolderText();

        // Get a handle to the RecyclerView.
        recyclerView = findViewById(R.id.recyclerView);
        // Create an adapter and supply the data to be displayed.
        recyclerViewAdapter = new RecyclerViewAdapter(this, expenses);
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(recyclerViewAdapter);
        // Give the RecyclerView a default layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Starts AddExpenseActivity
    public void openAddNewExpenseActivity(View view) {
        //Create a new intent
        Intent intent = new Intent(this, AddExpenseActivity.class);
        // Start the activity.
        startActivity(intent);
    }

    // Create and adds dummy expenses to the LinkedList expenses.
    private void addDummyExpenses() {

        // Dummy expenseAmount that will be modified for each Expense.
        double expenseAmount = 5;

        // Create a dummy Calendar expenseDate. Used for every expense created.
        Calendar expenseDateCalendar = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String expenseDateString = dateFormat.format( expenseDateCalendar.getTime() );

        for (int i = 0; i < 4; i++) {

            // Create a new dummy Expense.
            Expense newExpense = new Expense("Expense " + i, expenseAmount, expenseDateString);

            // Get the name of the newly created Expense.
            String expenseName = newExpense.getExpenseName();

            // Add the newly created dummy Expense to the LinkedList expenses.
            expenses.add(newExpense);

            // Modify double expenseAmount.
            expenseAmount += 5;
        }
    }

    // Sets the text for all TextViews in the LinearLayout headersHolder.
    private void setHeadersHolderText() {
        setHeaderTotalText();
        setHeaderPaycheckText();
        setHeaderPercentText();
    }

    private void setHeaderTotalText() {
        // Get the sum of all the monthly expenses.
        double sumOfExpenses = 0;
        for (Expense expense: expenses) {
            // Get the current expense amount and add to sumOfExpenses.
            double currentExpense = expense.getExpenseAmount();
            sumOfExpenses += currentExpense;
        }
        // Set the text for TextView headerTotal.
        String text = "Monthly expenses: $" + sumOfExpenses;
        headerTotal.setText(text);
    }

    private void setHeaderPaycheckText() {
        // TODO: Set the text in headerPaycheck with paycheck date info.
        headerPaycheck.setText("You will pay $###.## between now and your next paycheck.");
    }

    private void setHeaderPercentText() {
        // TODO: Set the text in headerPercent with paycheck amount info.
        headerPercent.setText("Your monthly expenses make up ##% of your monthly income.");
    }

}
