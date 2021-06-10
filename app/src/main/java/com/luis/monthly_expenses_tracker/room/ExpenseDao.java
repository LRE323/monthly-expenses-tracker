package com.luis.monthly_expenses_tracker.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Identifies a class as a DAO class for Room. A DAO class must either be an interface or an
 * abstract class.
 */
@Dao
public interface ExpenseDao {

    /**
     * OnConflictStrategy: Set of conflict handling strategies for various DAO methods.
     * IGNORE: Ignores the conflict, and doesn't insert the Expense into the database.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Expense expense);

    /**
     * A database query is a request to access data from a database to manipulate it or retrieve it.
     * The @Query annotation requires that you provide a SQL query as a string parameter to the
     * annotation.
     */

    @Query("DELETE FROM expense_table")
    // Deletes all the Expenses from the database.
    void deleteAllExpenses();

    @Query("DELETE FROM expense_table WHERE expenseName = :expenseName")
    // Deletes the specified Expense from the database.
    void deleteAnExpense(String expenseName);

    @Query("SELECT * FROM expense_table ORDER BY expenseName ASC")
    LiveData<List<Expense>> getLiveData();

    @Query("UPDATE expense_table SET expenseDate = :expenseDate WHERE expenseName = :expenseName")
    void setExpenseDate(String expenseName, String expenseDate);

    @Query("UPDATE expense_table SET previousCalendarDateField = :day WHERE expenseName = :expenseName")
    void setPreviousCalendarDateField(String expenseName, int day);

    @Query("UPDATE expense_table SET hasPreviousCalendarDateField = :bool WHERE expenseName = :expenseName")
    void setPreviousCalendarDateField(String expenseName, boolean bool);

    @Query("UPDATE expense_table SET wasThirtyFirst = :bool WHERE expenseName = :expenseName")
    void setWasThirtyFirst(String expenseName, boolean bool);
}
