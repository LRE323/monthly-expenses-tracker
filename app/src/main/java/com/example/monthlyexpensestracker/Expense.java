package com.example.monthlyexpensestracker;

public class Expense {
    private String expenseName;
    private double expenseAmount;
    // TODO: Create an instance variable for expenseDate.


    public Expense(String expenseName, double expenseAmount) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseName() {
        return this.expenseName;
    }

    public double getExpenseAmount() {
        return this.expenseAmount;
    }

}
