package com.example.Main.Room;


import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
Entity: Annotated class that describes a database table when working with Room.
Each @Entity class represents a SQLite table. Annotate your class declaration to indicate that it's
an entity. You can specify the name of the table if you want it to be different from the name
of the class. This names the table "word_table".
 */
@Entity(tableName = "expense_table")
public class Expense {
    // Did not auto generate key because I don't want the user to create multiple Expenses with the
    // same expenseName.

    // Each Room entity must identify a primary key that uniquely identifies each row in the
    // corresponding database table.
    @PrimaryKey
    @NonNull  // Denotes that a parameter, field, or method return value can never be null.
    @ColumnInfo(name = "expense_name") // Specifies the name of the column.
    private String expenseName;

    @ColumnInfo(name = "expense_amount")
    private double expenseAmount;

    @NonNull
    @ColumnInfo(name = "expense_date")
    private String expenseDate;

    public Expense(@NonNull String expenseName, double expenseAmount, @NonNull String expenseDate) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
    }

    /**
     * Every field that's stored in the database needs to be either public (private in this class)
     * or have a "getter" method.
     */

    public String getExpenseName() {
        return this.expenseName;
    }

    public double getExpenseAmount() {
        return this.expenseAmount;
    }

    public String getExpenseDate() {
        return this.expenseDate;
    }

}
