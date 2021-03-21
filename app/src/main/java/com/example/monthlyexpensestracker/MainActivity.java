package com.example.monthlyexpensestracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    //This LinkedList<String> will hold ONLY expense names for now.
    //This is just to demonstrate how to use RecyclerView.
    //In the final version, this LinkedList should hold all the expenses the user has created.
    private final LinkedList<String> expenses = new LinkedList<>();

    private RecyclerView recyclerView;
    private ExpenseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add to expenses.
        for (int i = 1; i <= 15; i++) {
            expenses.add("Expense " + i);
        }
        // Get a handle to the RecyclerView.
        recyclerView = findViewById(R.id.recyclerView);
        // Create an adapter and supply the data to be displayed.
        adapter = new ExpenseListAdapter(this, expenses);
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

    }
