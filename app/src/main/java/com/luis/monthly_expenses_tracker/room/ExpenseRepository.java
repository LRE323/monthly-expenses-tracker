package com.luis.monthly_expenses_tracker.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private LiveData< List<Expense> > expenseList;

    ExpenseRepository(Application application) {

        // Access the database, creating it if it's the first time it's being accessed.
        ExpenseRoomDatabase database = ExpenseRoomDatabase.getDatabase(application);

       expenseDao = database.expenseDao();

        // Get the LiveData
        expenseList = expenseDao.getLiveData();
    }

    /**
     * Returns the LiveData from Room.
     *
     * @return expenseList The LiveData returned
     */
    LiveData< List<Expense> > getLiveData() {
        return expenseList;
    }

    /**
     * We need to run the following methods on a background thread or the app will throw an
     * exception.
     */

    /**
     * Adds the Expense to the database.
     *
     * @param expense The Expense added to the database.
     */
    void insert(Expense expense) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.insert(expense);
        });
    }

    /**
     * Deletes all the Expenses in the database.
     */
    void deleteAllExpenses() {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.deleteAllExpenses();
        });
    }

    /**
     * Deletes an Expense with the specified expenseName.
     *
     * @param expense The Expense to be deleted.
     */
    void deleteAnExpense(Expense expense) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.deleteAnExpense(expense.expenseName);
        });
    }

    /**
     * Sets the field expenseDate for the expenseName passed.
     *
     * @param expenseName The name of the Expense for which expenseDate will be set.
     * @param expenseDate The date that expenseDate will be set to.
     */
    void setExpenseDate(String expenseName, String expenseDate) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.setExpenseDate(expenseName, expenseDate);
        });
    }

    /**
     * Sets calendarDateField as the the value for the previousCalendarDate variable of the passed
     * expense.
     *
     * @param expense The Expense that will have its previousCalendarDate field set
     * @param calendarDateField The value the field previousCalendarDate will be set to
     */
    void setPreviousCalendarDateField(Expense expense, int calendarDateField) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.setPreviousCalendarDateField(expense.expenseName, calendarDateField);
        });
    }

    /**
     * Sets the boolean hasPreviousCalendarDateField of the passed expense to the boolean passed.
     *
     * @param expense The Expense that will have its boolean hasPreviousCalendarDateField set.
     * @param bool The value that hasPreviousCalendarDateField will be set to.
     */
    void setHasPreviousCalendarDateField(Expense expense, boolean bool) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            this.expenseDao.setPreviousCalendarDateField(expense.expenseName, bool);
        });
    }

    /**
     * Sets the boolean wasThirtyFirst of the passed expense to the boolean passed.
     *
     * @param expense The Expense that will have its boolean wasThirtyFirst set.
     * @param bool The value that wasThirtyFirst will be set to.
     */
    public void setWasThirtyFirst(Expense expense, boolean bool) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            this.expenseDao.setWasThirtyFirst(expense.expenseName, bool);
        });
    }
}
