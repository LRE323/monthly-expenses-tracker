package com.example.monthlyexpensestracker;

public class Expense {
    private String expenseName;
    private double expenseAmount;


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
