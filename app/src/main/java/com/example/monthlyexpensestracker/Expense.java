package com.example.monthlyexpensestracker;

public class Expense {
    private double amount;
    private String name;

    public Expense(double amount) {
        this.amount = amount;
    }

    public double returnAmount() {
        return this.amount;
    }

}
