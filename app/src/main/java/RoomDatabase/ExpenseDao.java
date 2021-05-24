package RoomDatabase;

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

    @Query("DELETE FROM expense_table WHERE expense_name = :expenseName")
    void deleteAnExpense(String expenseName);

    // Returns a LiveData list of expenses.
    @Query("SELECT * FROM expense_table ORDER BY expense_name ASC")
    LiveData< List<Expense> > getExpensesAsLiveData();

    @Query("UPDATE expense_table SET expense_date = :expenseDate WHERE expense_name = :expenseName")
    void setExpenseDate(String expenseName, String expenseDate);

    @Query("UPDATE expense_table SET previous_january_day = :day WHERE expense_name = :expenseName" )
    void setPreviousJanuaryDay(String expenseName, int day);

    @Query("UPDATE expense_table SET has_previous_january_day = :bool WHERE expense_name = :expenseName")
    void setHasPreviousJanuaryDay(String expenseName, boolean bool);

    @Query("UPDATE expense_table SET was_thirty_first = :bool WHERE expense_name = :expenseName")
    void setWasThirtyFirst(String expenseName, boolean bool);
}
