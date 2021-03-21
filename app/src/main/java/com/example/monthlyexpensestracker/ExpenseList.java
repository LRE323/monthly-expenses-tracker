package com.example.monthlyexpensestracker;

import java.util.ArrayList;
import java.util.List;

public class ExpenseList {
    private List<Expense> expenses;

    public ExpenseList() {
        this.expenses = new ArrayList<>();
    }

    public void addExpense(Expense e) {
        this.expenses.add(e);
    }

    public double sumOfExpenses() {
        double sumOfExpenses = 0;
        for (Expense e: this.expenses) {
            double amount = e.returnAmount();
            sumOfExpenses += amount;
        }
        return sumOfExpenses;
    }

    @Override
    public String toString() {
        return String.valueOf(this.expenses);
    }

}
