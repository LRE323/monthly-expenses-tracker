    package com.example.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.Main.Room.Expense;
import com.example.Main.Room.ExpenseDao;
import com.example.Main.Room.ExpenseViewModel;
import com.example.monthlyexpensestracker.R;

    public class MainActivity extends AppCompatActivity {
    private TextView headerTotal;
    private TextView headerPaycheck;
    private TextView headerPercent;
    public ExpenseViewModel mExpenseViewModel;
    private String LOG_TAG = "MainActivity";
    private static final int ADD_EXPENSE_ACTIVITY_REQUEST_CODE = 100;

    // Define a request code as a member of the MainActivity
    public static final int NEW_EXPENSE_ACTIVITY_REQUEST_CODE = 1;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle to all the TextView objects in LinearLayout headersHolder.
        headerTotal = findViewById(R.id.headerTotal);
        headerPaycheck = findViewById(R.id.headerPaycheck);
        headerPercent = findViewById(R.id.headerPercent);

        // Set the text for the TextView objects in headersHolder.
        setHeadersHolderText();

        // Get a handle to the RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // Create an adapter.
        final ExpenseListAdapter adapter = new ExpenseListAdapter(new ExpenseListAdapter.ExpenseDiff());
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(adapter);
        // Give the RecyclerView a default layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a ViewModel from the ViewModelProvider
        mExpenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);


        // Add an observer for the LiveData. The onChanged() method fires when the observed data
        // changes and the activity is in the foreground.
        mExpenseViewModel.getAllExpenses().observe(this, expenses -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(expenses);
        });

        // Delete all Expenses onCreate(). FOR TESTING PURPOSES ONLY.
        mExpenseViewModel.deleteAll();
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
        setHeaderPaycheckText();
        setHeaderPercentText();
    }

    private void setHeaderTotalText() {
        // Get the sum of all the monthly expenses.
        double sumOfExpenses = 0;
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

}
