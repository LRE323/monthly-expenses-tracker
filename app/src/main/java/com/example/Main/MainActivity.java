package com.example.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import RoomDatabase.Expense;
import RoomDatabase.ExpenseViewModel;

import com.example.monthlyexpensestracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpenseViewHolder.OnExpenseListener {

    public final int ADD_EXPENSE_ACTIVITY_REQUEST_CODE = 100;

    // Room database variables.
    private ExpenseViewModel mExpenseViewModel;

    // UI Components.
    private TextView tvSumOfExpenses;
    private ExpenseListAdapter mExpenseListAdapter;

    // Misc. instance variables.
    private double mSumOfExpenseList;
    private ExpenseDateUpdater mExpenseDateUpdater;

    // Logcat Tags
    private final String LOG_TAG = "MainActivity";
    private final String MAIN_ACTIVITY_LIFECYCLE = "MainActivityLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MAIN_ACTIVITY_LIFECYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_all_XML_objects();

        createAllOnClickListeners();

        connectToRoomDatabase();

        mExpenseDateUpdater = new ExpenseDateUpdater(mExpenseViewModel);

        createDummyExpenses();

        observeExpenses();

        // Delete all previously made expenses. For development purposes.
        //mExpenseViewModel.deleteAll();

    }

    private void observeExpenses() {

        // Add an observer for the LiveData. The onChanged() method fires when the observed data
        // changes and the activity is in the foreground.

        // Get the LiveData from Room.
        LiveData<List<Expense>> expenseListLiveData = mExpenseViewModel.getExpensesAsLiveData();

        // Observe the LiveData in MainActivity.
        Log.i(LOG_TAG, "Start of .observe() lambda block.");
        expenseListLiveData.observe(this, expenseList -> {
            Log.i(LOG_TAG, "Entering .observe() lambda block...");

            // Sort expense list.
            sortExpensesByDate(expenseList);

            // Update the expenseDates of the Expense list if needed.
            //mExpenseDateUpdater.start(mExpenseList);

            // Calculate the sum of the expense list.
            mSumOfExpenseList = getSumOfExpenseList(expenseList);

            // Set the text for the TextView objects in headersHolder.
            setHeadersHolderText();

            // Update the cached copy of the words in the adapter.
            mExpenseListAdapter.submitList(expenseList);

            Log.i(LOG_TAG, "Exiting .observe() lambda block...");
        });
        Log.i(LOG_TAG, "End of .observe() lambda block");
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
        Log.i(LOG_TAG, "onActivityResult");

        // If the return was successful.

        if (resultCode == MainActivity.RESULT_OK) {

            // If we are returning from AddExpenseActivity
            if (requestCode == ADD_EXPENSE_ACTIVITY_REQUEST_CODE) {

                // If the intent has an extra.
                if (intent.hasExtra("newExpense")) {

                    // Get the new Expense created by the user.
                    Expense newExpense = intent.getParcelableExtra("newExpense");
                    Log.i(LOG_TAG, "Intent has Expense extra");
                    Log.i(LOG_TAG, "New expense created: " + newExpense.toString());

                    // Add newExpense to the database.
                    mExpenseViewModel.insert(newExpense);
                }
            }
        } else {
            // Do nothing.
        }
    }

    private void connectToRoomDatabase() {
        // Get a ViewModel from the ViewModelProvider
        mExpenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
    }

    private void createAllOnClickListeners() {
            /*
            tvPercentOfIncome.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(MainActivity.this, "Test long click", Toast.LENGTH_SHORT)
                        .show();
                        return true;
                    }
                });
            */
        // onClickListener for RecyclerView item

    }

    // Sets the text for all TextViews in the LinearLayout headersHolder.
    private void setHeadersHolderText() {
        setTextForTextViewSumOfExpenses(mSumOfExpenseList);
        //setHeaderPaycheckText();
        //setHeaderPercentText();
    }

    private void setTextForTextViewSumOfExpenses(double sum) {
        String sumOfExpensesFormatted = String.format("%.2f", sum);
        tvSumOfExpenses.setText("Sum of monthly expenses: $" + sumOfExpensesFormatted);

    }

        /*
        private void setHeaderPaycheckText() {
            // TODO: Set the text in tvUpcomingPayments with paycheck date info.
            tvUpcomingPayments.setText("You will pay $###.## between now and your next paycheck.");
        }

        private void setHeaderPercentText() {
            // TODO: Set the text in tvPercentOfIncome with paycheck amount info.
            //tvPercentOfIncome.setText("Your monthly expenses make up ##% of your monthly income.");
        }
         */

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MAIN_ACTIVITY_LIFECYCLE, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MAIN_ACTIVITY_LIFECYCLE, "onResume");

        // Start the expense date updater if possible.
        if ( this.mExpenseListAdapter.getCurrentList().size() != 0 ) {
            this.mExpenseDateUpdater.start(this.mExpenseListAdapter.getCurrentList());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MAIN_ACTIVITY_LIFECYCLE, "onPause");
    }

    private void sortExpensesByDate(List<Expense> expenseList) {

        // Create a new DateFormat object, to parse expenseDate for every Expense.
        DateFormat dateFormat = DateFormat.getDateInstance();

        ParsePosition pp = new ParsePosition(0);

        for (int i = 1; i < expenseList.size(); i++) {
            int k = i;

            try {

                while (k > 0) {
                    // Get Expenses.
                    Expense currentExpenseObject = expenseList.get(k);
                    Expense previousExpenseObject = expenseList.get(k - 1);

                    // Get expenseDates.
                    String currentExpenseDateString = currentExpenseObject.getExpenseDate();
                    String previousExpenseDateString = previousExpenseObject.getExpenseDate();

                    // Parse to Date objects.
                    Date currentDateObject = dateFormat.parse(currentExpenseDateString);
                    Date previousDateObject = dateFormat.parse(previousExpenseDateString);


                    if (currentDateObject.before(previousDateObject)) {

                        // Swap.
                        expenseList.set(k - 1, currentExpenseObject);
                        expenseList.set(k, previousExpenseObject);

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

    private void createDummyExpenses() {
        mExpenseViewModel.insert(new Expense("Expense 1", 0.99, "April 24, 2021"));
        mExpenseViewModel.insert(new Expense("Expense 2", 1.99, "May 1, 2021"));
        mExpenseViewModel.insert(new Expense("Expense 3", 2.99, "June 4, 2021"));
    }

    private void create_all_XML_objects() {
        tvSumOfExpenses = findViewById(R.id.tvSumOfExpenses);
        //tvUpcomingPayments = findViewById(R.id.tvUpcomingPayments);
        //tvPercentOfIncome = findViewById(R.id.tvPercentOfIncome);

        // Get a handle to the RecyclerView.
        createRecyclerViewAndRelated();

    }

    private void createRecyclerViewAndRelated() {
        // These two will be added in a later version.
        //private TextView tvUpcomingPayments;
        //private TextView tvPercentOfIncome;
        RecyclerView rvExpenseList = findViewById(R.id.recyclerView);

        // Create an adapter.
        mExpenseListAdapter = new ExpenseListAdapter(new ExpenseListAdapter.ExpenseDiff(), this);

        // Connect the adapter with the RecyclerView.
        rvExpenseList.setAdapter(mExpenseListAdapter);

        // Give the RecyclerView a default layout manager.
        rvExpenseList.setLayoutManager(new LinearLayoutManager(this));
    }

    private double getSumOfExpenseList(List<Expense> expenseList) {
        double sum = 0;
        for (Expense expense : expenseList) {
            double currentExpenseAmount = expense.getExpenseAmount();
            sum += currentExpenseAmount;
        }
        return sum;
    }

    @Override
    public void onExpenseClick(int position) {

        // Create a new AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Create the OnClickListener.
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Delete the row at position in expense_table
                mExpenseViewModel.deleteExpense(mExpenseListAdapter.getCurrentList().get(position));
            }
        };

        // Set the message for the AlertDialog.
        builder.setMessage("Are you sure you want to delete this expense?");

        // Setup the positive button.
        builder.setPositiveButton("Yes", onClickListener);

        // Setup the negative button, which will do nothing.
        builder.setNegativeButton("Cancel", null);

        // Create the AlertDialog.
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog.
        alertDialog.show();
    }
}
