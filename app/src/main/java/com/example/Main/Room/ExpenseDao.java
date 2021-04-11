package com.example.Main.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// Identifies a class as a DAO class for Room. DAOs must be either an interfaces or
// abstract classes.
@Dao
public interface ExpenseDao {

    /*
    OnConflictStrategy: Set of conflict handling strategies for various Dao methods.
    IGNORE: OnConflict strategy constant to ignore the conflict.
    The selected onConflict strategy ignores a new Expense
    */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Expense expense);

    /**
     * A database query is a request to access data from a database to manipulate it or retrieve it.
     * @Query requires that you provide a SQL query as a string parameter to the annotation.
     */

    @Query("DELETE FROM expense_table")
    // Declares a method to delete all the Expenses.
    void deleteAll();

    // Returns a list of alphabetized Expenses.
    @Query("SELECT * FROM expense_table ORDER BY expense_name ASC")
    LiveData< List<Expense> > getAlphabetizedExpenses();
}
