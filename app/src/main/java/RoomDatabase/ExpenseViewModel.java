package RoomDatabase;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public void setExpenseDate(String expenseName, String expenseDate) {
        expenseRepository.setExpenseDate(expenseName, expenseDate);
    }
}
