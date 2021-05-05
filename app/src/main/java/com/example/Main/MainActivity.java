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

import com.example.Main.ExpenseRoom.Expense;
import com.example.Main.ExpenseRoom.ExpenseViewModel;
import com.example.monthlyexpensestracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

    public class MainActivity extends AppCompatActivity {

        // UI Components.
        private TextView tvHeaderTotal;
        private RecyclerView rvExpenseList;
        private ExpenseListAdapter expenseListAdapter;
        // These two will be added in a later version.
        //private TextView tvHeaderPaycheck;
        //private TextView tvHeaderPercent;

        // Room database variables.
        public ExpenseViewModel mExpenseViewModel;
        private static final int ADD_EXPENSE_ACTIVITY_REQUEST_CODE = 100;

        // Misc. instance variables.
        private double sumOfExpenseList;

        // Logcat Tags
        private String TAG = "MainActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.i(TAG, "onCreate");
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

        private void updateExpenses() {
        }

        private boolean expensesAreUpToDate() {
            // Get the today's date.
            final Calendar today = Calendar.getInstance();
            Log.i(TAG, String.valueOf(today.get(Calendar.MONTH) + 1));
            Log.i(TAG, String.valueOf(today.get(Calendar.DATE)));
            Log.i(TAG, String.valueOf(today.get(Calendar.YEAR)));

            mExpenseViewModel.getExpensesAsLiveData().observe(this, expenses -> {
                for (Expense e: expenses) {
                }
            });
            return true;
        }

        // TODO: After initial release: Change this method, it is deprecated.
        //Starts AddExpenseActivity
        public void openAddNewExpenseActivity(View view) {
            //Create a new intent
            Intent intent = new Intent(this, AddExpenseActivity.class);

            /*
            Per Android documentation: While the underlying startActivityForResult() and
            onActivityResult() APIs are available on the Activity class on all API levels,
            it is strongly recommended to use the Activity Result APIs introduced in
            AndroidX Activity and Fragment.
            */
            // Start the activity.
            startActivityForResult(intent, ADD_EXPENSE_ACTIVITY_REQUEST_CODE);
        }

        // TODO: After initial release: Change this method, it is deprecated.
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);
            Log.i(TAG, "onActivityResult");

            // If the return was successful.

            if (resultCode == MainActivity.RESULT_OK) {

                // If we are returning from AddExpenseActivity
                if (requestCode == ADD_EXPENSE_ACTIVITY_REQUEST_CODE) {

                    // If the intent has an extra.
                    if ( intent.hasExtra("newExpense") ) {

                        // Get the new Expense created by the user.
                        Expense newExpense = intent.getParcelableExtra("newExpense");
                        Log.i(TAG, "Intent has Expense extra");
                        Log.i(TAG, "New expense created: " + newExpense.toString());

                        // Add newExpense to the database.
                        mExpenseViewModel.insert(newExpense);
                    }
                }
            } else  {
                // Do nothing.
            }
        }

        private void observeData() {
            // Add an observer for the LiveData. The onChanged() method fires when the observed data
            // changes and the activity is in the foreground.
            mExpenseViewModel.getExpensesAsLiveData().observe(this, expenseList -> {

                // Calculate the sum of the expense list.
                sumOfExpenseList = getSumOfExpenseList(expenseList);

                // Sort expenses.
                sortExpensesByDate(expenseList);

                // Set the text for the TextView objects in headersHolder.
                setHeadersHolderText();

                // Update the cached copy of the words in the adapter.
                expenseListAdapter.submitList(expenseList);

            });


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

        // Sets the text for all TextViews in the LinearLayout headersHolder.
        private void setHeadersHolderText() {
            setHeaderTotalText(sumOfExpenseList);
            //setHeaderPaycheckText();
            //setHeaderPercentText();
        }

        private void setHeaderTotalText(double sum) {
            // Get a List<Expense> from the Room database.
            /*mExpenseViewModel.getExpensesAsLiveData().observe(this, expenses -> {
                double sumOfExpenses = 0;
                for (Expense e: expenses) {
                    double currentExpenseAmount = e.getExpenseAmount();
                    sumOfExpenses += currentExpenseAmount;
                }
                // Format sumOfExpenses to 0.01
                String sumOfExpensesFormatted = String.format("%.2f", sumOfExpenses);
                tvHeaderTotal.setText("Sum of monthly expenses: $" + sumOfExpensesFormatted);
            });*/
            /*
            double sumOfExpenses = 0;
            for (Expense expense: expenseList) {
                double currentExpenseAmount = expense.getExpenseAmount();
                sumOfExpenses += currentExpenseAmount;
            }
            // Format sumOfExpenses to 0.01
            String sumOfExpensesFormatted = String.format("%.2f", sumOfExpenses);
            tvHeaderTotal.setText("Sum of monthly expenses: $" + sumOfExpensesFormatted);*/
            String sumOfExpensesFormatted = String.format("%.2f", sum);
            tvHeaderTotal.setText("Sum of monthly expenses: $" + sumOfExpensesFormatted);

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
            Log.i(TAG, "onStart");
        }

        @Override
        protected void onPause() {
            super.onPause();
            Log.i(TAG, "onPause");
        }

        private void sortExpensesByDate(List<Expense> listOfExpenseObjects) {

            // Create a new DateFormat object, to parse expenseDate for every Expense.
            DateFormat dateFormat = DateFormat.getDateInstance();

            ParsePosition pp = new ParsePosition(0);

            for (int i = 1; i < listOfExpenseObjects.size(); i++) {
                int k = i;

                try {

                    while (k > 0) {
                        // Get Expenses.
                        Expense currentExpenseObject = listOfExpenseObjects.get(k);
                        Expense previousExpenseObject = listOfExpenseObjects.get(k - 1);

                        // Get expenseDates.
                        String currentExpenseDateString = currentExpenseObject.getExpenseDate();
                        String previousExpenseDateString = previousExpenseObject.getExpenseDate();

                        // Parse to Date objects.
                        Date currentDateObject = dateFormat.parse(currentExpenseDateString);
                        Date previousDateObject = dateFormat.parse(previousExpenseDateString);


                        if ( currentDateObject.before(previousDateObject) ) {

                            // Swap.
                            listOfExpenseObjects.set(k - 1, currentExpenseObject );
                            listOfExpenseObjects.set(k, previousExpenseObject);

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
            tvHeaderTotal = findViewById(R.id.headerTotal);
            //headerPaycheck = findViewById(R.id.headerPaycheck);
            //headerPercent = findViewById(R.id.headerPercent);

            // Get a handle to the RecyclerView.
            createRecyclerViewAndRelated();

        }

        private void createRecyclerViewAndRelated() {
            rvExpenseList = findViewById(R.id.recyclerView);

            // Create an adapter.
            expenseListAdapter = new ExpenseListAdapter(new ExpenseListAdapter.ExpenseDiff());

            // Connect the adapter with the RecyclerView.
            rvExpenseList.setAdapter(expenseListAdapter);

            // Give the RecyclerView a default layout manager.
            rvExpenseList.setLayoutManager(new LinearLayoutManager(this));
        }

        private double getSumOfExpenseList(List<Expense> expenseList) {
            double sum = 0;
            for (Expense expense: expenseList) {
                double currentExpenseAmount = expense.getExpenseAmount();
                sum += currentExpenseAmount;
            }
            return sum;
        }
    }
