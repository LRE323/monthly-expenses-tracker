package com.luis.monthly_expenses_tracker;

import com.luis.monthly_expenses_tracker.expense_utility.ExpenseDateUpdater;

import junit.framework.TestCase;

import org.junit.Test;

import com.luis.monthly_expenses_tracker.room.Expense;

public class ExpenseDateUpdaterTest extends TestCase {
    private ExpenseDateUpdater expenseDateUpdater = new ExpenseDateUpdater(null);

    // An expense with the current year, but previous month.
    private Expense expense1 = new Expense("Expense", 100,
            "January 1, 2021");

    @Test
    public void outOfDate() {
        assertTrue(expenseDateUpdater.expenseIsOutOfDate(expense1));
    }

}