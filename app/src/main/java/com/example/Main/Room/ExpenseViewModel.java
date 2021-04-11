package com.example.Main.Room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    // Private member that holds a reference to the repository.
    private ExpenseRepository expenseRepository;

    private final LiveData< List<Expense> > allExpenses;

    public ExpenseViewModel(Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        allExpenses = expenseRepository.getAllExpenses();
    }

    LiveData< List<Expense> > getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        expenseRepository.insert(expense);
    }
}
