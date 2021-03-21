package com.example.monthlyexpensestracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    //This LinkedList<String> will hold ONLY expenseNameTextView names for now.
    //This is just to demonstrate how to use RecyclerView.
    //In the final version, this LinkedList should hold all the expenses the user has created.
    private final LinkedList<Expense> expenses = new LinkedList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add to expenses.
        addDummyExpenses();

        /*
        for (int i = 1; i <= 15; i++) {
            expenses.add("Expense " + i);
        } */

        // Get a handle to the RecyclerView.
        recyclerView = findViewById(R.id.recyclerView);
        // Create an adapter and supply the data to be displayed.
        adapter = new RecyclerViewAdapter(this, expenses);
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(adapter);
        // Give the RecyclerView a default layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Starts AddExpenseActivity
    public void testFabClick(View view) {
        //Create a new intent
        Intent intent = new Intent(this, AddExpenseActivity.class);
        startActivity(intent);
    }

    // Create and adds dummy expenses to the LinkedList expenses.
    public void addDummyExpenses() {
        // Dummy expenseAmount that will be modified for each Expense.
        double expenseAmount = 5;
        for (int i = 0; i < 15; i++) {
            // Create a new dummy Expense.
            Expense newExpense = new Expense("Expense " + i, expenseAmount);
            // Get the name of the newly created Expense.
            String expenseName = newExpense.getExpenseName();
            // Add the newly created dummy Expense to the LinkedList expenses.
            expenses.add(newExpense);
            // Modify double expenseAmount.
            expenseAmount += 5;
        }
    }

}
