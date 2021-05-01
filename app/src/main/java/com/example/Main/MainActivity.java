    package com.example.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Main.ExpenseRoom.Expense;
import com.example.Main.ExpenseRoom.ExpenseViewModel;
import com.example.monthlyexpensestracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

    public class MainActivity extends AppCompatActivity {
        // XML objects
        private TextView headerTotal;
        private RecyclerView recyclerView;
        private ExpenseListAdapter adapter;

        // These two will be added in a later version.
        //private TextView headerPaycheck;
        //private TextView headerPercent;

        public ExpenseViewModel mExpenseViewModel;
        private String LOG_TAG = "MainActivity";
        private String SORT_TAG = "Sort";
        private static final int ADD_EXPENSE_ACTIVITY_REQUEST_CODE = 100;

        // Define a request code as a member of the MainActivity
        public static final int NEW_EXPENSE_ACTIVITY_REQUEST_CODE = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.i(LOG_TAG, "onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            create_all_XML_objects();

            createAllOnClickListeners();

            connectToRoomDatabase();

            // Delete all previously made expenses. For development purposes.
            //mExpenseViewModel.deleteAll();

            createDummyExpense();

            observeData();
        }

        private void observeData() {
            // Add an observer for the LiveData. The onChanged() method fires when the observed data
            // changes and the activity is in the foreground.
            mExpenseViewModel.getExpensesAsLiveData().observe(this, expenses -> {

                // Sort expenses.
                sortExpensesByDate(expenses);

                // Update the cached copy of the words in the adapter.
                adapter.submitList(expenses);
            });

            // Set the text for the TextView objects in headersHolder.
            setHeadersHolderText();
        }

        private void connectToRoomDatabase() {
            // Get a ViewModel from the ViewModelProvider
            mExpenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        }

        private void createAllOnClickListeners() {
            /*
            headerPercent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(MainActivity.this, "Test long click", Toast.LENGTH_SHORT)
                        .show();
                        return true;
                    }
                });
            */
        }

        //Starts AddExpenseActivity
        public void openAddNewExpenseActivity(View view) {
            //Create a new intent
            Intent intent = new Intent(this, AddExpenseActivity.class);
            // Start the activity.
            startActivityForResult(intent, ADD_EXPENSE_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);
            Log.i(LOG_TAG, "onActivityResult");

            // If we are returning from AddExpenseActivity
            if (requestCode == ADD_EXPENSE_ACTIVITY_REQUEST_CODE) {

                // If the return was successful.
                if (resultCode == MainActivity.RESULT_OK); {

                    // If the intent has an extra.
                    if ( intent.hasExtra("newExpense") ) {

                        // Get the new Expense created by the user.
                        Expense newExpense = intent.getParcelableExtra("newExpense");
                        Log.i(LOG_TAG, "Intent has Expense extra");
                        Log.i(LOG_TAG, "New expense created: " + newExpense.toString());

                        // Add newExpense to the database.
                        mExpenseViewModel.insert(newExpense);
                    }
                }
            }
        }
        // Sets the text for all TextViews in the LinearLayout headersHolder.
        private void setHeadersHolderText() {
            setHeaderTotalText();
            //setHeaderPaycheckText();
            //setHeaderPercentText();
        }

        // TODO: Round off sumOfExpenses to 0.01
        private void setHeaderTotalText() {
            // Get a List<Expense> from the Room database.
            mExpenseViewModel.getExpensesAsLiveData().observe(this, expenses -> {
                double sumOfExpenses = 0;
                for (Expense e: expenses) {
                    double currentExpenseAmount = e.getExpenseAmount();
                    sumOfExpenses += currentExpenseAmount;
                }
                // Format sumOfExpenses to 0.01
                String sumOfExpensesFormatted = String.format("%.2f", sumOfExpenses);
                headerTotal.setText("Sum of monthly expenses: $" + sumOfExpensesFormatted);
            });
        }

        /*
        private void setHeaderPaycheckText() {
            // TODO: Set the text in headerPaycheck with paycheck date info.
            headerPaycheck.setText("You will pay $###.## between now and your next paycheck.");
        }

        private void setHeaderPercentText() {
            // TODO: Set the text in headerPercent with paycheck amount info.
            //headerPercent.setText("Your monthly expenses make up ##% of your monthly income.");
        }
         */

        @Override
        protected void onStart() {
            super.onStart();
            Log.i(LOG_TAG, "onStart");
        }

        @Override
        protected void onPause() {
            super.onPause();
            Log.i(LOG_TAG, "onPause");
        }


        private void sortExpensesByDate(List<Expense> expenses) {

            // Create a new DateFormat object, to parse expenseDate for every Expense.
            DateFormat dateFormat = DateFormat.getDateInstance();

            for (int i = 1; i < expenses.size(); i++) {
                int k = i;

                try {

                    while (k > 0) {
                        // Get Expenses.
                        Expense currentExpense = expenses.get(k);
                        Expense previousExpense = expenses.get(k - 1);

                        // Get expenseDates.
                        String currentExpenseDate = currentExpense.getExpenseDate();
                        String previousExpenseDate = previousExpense.getExpenseDate();

                        // Parse to Date objects.
                        Date currentDate = dateFormat.parse(currentExpenseDate);
                        Date previousDate = dateFormat.parse(previousExpenseDate);


                        if ( currentDate.before(previousDate) ) {

                            // Swap.
                            expenses.set(k - 1, currentExpense );
                            expenses.set(k, previousExpense);

                            // Go down the list.
                            k--;
                        } else {
                            break;
                        }
                    }
                } catch (ParseException e) {
                }
            }
        }

        private void createDummyExpense() {
            mExpenseViewModel.insert(new Expense("Expense 1", 0.99, "January 1, 1999"));
            mExpenseViewModel.insert(new Expense("Expense 2", 1.99, "January 1, 2020"));
            mExpenseViewModel.insert(new Expense("Expense 3", 2.99, "July 4, 1776"));
            mExpenseViewModel.insert(new Expense("Expense 4", 3.99, "April 14, 1284"));
        }

        private void create_all_XML_objects() {
            headerTotal = findViewById(R.id.headerTotal);
            //headerPaycheck = findViewById(R.id.headerPaycheck);
            //headerPercent = findViewById(R.id.headerPercent);

            // Get a handle to the RecyclerView.
            createRecyclerViewAndRelated();

        }

        private void createRecyclerViewAndRelated() {
            recyclerView = findViewById(R.id.recyclerView);

            // Create an adapter.
            adapter = new ExpenseListAdapter(new ExpenseListAdapter.ExpenseDiff());

            // Connect the adapter with the RecyclerView.
            recyclerView.setAdapter(adapter);

            // Give the RecyclerView a default layout manager.
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
