package com.example.Main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import RoomDatabase.Expense;
import RoomDatabase.ExpenseViewModel;

/**
 * This class is used  to decide which case will be executed depending on the full Expense date
 * using the method .decideCase(expense).
 */
public class ExpenseDateCaseDecider {
    private final int currentYearInteger; // The current year is an integer.
    List<Integer> monthsWithThirtyDays; //A list of months that have 30 days.

    public ExpenseDateCaseDecider(Calendar calendar) {
        this.currentYearInteger = calendar.get(Calendar.YEAR);
        this.assignMonthsWithThirtyDays();
    }

    /**
     * This methods decides the case (1 through 6) for an Expense that needs to be updated.
     *
     * @param expense The Expense for which a case is requested.
     * @return int The case integer that will decide how the Expense will be updated.
     */
    public int decideCase(Expense expense) {

        // Get all the required objects.
        int expenseDayInt = expense.getDayInt();
        int expenseMonthInt = expense.getMonthInt();
        boolean hasPreviousJanuaryDay = expense.hasPreviousJanuaryDay;
        boolean wasThirtyFirst = expense.wasThirtyFirst;

        // If the Expense month is December.
        if (expenseMonthInt == Calendar.DECEMBER) {
            // Return case 2.
            return 2;
        }

        // If Expense month is February AND hasPreviousJanuaryDay is true.
        if (expenseMonthInt == Calendar.FEBRUARY && hasPreviousJanuaryDay) {
            // Return case 4
            return 4;
        }

        // If the Expense day is between the 1st and the 28th.
        if ((expenseDayInt >= 1) && (expenseDayInt <= 28)) {

            // If the Expense month is between January and November.
            if ((expenseMonthInt >= Calendar.JANUARY) && (expenseMonthInt <= Calendar.NOVEMBER)) {

                // Return case 1.
                return 1;
            }
        }

        // If the Expense day is the 29th.
        if (expenseDayInt == 29) {

            // If the current year is a leap year.
            if (isLeapYear()) {

                // If the Expense month is between January and November, eg. not December.
                if ((expenseMonthInt >= Calendar.JANUARY) && (expenseMonthInt <= Calendar.NOVEMBER)) {
                    // Return case 1.
                    return 1;
                }

            // If the current year is not a leap year.
            } else {

                // If the expense month is January, there is an issue because February has 28 days.
                if (expenseMonthInt == Calendar.JANUARY) {
                    // Return case 3.
                    return 3;
                }

                // If the expense month is between March and November.
                if ((expenseMonthInt >= Calendar.MARCH) && (expenseMonthInt <= Calendar.NOVEMBER)) {
                    // Return case 1.
                    return 1;
                }
            }
        }

        // If the Expense day is the 30th.
        if (expenseDayInt == 30) {

            int nextMonth = expenseMonthInt + 1;
            boolean nextMonthHasThirtyDays = this.monthsWithThirtyDays.contains(nextMonth);

            // If the Expense day was previously the 31st and the next Expense month has 31 days.
            if (wasThirtyFirst && !(nextMonthHasThirtyDays)) {
                // Return case 6.
                return 6;
            }

            // If the Expense month is January, there is an issue because February does not have
            // 30 days.
            if (expenseMonthInt == Calendar.JANUARY) {
                // Return case 3
                return 3;
            }

            // If the Expense month is between March and November.
            if ((expenseMonthInt >= Calendar.MARCH) && (expenseMonthInt <= Calendar.NOVEMBER)) {
                // Return case 1.
                return 1;
            }

        }

        // If the Expense day is the 31st there is an issue because not all months have 31 days.
        if (expenseDayInt == 31) {

            // If the current month is January there is an issue,
            // because February does not have 31 days.
            if (expenseMonthInt == Calendar.JANUARY) {
                // Return case 3
                return 3;
            }

            // If the Expense month is between March and November.
            if (expenseMonthInt >= Calendar.MARCH && expenseMonthInt <= Calendar.NOVEMBER) {

                // Create a variable to hold the integer of next month.
                int nextMonth = expenseMonthInt + 1;

                // If the Expense month is July there no issues
                // because both July and August have 31 days.
                if (expenseMonthInt == Calendar.JULY) {
                    // Return case 1.
                    return 1;
                }

                // For March through November, excluding July.
                // If next month only has 30 days (April, June, September, November)
                if (this.monthsWithThirtyDays.contains(nextMonth)) {
                    // Return case 8.
                    return 5;
                }
            }
        }
        return -1;
    }

    /**
     * Returns whether the current year is a leap year or not.
     *
     * @return boolean Whether the current year is a leap year or not.
     */
    boolean isLeapYear() {

        // The Gregorian calendar stipulates that a years evenly divisible by 100
        // (for example, 1900) is a leap year only if it is ALSO evenly divisible by 400.
        if (this.currentYearInteger % 100 == 0) {
            if (this.currentYearInteger % 400 == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (this.currentYearInteger % 4 == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Populates the monthsWithThirtyDays with the integers of months that have thirty days.
     */
    private void assignMonthsWithThirtyDays() {
        // Create the ArrayList
        this.monthsWithThirtyDays = new ArrayList<>();

        // Add all the months with thirty days.
        this.monthsWithThirtyDays.add(Calendar.APRIL);
        this.monthsWithThirtyDays.add(Calendar.JUNE);
        this.monthsWithThirtyDays.add(Calendar.SEPTEMBER);
        this.monthsWithThirtyDays.add(Calendar.NOVEMBER);
    }
}
