package com.example.Main;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import RoomDatabase.Expense;
import RoomDatabase.ExpenseViewModel;

public class ExpenseDateUpdater {
    private final String LOG_TAG = "ExpenseDateUpdater";

    // Passed parameters.
    private List<Expense> mExpenseList;
    private final ExpenseViewModel mExpenseViewModel;

    // Instance variables related to date.
    private final DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.LONG);

    private final Calendar currentCalendar = Calendar.getInstance();

    private final int currentDayInteger = this.currentCalendar.get(Calendar.DATE);
    private final int currentMonthInteger = this.currentCalendar.get(Calendar.MONTH);
    private final int currentYearInteger = this.currentCalendar.get(Calendar.YEAR);

    // Public constructor.
    public ExpenseDateUpdater(ExpenseViewModel mExpenseViewModel) {
        this.mExpenseViewModel = mExpenseViewModel;
    }

    public void start(List<Expense> expenseList) {
        Log.i(LOG_TAG, "currentDayInteger: " + this.currentDayInteger);
        Log.i(LOG_TAG, "currentMonthInteger: " + this.currentMonthInteger);
        Log.i(LOG_TAG, "currentYearInteger: " + this.currentYearInteger);
        Log.i(LOG_TAG, "isLeapYear: " + String.valueOf(isLeapYear()));

        this.mExpenseList = expenseList;
        Log.i(LOG_TAG,"Expenses to be checked: " + this.mExpenseList.toString());

        // Get the positions of out of date expenses, if any.
        List<Integer> positions = expensesOutOfDateAtPositions();

        // If any expense is out date.
        if (positions.size() != 0) {

            // Update the expenses.
            updateExpensesAt(positions);

        } else {
            // Do nothing, no expense is out of date.
        }
    }

    private List<Integer> expensesOutOfDateAtPositions() {

        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < this.mExpenseList.size(); i++) {

            Expense currentExpense = this.mExpenseList.get(i);

            if ( expenseIsOutOfDate(currentExpense) ) {

                Log.i(LOG_TAG, currentExpense.toString() + " is out of date.");
                Log.i(LOG_TAG, currentExpense.toString() + ": "
                        + currentExpense.getExpenseDate());

                positions.add(i);
            } else {
                Log.i(LOG_TAG, currentExpense.toString() + " is NOT out of date.");
                Log.i(LOG_TAG, currentExpense.toString() + ": "
                        + currentExpense.getExpenseDate());
            }
        }
        Log.i(LOG_TAG, "# of Expenses checked: " + this.mExpenseList.size() );

        return positions;
    }

    public boolean expenseIsOutOfDate(Expense expense) {

        // Get the Calendar object of the current expense.
        Calendar expenseDateCalendar = expense.getExpenseDateCalendar();

        // Get the expense date as integers.
        int expenseDayInteger = expenseDateCalendar.get(Calendar.DATE);
        int expenseMonthInteger = expenseDateCalendar.get(Calendar.MONTH);
        int expenseYearInteger = expenseDateCalendar.get(Calendar.YEAR);

        // If the expense year is after today's year.
        if (expenseYearInteger > this.currentYearInteger) {
            return false;
        }

        // If expense year is before today's year.
        if (expenseYearInteger < this.currentYearInteger) {
            return true;
        }

        // If expense month is before today's month.
        if (expenseMonthInteger < this.currentMonthInteger &&
                expenseYearInteger <= currentYearInteger) {

            return true;
        }

        // If expense day is before today.
        if (expenseDayInteger < this.currentDayInteger) {
            if (expenseMonthInteger <= this.currentMonthInteger) {
                return true;
            }
        }
        return false;
    }

    private void updateExpensesAt(List<Integer> positions) {

        for (int position: positions) {

            // Get the Expense that is out of date.
            Expense expense = mExpenseList.get(position);

            // Update the Expense.
            updateExpense(expense);
        }
    }
    // TODO: Continue with this method.
    private void updateExpense(Expense expense) {
        // Get the Calendar and fields of the expense.
        Calendar expenseDateCalendar = expense.getExpenseDateCalendar();
        int expenseDayInt = expense.getDayInt();
        int expenseMonthInt = expense.getMonthInt();
        int expenseYearInt = expense.getYearInt();

        int expenseCase = decideCase(expenseDayInt, expenseMonthInt, expenseYearInt);
        Log.i(LOG_TAG, expense.toString() + " is Case " + expenseCase);

        switch (expenseCase) {

            case 1:
                Log.i(LOG_TAG, "Reached Case 1");

                // Set the updated date.
                expenseDateCalendar.set(Calendar.MONTH, expenseMonthInt + 1);
                //expenseDateCalendar.set(Calendar.YEAR, this.currentYearInteger);

                Log.i(LOG_TAG, expense.toString() + " will be set to " + this.mDateFormat.format(expenseDateCalendar.getTime()));

                // Update the Expense in Room with the new expense date.
                mExpenseViewModel.setExpenseDate(expense.getExpenseName(), this.mDateFormat.format(expenseDateCalendar.getTime()));
                break;

                case 2:
                Log.i(LOG_TAG, "Reached Case 2");

                // Set the updated date.
                expenseDateCalendar.set(Calendar.MONTH, expenseMonthInt + 1);
                //expenseDateCalendar.set(Calendar.YEAR, this.currentYearInteger);

                Log.i(LOG_TAG, expense.toString() + " will be set to " + this.mDateFormat.format(expenseDateCalendar.getTime()));

                // Update the Expense in Room with the new expense date.
                mExpenseViewModel.setExpenseDate(expense.getExpenseName(), this.mDateFormat.format(expenseDateCalendar.getTime()));
                break;

            case 3:
                Log.i(LOG_TAG, "Reached Case 3");
                // Set the updated date.
                expenseDateCalendar.set(Calendar.MONTH, Calendar.JANUARY);
                expenseDateCalendar.set(Calendar.YEAR, expenseYearInt + 1);

                Log.i(LOG_TAG, expense.toString() + " will be set to " + this.mDateFormat.format(expenseDateCalendar.getTime()));

                // Update the Expense in Room with the new expense date.
                mExpenseViewModel.setExpenseDate(expense.getExpenseName(), this.mDateFormat.format(expenseDateCalendar.getTime()));
                break;

            default:
                Log.i(LOG_TAG, "Reached default Case");
                break;
                // Do nothing.
        }

    }

    private int decideCase(int expenseDayInt, int expenseMonthInt, int expenseYearInt) {
        String date = expenseDayInt + "-" + expenseMonthInt + "-" + expenseYearInt;
        //Log.i(LOG_TAG, "Deciding case for date: " + date);
        // First the easy cases.

        // If Case 1
        if ((expenseMonthInt >= Calendar.JANUARY) && (expenseMonthInt <= Calendar.NOVEMBER)) {

            if ((expenseDayInt >= 1) && (expenseDayInt <= 28)) {
                return 1;
            }
        }

        // If Case 2
        if (isLeapYear()) {
            if ((expenseMonthInt >= Calendar.JANUARY) && (expenseMonthInt <= Calendar.NOVEMBER)) {

                if ((expenseDayInt >= 1) && (expenseDayInt <= 29)) {
                    return 2;
                }
            }
        }

        // If Case 3
        if (expenseMonthInt == Calendar.DECEMBER) {
            return 3;
        }

        return -1;
    }

    private boolean isLeapYear() {

        // The Gregorian calendar stipulates that a years evenly divisible by 100
        // (for example, 1900) is a leap year only if it is ALSO evenly divisible by 400.
        if (this.currentYearInteger % 100 == 0) {
            if (this.currentYearInteger % 400 == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (this.currentYearInteger % 4 == 0) {
                return true;
            }
        }
        return false;
    }

}
