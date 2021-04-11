package com.example.Main.Room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseRepository {

    private ExpenseDao expenseDao;
    private LiveData< List<Expense> > allExpenses;

    // Constructor method.
    ExpenseRepository(Application application) {

        // Access the database, creating it if it's the first time it's being accessed.
        ExpenseRoomDatabase db = ExpenseRoomDatabase.getDatabase(application);

        // This is calling an abstract method, not sure what is going on here.
        // Assign a value to expenseDao(?)
        expenseDao = db.expenseDao();

        // Create a List<Expense> sorted by ascending expenseName.
        allExpenses = expenseDao.getAlphabetizedExpenses();
    }

    // Returns the LiveData list of Expenses from Room.
    LiveData< List<Expense> > getAllExpenses() {
        return allExpenses;
    }

    /*
    You must call this on a non-UI thread or your app will throw an exception. Room ensures
    that you're not doing any long running operations on the main thread, blocking the UI.

    We need to not run the insert on the main thread so we use the ExecutorService we created in
    the WordRoomDatabase to perform the insert on a background thread.
    */
    void insert(Expense expense) {
        ExpenseRoomDatabase.databaseWriteExecutor.execute(() -> {
            expenseDao.insert(expense);
        });
    }
}