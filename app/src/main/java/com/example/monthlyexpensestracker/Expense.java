package com.example.monthlyexpensestracker;


import android.icu.util.Calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Expense {
    private String expenseName;
    private double expenseAmount;
    private Calendar expenseDate;


    public Expense(String expenseName, double expenseAmount, Calendar expenseDate) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
    }

    public String getExpenseName() {
        return this.expenseName;
    }

    public double getExpenseAmount() {
        return this.expenseAmount;
    }

    public String getExpenseDateString() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String expenseDateString = dateFormat.format( this.expenseDate.getTime() );
        return expenseDateString;
    }

}
