package com.example.Main.ExpenseUtility;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import RoomDatabase.Expense;
import RoomDatabase.ExpenseViewModel;

/**
 * A class that updates outdated Expenses saved in the Room database.
 */
public class ExpenseDateUpdater {

    private final String LOG_TAG = "ExpenseDateUpdater"; // Simple log tag.

    /**
     * The following are parameters passed to the public constructor.
     */
    private List<Expense> expenseList; // The list of Expenses saved in the Room database.

    private final ExpenseViewModel expenseViewModel; // The ViewModel for the Room database.

    /**
     * This DateFormat object is used in the Logcat and is passed to mExpenseViewModel.
     */
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);

    /**
     * The following are all Calendar-related objects.
     * They are used in the Logcat and in the method expenseIsOutOfDate().
     */
    // A Calendar object with the current time.
    private final Calendar currentCalendar = Calendar.getInstance();

    // currentCalendar fields DATE, MONTH, and YEAR.
    private final int currentDateField = this.currentCalendar.get(Calendar.DATE);
    private final int currentMonthField = this.currentCalendar.get(Calendar.MONTH);
    private final int currentYearField = this.currentCalendar.get(Calendar.YEAR);

    /**
     * The ExpenseDateCaseDecider used in the method updateExpense()
     */
    private final ExpenseDateCaseDecider caseDecider;

    /**
     * The public constructor
     */
    public ExpenseDateUpdater(ExpenseViewModel expenseViewModel) {
        this.expenseViewModel = expenseViewModel;
        this.caseDecider = new ExpenseDateCaseDecider(this.currentCalendar);
    }

    /**
     * This method starts the process of checking for outdated Expenses and updating them if any
     * are out-of-date.
     *
     * @param expenseList The current list of Expenses saved in the Room database.
     */
    public void start(List<Expense> expenseList) {

        // Save the current list of Expenses stored in the Room database.
        this.expenseList = expenseList;

        // Get the indices of out of date expenses, if any.
        List<Integer> indices = expensesOutOfDateAtIndices();

        // If any Expense is out date.
        if (indices.size() != 0) {

            // Update the expenses.
            updateExpensesAt(indices);

        }
    }

    /**
     * This method goes through each Expense stored in expenseList and checks if any are
     * out of date.
     * If any Expenses are out of date, their indices are returned as a List of Integers.
     *
     * @return indices The indices of out of date Expenses that are stored in expenseList.
     */
    private List<Integer> expensesOutOfDateAtIndices() {

        // Create a new ArrayList to store the indices of out of date Expenses.
        List<Integer> indices = new ArrayList<>();

        // Go through each Expense and check if it's out of date
        for (int i = 0; i < this.expenseList.size(); i++) {

            // Get the current Expense in the this.mExpenseList
            Expense currentExpense = this.expenseList.get(i);

            // If currentExpense is out of date.
            if (expenseIsOutOfDate(currentExpense)) {

                // Add the index of currentExpense in this.mExpenseList to indices.
                indices.add(i);
            }
            // Else do nothing
        }
        // Return the list.
        return indices;
    }

    /**
     * Checks if the passed Expense is out of date.
     *
     * @param expense The Expense that will be checked
     * @return boolean Whether the Expense is out of date or not.
     */
    public boolean expenseIsOutOfDate(Expense expense) {

        // Get the Calendar object of the current expense.
        Calendar expenseCalendar = expense.getCalendar();

        // Get the Calendar fields DATE, MONTH, and YEAR for the Expense.
        int expenseDateField = expenseCalendar.get(Calendar.DATE);
        int expenseMonthField = expenseCalendar.get(Calendar.MONTH);
        int expenseYearField = expenseCalendar.get(Calendar.YEAR);

        // If the Expense year is after today's year, the Expense is not out of date.
        if (expenseYearField > this.currentYearField) {
            return false;
        }

        // If the Expense year is before today's year the Expense is out of date.
        if (expenseYearField < this.currentYearField) {
            return true;
        }

        // If the Expense month is before today's month AND if the Expense year is less than or
        // equal to today's year, the Expense is out of date.
        if (expenseMonthField < this.currentMonthField &&
                expenseYearField <= currentYearField) {
            return true;
        }

        // If the Expense day is before today AND the Expense month is less than or equal to
        // today's month, the Expense is out of date.
        if (expenseDateField < this.currentDateField) {
            return expenseMonthField <= this.currentMonthField;
        }
        // Else..
        return false;
    }

    /**
     * Updates the Expenses in expenseList at the indices stored in the passed list.
     *
     * @param indices The indices of out of date Expenses stored in expenseList
     */
    private void updateExpensesAt(List<Integer> indices) {

        // Go through each index in indices.
        for (int position : indices) {

            // Get the Expense that is out of date.
            Expense expense = expenseList.get(position);

            // Update the Expense.
            updateExpense(expense);
        }
    }

    /**
     * Updates the passed Expense.
     * <p>
     * Every Expense that is outdated has an expenseCase. The expenseCase will determine how the
     * expense will be updated.
     *
     * @param expense The Expense that will be updated.
     */
    private void updateExpense(Expense expense) {

        // Get the Calendar of the Expense.
        Calendar expenseCalendar = expense.getCalendar();

        // Get the Calendar fields DATE, MONTH, and EXPENSE.
        int expenseCalendarDateField = expense.getCalendarDateField();

        // Determine the case for the Expense.
        int expenseCase = this.caseDecider.decideCase(expense);

        // Execute the case and update the Expense.
        switch (expenseCase) {

            /* Increments the month by one and does nothing else. */
            case 1:

                // Increment the month by one.
                expenseCalendar.add(Calendar.MONTH, 1);

                // Update the Expense in Room with the new expense date.
                this.expenseViewModel.setExpenseDate(expense, expenseCalendar);

                break;

            /**
             * This is the case for December.
             *
             * Since December and January have 31 days, there are no
             * possible conflicts for Calendar.DATE.
             */
            case 2:

                // Set the month of expenseCalendar to January.
                expenseCalendar.set(Calendar.MONTH, Calendar.JANUARY);

                // Increment the year by one.
                expenseCalendar.add(Calendar.YEAR, 1);

                // Update the Expense in Room with the new expense date.
                this.expenseViewModel.setExpenseDate(expense, expenseCalendar);

                break;

            /**
             * Sets Calendar.DATE to 28, sets Calendar.MONTH to Calendar.FEBRUARY, and saves the
             * current Calendar.DATE field of the Expense.
             *
             * Case 3 is used when the Expense month is January and the current Calendar.DATE is
             * out of bounds for February.
             *
             * Since the current Calendar.DATE field is out of bounds for February, Jan. 29 through
             * 31, then it must be saved so that it can be restored when updating the Expense to
             * March.
             */
            case 3:

                // Save the current Calendar.DATE field of the Expense, so that it can be restored
                // for March.
                this.expenseViewModel
                        .setPreviousCalendarDateField(expense, expenseCalendarDateField);

                // Update the boolean hasPreviousCalendarDateField of the Expense.
                this.expenseViewModel.setHasPreviousCalendarDateField(expense, true);

                // Set the date of expenseCalendar to the 28th.
                expenseCalendar.set(Calendar.DATE, 28);

                // Set month of expenseCalendar to February.
                expenseCalendar.set(Calendar.MONTH, Calendar.FEBRUARY);

                // Update the Expense in Room with the new expense date.
                this.expenseViewModel.setExpenseDate(expense, expenseCalendar);

                break;

            /**
             * Reverts the Calendar.DATE field to the value of the previous Calendar.DATE field in
             * January and sets the Calendar.MONTH field to Calendar.MARCH.
             *
             * This case is used when updating the Expense month to March and IF the Expense has a
             * previous Calendar.DATE field saved.
             *
             */
            case 4:

                // Set the the date of expenseCalendar to the previous Calendar.DATE field in
                // January.
                expenseCalendar.set(Calendar.DATE, expense.previousCalendarDateField);

                // Reset the value of the previousCalendarDateField of the Expense, to show that
                // the Calendar.DATE field of expenseCalendar has been reverted and the value is no
                // longer needed.
                this.expenseViewModel.setPreviousCalendarDateField(expense, 0);

                // Update the boolean hasPreviousCalendarDateField to false.
                this.expenseViewModel.setHasPreviousCalendarDateField(expense, false);

                // Set the month of expenseCalendar to March.
                expenseCalendar.set(Calendar.MONTH, Calendar.MARCH);

                // Update the Expense in Room with the new expense date.
                this.expenseViewModel.setExpenseDate(expense, expenseCalendar);

                break;

            // This case is used when the current Expense day is the 31st and next month doesn't
            // have 31 days.
            /**
             * This case is used when the current Calendar.DATE field of the Expense is 31 and the
             * next month doesn't have 31 days.
             */
            case 5:

                // Set the value of wasThirtyFirst to true.
                this.expenseViewModel.setWasThirtyFirst(expense, true);

                // Set the date of expenseCalendar to the 30th.
                expenseCalendar.set(Calendar.DATE, 30);

                // Increment the month of expenseCalendar by one.
                expenseCalendar.add(Calendar.MONTH, 1);

                // Update the Expense in Room with the new expense date.
                this.expenseViewModel.setExpenseDate(expense, expenseCalendar);

                break;

            // This case if used when next month has 31 days and the Expense day was previously
            // set to the 31st but was changed to the 30th.
            /**
             * This case is used when next month has 31 days and if the value of wasThirtyFirst for
             * the Expense is true.
             */
            case 6:

                // Increment the month of expenseCalendar by one.
                expenseCalendar.add(Calendar.MONTH, 1);

                // Set the date of expenseCalendar to the 31st.
                expenseCalendar.set(Calendar.DATE, 31);

                // Set the value of wasThirtyFirst of the Expense to false.
                this.expenseViewModel.setWasThirtyFirst(expense, false);

                // Update the Expense in Room with the new expense date.
                this.expenseViewModel.setExpenseDate(expense, expenseCalendar);

                break;

            default:
                Log.i(LOG_TAG, "Reached default case.");
                break;
            // Do nothing.
        }
    }
}
