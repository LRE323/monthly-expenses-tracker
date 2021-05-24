package RoomDatabase;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    // Private member that holds a reference to the repository.
    private ExpenseRepository expenseRepository;

    private final LiveData<List<Expense>> allExpenses;
    private List<Expense> mExpenseList;

    public ExpenseViewModel(Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        allExpenses = expenseRepository.getExpensesAsLiveData();
    }

    public LiveData<List<Expense>> getExpensesAsLiveData() {
        return allExpenses;
    }

    public List<Expense> getExpenseList() {
        return this.mExpenseList;
    }

    public void insert(Expense expense) {
        expenseRepository.insert(expense);
    }

    public void deleteAll() {
        expenseRepository.deleteAll();
    }

    public void deleteExpense(Expense expense) {

        expenseRepository.deleteAnExpense(expense.getExpenseName());
    }

    public void setExpenseDate(Expense expense, Calendar calendar, DateFormat dateFormat) {

        // Get name and date of the Expense.
        String expenseName = expense.getExpenseName();
        String expenseDate = dateFormat.format(calendar.getTime());

        expenseRepository.setExpenseDate(expenseName, expenseDate);
    }

    public void setPreviousJanuaryDay(Expense expense, int day) {
        String expenseName = expense.getExpenseName();
        expenseRepository.setPreviousJanuaryDay(expenseName, day);
    }

    public void setHasPreviousJanuaryDay(Expense expense, boolean bool) {
        String expenseName = expense.getExpenseName();
        this.expenseRepository.setHasPreviousJanuaryDay(expenseName, bool);
    }

    public void setWasThirtyFirst(Expense expense, boolean b) {
        String expenseName = expense.getExpenseName();
        this.expenseRepository.setWasThirtyFirst(expenseName, b);
    }
}
