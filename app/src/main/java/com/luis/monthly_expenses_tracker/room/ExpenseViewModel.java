package com.luis.monthly_expenses_tracker.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository expenseRepository;

    private final LiveData<List<Expense>> expenseLiveData;

    public ExpenseViewModel(Application application) {
        super(application);

        // Create a new repository
        this.expenseRepository = new ExpenseRepository(application);

        // Get the LiveData for the list of Expenses
        this.expenseLiveData = expenseRepository.getLiveData();
    }

    /**
     * Returns the current LiveData of the Expense list.
     *
     */
    public LiveData<List<Expense>> getExpenseLiveData() {
        return this.expenseLiveData;
    }

    /**
     * Saves the passed expense with Room.
     * @param expense The Expense to be saved.
     */
    public void insert(Expense expense) {
        this.expenseRepository.insert(expense);
    }

    /**
     * Deletes all the Expenses saved in Room.
     */
    public void deleteAll() {
        this.expenseRepository.deleteAllExpenses();
    }

    /**
     * Deletes the passed expense from Room.
     *
     * @param expense The Expense to be deleted.
     */
    public void deleteExpense(Expense expense) {
        this.expenseRepository.deleteAnExpense(expense);
    }

    /**
     * Sets the date of the passed Expense to the date specified in the Calendar object passed.
     *
     * @param expense The Expense that will have its expenseDate set
     * @param calendar The time that expenseDate will be set to.
     */
    public void setExpenseDate(Expense expense, Calendar calendar) {
        this.expenseRepository.setExpenseDate(expense, calendar);
    }

    /**
     * Sets calendarDateField as the the value for the previousCalendarDate variable of the passed
     * Expense.
     *
     * @param expense The Expense that will have its previousCalendarDate field set
     * @param calendarDateField The value the field previousCalendarDate will be set to
     */
    public void setPreviousCalendarDateField(Expense expense, int calendarDateField) {
        this.expenseRepository.setPreviousCalendarDateField(expense, calendarDateField);
    }

    /**
     * Sets the boolean hasPreviousCalendarDateField of the passed Expense to the boolean passed.
     *
     * @param expense The Expense that will have its hasPreviousCalendarDateField set.
     * @param bool The value that hasPreviousCalendarDateField will be set to.
     */
    public void setHasPreviousCalendarDateField(Expense expense, boolean bool) {
        String expenseName = expense.expenseName;
        this.expenseRepository.setHasPreviousCalendarDateField(expense, bool);
    }

    /**
     * Sets the boolean wasThirtyFirst of the passed Expense to the boolean passed.
     *
     * @param expense The Expense that will have its boolean wasThirtyFirst set.
     * @param bool The value that wasThirtyFirst will be set to.
     */
    public void setWasThirtyFirst(Expense expense, boolean bool) {
        this.expenseRepository.setWasThirtyFirst(expense, bool);
    }
}
