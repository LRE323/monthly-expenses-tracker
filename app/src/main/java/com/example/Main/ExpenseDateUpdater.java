package com.example.Main;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import RoomDatabase.Expense;
import RoomDatabase.ExpenseViewModel;

/** A class that updates outdated Expenses saved in the Room database. */
public class ExpenseDateUpdater {

    private final String LOG_TAG = "ExpenseDateUpdater"; // Simple log tag.

    /** The following are parameters passed to the public constructor.*/
    private List<Expense> mExpenseList; // The list of Expenses saved in the Room database.

    private final ExpenseViewModel mExpenseViewModel; // The ViewModel for the Room database.

    /** This DateFormat object is used in the Logcat and is passed to mExpenseViewModel.*/
    private final DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.LONG);

    /** The following are all Calendar-related objects.
     * They are used in the Logcat and in the method expenseIsOutOfDate(). */
    // A Calendar object with the current time.
    private final Calendar mCurrentCalendar = Calendar.getInstance();

    // The following are all integer values of mCurrentCalendar day, month, and year.
    private final int mCurrentDayInteger = this.mCurrentCalendar.get(Calendar.DATE);
    private final int mCurrentMonthInteger = this.mCurrentCalendar.get(Calendar.MONTH);
    private final int mCurrentYearInteger = this.mCurrentCalendar.get(Calendar.YEAR);

    /** The ExpenseDateCaseDecider used in the method updateExpense() */
    private final ExpenseDateCaseDecider mCaseDecider;

    /** The public constructor*/
    public ExpenseDateUpdater(ExpenseViewModel mExpenseViewModel) {
        this.mExpenseViewModel = mExpenseViewModel;
        this.mCaseDecider =
                new ExpenseDateCaseDecider(this.mCurrentCalendar);
    }

    /**
     *  This method starts the process of checking for outdated Expenses and updating them if any
     *  are out-of-date.
     *
     * @param expenseList The current list of Expenses saved in the Room database.
     */
    public void start(List<Expense> expenseList) {

        // Save the current list of Expenses stored in the Room database.
        this.mExpenseList = expenseList;

        // Get the indices of out of date expenses, if any.
        List<Integer> indices = expensesOutOfDateAtPositions();

        // If any Expense is out date.
        if (indices.size() != 0) {

            // Update the expenses.
            updateExpensesAt(indices);

        }
    }

    /**
     * This method goes through each Expense stored in this.mExpenseList and checks if any are
     * out of date.
     * If any Expenses are out of date, their indices are returned as a List of Integers.
     *
     * @return indices The indices of out of date Expenses that are stored in this.mExpenseList.
     */
    private List<Integer> expensesOutOfDateAtPositions() {

        // Create a new ArrayList to store the indices of out of date Expenses
        List<Integer> indices = new ArrayList<>();

        // Go through each Expense and check if it's out of date
        for (int i = 0; i < this.mExpenseList.size(); i++) {

            // Get the current Expense in the this.mExpenseList
            Expense currentExpense = this.mExpenseList.get(i);

            // If currentExpense is out of date.
            if (expenseIsOutOfDate(currentExpense)) {

                // Add the index of currentExpense in this.mExpenseList to indices.
                indices.add(i);
            }
        }
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
        Calendar expenseDateCalendar = expense.getExpenseDateCalendar();

        // Get the Expense day, month, and year as integers.
        int expenseDayInteger = expenseDateCalendar.get(Calendar.DATE);
        int expenseMonthInteger = expenseDateCalendar.get(Calendar.MONTH);
        int expenseYearInteger = expenseDateCalendar.get(Calendar.YEAR);

        // If the Expense year is after today's year, the Expense is not out of date.
        if (expenseYearInteger > this.mCurrentYearInteger) {
            return false;
        }

        // If the Expense year is before today's year the Expense is out of date.
        if (expenseYearInteger < this.mCurrentYearInteger) {
            return true;
        }

        // If the Expense month is before today's month AND if the Expense year is less than or
        // equal to today's year, the Expense is out of date.
        if (expenseMonthInteger < this.mCurrentMonthInteger &&
                expenseYearInteger <= mCurrentYearInteger) {
            return true;
        }

        // If the Expense day is before today AND the Expense month is less than or equal to
        // today's month, the Expense is out of date.
        if (expenseDayInteger < this.mCurrentDayInteger) {
            if (expenseMonthInteger <= this.mCurrentMonthInteger) {
                return true;
            }
        }
        // Else..
        return false;
    }

    /**
     * Updates the Expenses stored in this.mExpenseList at the indices stored in the passed list.
     *
     * @param indices The indices of out of date Expenses stored in this.mExpenseList
     */
    private void updateExpensesAt(List<Integer> indices) {

        // Go through each index in indices.
        for (int position : indices) {

            // Get the Expense that is out of date.
            Expense expense = mExpenseList.get(position);

            // Update the Expense.
            updateExpense(expense);
        }
    }

    // TODO: Finish cleaning this class.
    /**
     *
     * @param expense The Expense that will be updated.
     */
    private void updateExpense(Expense expense) {
        // Get the Calendar and fields of the expense.
        Calendar expenseDateCalendar = expense.getExpenseDateCalendar();
        int expenseDayInt = expense.getDayInt();
        int expenseMonthInt = expense.getMonthInt();
        int expenseYearInt = expense.getYearInt();

        int expenseCase = this.mCaseDecider.decideCase(expense);

        switch (expenseCase) {

            // This case only increments the month by one and does nothing else.
            case 1:

                // Increment the month by one.
                expenseDateCalendar.set(Calendar.MONTH, expenseMonthInt + 1);

                // Update the Expense in Room with the new expense date.
                Log.i(LOG_TAG, expense.toString() + " will be set to " +
                        this.mDateFormat.format(expenseDateCalendar.getTime()));

                this.mExpenseViewModel.setExpenseDate(expense, expenseDateCalendar,
                        this.mDateFormat);

                break;

            // This is the case for December. This case sets the month to January and increments
            // the year.
            case 2:

                // Set the month to January.
                expenseDateCalendar.set(Calendar.MONTH, Calendar.JANUARY);

                // Increment the year.
                expenseDateCalendar.set(Calendar.YEAR, expenseYearInt + 1);

                // Update the Expense in Room with the new expense date.
                Log.i(LOG_TAG, expense.toString() + " will be set to " +
                        this.mDateFormat.format(expenseDateCalendar.getTime()));

                this.mExpenseViewModel.setExpenseDate(expense, expenseDateCalendar,
                        this.mDateFormat);

                break;

            // This is the case if the Expense month is January and the Expense day is out of bounds
            // for February.
            case 3:

                // Set the day to the 28th.
                expenseDateCalendar.set(Calendar.DATE, 28);

                // Set previousJanuaryDay to expenseDayInt.
                this.mExpenseViewModel.setPreviousJanuaryDay(expense, expenseDayInt);

                // Update the boolean hasPreviousJanuary day for the Expense.
                this.mExpenseViewModel.setHasPreviousJanuaryDay(expense, true);

                // Set the month to February.
                expenseDateCalendar.set(Calendar.MONTH, Calendar.FEBRUARY);

                // Update the Expense in Room with the new expense date.
                Log.i(LOG_TAG, expense.toString() + " will be set to " +
                        this.mDateFormat.format(expenseDateCalendar.getTime()));

                this.mExpenseViewModel.setExpenseDate(expense, expenseDateCalendar,
                        this.mDateFormat);

                break;

            // This case is used when updating the Expense month to March and if the previous
            // Expense day had a conflict with February.
            case 4:

                // Set the date to previousJanuaryDay
                expenseDateCalendar.set(Calendar.DATE, expense.previousJanuaryDay);

                // Set hasPreviousJanuaryDay to false.
                this.mExpenseViewModel.setHasPreviousJanuaryDay(expense, false);

                // Reset the value of previousJanuaryDay.
                this.mExpenseViewModel.setPreviousJanuaryDay(expense, 0);

                // Set the month to March
                expenseDateCalendar.set(Calendar.MONTH, Calendar.MARCH);

                // Update the Expense in Room with the new expense date.
                Log.i(LOG_TAG, expense.toString() + " will be set to " +
                        this.mDateFormat.format(expenseDateCalendar.getTime()));

                this.mExpenseViewModel.setExpenseDate(expense, expenseDateCalendar,
                        this.mDateFormat);

                break;

            // This case is used when the current Expense day is the 31st and next month doesn't
            // have 31 days.
            case 5:

                // Change the Expense day to the 30th.
                expenseDateCalendar.set(Calendar.DATE, 30);

                //Increment the Expense month by one.
                expenseDateCalendar.add(Calendar.MONTH, 1);

                // Set wasThirtyFirst to true.
                this.mExpenseViewModel.setWasThirtyFirst(expense, true);

                // Update the Expense in Room with the new expense date.
                Log.i(LOG_TAG, expense.toString() + " will be set to " +
                        this.mDateFormat.format(expenseDateCalendar.getTime()));

                this.mExpenseViewModel.setExpenseDate(expense, expenseDateCalendar,
                        this.mDateFormat);

                break;


            // This case if used when next month has 31 days and the Expense day was previously
            // set to the 31st but was changed to the 30th.
            case 6:

                // Increment the month by 1.
                expenseDateCalendar.add(Calendar.MONTH, 1);

                // Set the Expense day to the 31st.
                expenseDateCalendar.set(Calendar.DATE, 31);

                // Set wasThirtyFirst to false.
                this.mExpenseViewModel.setWasThirtyFirst(expense, false);

                // Update the Expense in Room with the new expense date.
                Log.i(LOG_TAG, expense.toString() + " will be set to " +
                        this.mDateFormat.format(expenseDateCalendar.getTime()));

                this.mExpenseViewModel.setExpenseDate(expense, expenseDateCalendar,
                        this.mDateFormat);

                break;

            default:
                Log.i(LOG_TAG, "Reached default case.");
                break;
            // Do nothing.
        }
    }
}
