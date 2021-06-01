package com.example.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monthlyexpensestracker.R;

import java.util.List;

import RoomDatabase.Expense;
import RoomDatabase.ExpenseViewModel;

public class MainActivity extends AppCompatActivity implements ExpenseViewHolder.OnExpenseListener {

    /**
     * The activity request code for AddExpenseActivity.
     */
    public final int addExpenseActivityRequestCode = 1;

    /**
     * The ViewModel for the Room database.
     */
    private ExpenseViewModel expenseViewModel;

    // User interface objects
    /**
     * The TextView that displays the sum of all the expenses, sumOfExpenses.
     */
    private TextView tvSumOfExpenses;
    /**
     * The required ListAdapter for the RecyclerView used to display the list of expenses.
     */
    private ExpenseListAdapter expenseListAdapter;

    /**
     * The total sum of expenseAmount of the expenses.
     */
    private String sumOfExpenses;

    /**
     * Used to update the expenses.
     */
    private ExpenseDateUpdater updater;

    /**
     * Logcat tags
     */
    private final String LOG_TAG = "MainActivity";
    private final String MAIN_ACTIVITY_LIFECYCLE = "MainActivityLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MAIN_ACTIVITY_LIFECYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates all the necessary user interface objects.
        createUserInterfaceObjects();

        // Creates all the required listeners.
        // This method is currently disabled and will be implemented in later version.
        createAllOnClickListeners();

        // Connects to the Room database.
        connectToRoomDatabase();

        // Creates a new ExpenseDateUpdater
        this.updater = new ExpenseDateUpdater(expenseViewModel);

        // Observes the LiveData of the list of expenses saved in Room.
        observeExpenses();

    }

    /**
     * Creates all the necessary user interface (XML) objects.
     */
    private void createUserInterfaceObjects() {
        // The TextView that displays the sum of all the expenses, sumOfExpenses.
        tvSumOfExpenses = findViewById(R.id.tvSumOfExpenses);

        // Goes through all the necessary processes needed to have a functioning RecyclerView for
        // the list of expenses.
        createRecyclerViewAndRelated();

    }

    /**
     * Goes through all the necessary processes needed to have a functioning RecyclerView for the
     * list of expenses.
     */
    private void createRecyclerViewAndRelated() {
        // The RecyclerView used for the list of expenses
        RecyclerView rvExpenseList = findViewById(R.id.recyclerView);

        // The ListAdapter needed for rvExpenseList
        expenseListAdapter =
                new ExpenseListAdapter(new ExpenseListAdapter.ExpenseDiff(), this,
                        getApplicationContext());

        // Connect expenseListAdapter to rvExpenseList
        rvExpenseList.setAdapter(expenseListAdapter);

        // Give rvExpenseList a default layout manager.
        rvExpenseList.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Creates all the listeners needed for this activity.
     * <p>
     * This method is currently disabled and will be implemented in later version.
     */
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

    /**
     * Connects to the Room database by creating a ViewModel object.
     */
    private void connectToRoomDatabase() {
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
    }

    /**
     * Observes the LiveData of the list of expenses saved in Room.
     * <p>
     * Sorts the expenseList by date and calculates the sum of expenseList before submitting the
     * list to expenseListAdapter.
     */
    private void observeExpenses() {

        // Get the LiveData from Room.
        LiveData<List<Expense>> expenseListLiveData = this.expenseViewModel.getExpenseLiveData();

        // Observe the LiveData in MainActivity.
        expenseListLiveData.observe(this, expenseList -> {

            // Sort the expenses before submitting them to expenseListAdapter.
            ExpenseListSorter sorter = new ExpenseListSorter();
            sorter.sort(expenseList);
            //sortExpensesByDate(expenseList);

            // Calculate the sum of the expenseList.
            this.sumOfExpenses = getSumOfExpenses(expenseList);

            // Set the text for tvSumOfExpenses
            setTextForTextViewSumOfExpenses(this.sumOfExpenses);

            //Submit the expenseList to the expenseListAdapter
            expenseListAdapter.submitList(expenseList);

        });
    }

    /**
     * Returns the sum of expenseAmount for all the expenses in expenseList.
     *
     * @param expenseList The list of expenses that will be summed.
     * @return sum The sun of expenseAmount for all the expenses in expenseList.
     */
    private String getSumOfExpenses(List<Expense> expenseList) {
        double sum = 0;

        for (Expense expense : expenseList) {
            double currentExpenseAmount = expense.expenseAmount;
            sum += currentExpenseAmount;
        }
        // Format and return the sum.
        return String.format("%.2f", sum);
    }

    /**
     * Sets the text for the tvSumOfExpenses with the String provided.
     *
     * @param sum The sum of expenseList.
     */
    private void setTextForTextViewSumOfExpenses(String sum) {

        String text = getString(R.string.sum_of_expenses_text, sum);

        tvSumOfExpenses.setText(text);

    }

    // TODO: After initial release: Change this method, it is deprecated.

    /**
     * Starts AddExpenseActivity
     *
     * @param view
     */
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
        startActivityForResult(intent, addExpenseActivityRequestCode);
    }

    // TODO: After initial release: Change this method, it is deprecated.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // If the return was successful.
        if (resultCode == MainActivity.RESULT_OK) {

            // If we are returning from AddExpenseActivity
            if (requestCode == addExpenseActivityRequestCode) {

                // If the intent has an extra.
                if (intent.hasExtra("newExpense")) {

                    // Get the new Expense created by the user.
                    Expense newExpense = intent.getParcelableExtra("newExpense");

                    // Add newExpense to the database.
                    expenseViewModel.insert(newExpense);
                }
            }
        }
    }

    /**
     * The outdated expenses in expenseList are updated when onResume is called.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Start the expense date updater if possible.
        if (this.expenseListAdapter.getCurrentList().size() != 0) {
            this.updater.start(this.expenseListAdapter.getCurrentList());
        }
    }

    /**
     * Shows when the user long clicks an expense in the RecyclerView for expenseList.
     * <p>
     * Allows the user to delete an expense.
     *
     * @param position The index of the clicked expense in expenseList.
     */
    @Override
    public void onExpenseClick(int position) {

        // Create a new AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Create the OnClickListener.
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {

            // Delete the row at position in expense_table
            expenseViewModel.deleteExpense(expenseListAdapter.getCurrentList().get(position));
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
