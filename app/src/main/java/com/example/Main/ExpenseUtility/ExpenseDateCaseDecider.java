package com.example.Main.ExpenseUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import RoomDatabase.Expense;

/**
 * This class is used to decide which case will be executed by ExpenseDateUpdater in order to update
 * the Expense.
 */
public class ExpenseDateCaseDecider {
    private final int currentCalendarYearField;

    /**
     * A list of months with thirty days.
     */
    List<Integer> monthsWithThirtyDays = new ArrayList<>(); //A list of months that have 30 days.

    public ExpenseDateCaseDecider(Calendar currentCalendar) {
        this.currentCalendarYearField = currentCalendar.get(Calendar.YEAR);
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
        int expenseCalendarDateField = expense.getCalendarDateField();
        int expenseCalendarMonthField = expense.getCalendarMonthField();
        boolean hasPreviousCalendarDateField = expense.hasPreviousCalendarDateField;
        boolean wasThirtyFirst = expense.wasThirtyFirst;

        // If the Expense month is December.
        if (expenseCalendarMonthField == Calendar.DECEMBER) {
            // Return case 2.
            return 2;
        }

        // If the Expense month is February AND hasPreviousCalendarDateField is true.
        if (expenseCalendarMonthField == Calendar.FEBRUARY && hasPreviousCalendarDateField) {
            // Return case 4
            return 4;
        }

        // If the Expense date is between the 1st and the 28th.
        if ((expenseCalendarDateField >= 1) && (expenseCalendarDateField <= 28)) {

            // If the Expense month is between January and November.
            if ((expenseCalendarMonthField >= Calendar.JANUARY) && (expenseCalendarMonthField <= Calendar.NOVEMBER)) {

                // Return case 1.
                return 1;
            }
        }

        // If the Expense date is the 29th.
        if (expenseCalendarDateField == 29) {

            // If the current year is a leap year.
            if (isLeapYear()) {

                // If the Expense month is between January and November, eg. not December.
                if ((expenseCalendarMonthField >= Calendar.JANUARY) && (expenseCalendarMonthField <= Calendar.NOVEMBER)) {
                    // Return case 1.
                    return 1;
                }

                // If the current year is not a leap year.
            } else {

                // If the expense month is January, there is an issue because February has 28 days.
                if (expenseCalendarMonthField == Calendar.JANUARY) {
                    // Return case 3.
                    return 3;
                }

                // If the expense month is between March and November.
                if ((expenseCalendarMonthField >= Calendar.MARCH) && (expenseCalendarMonthField <= Calendar.NOVEMBER)) {
                    // Return case 1.
                    return 1;
                }
            }
        }

        // If the Expense date is the 30th.
        if (expenseCalendarDateField == 30) {

            int nextMonth = expenseCalendarMonthField + 1;
            boolean nextMonthHasThirtyDays = this.monthsWithThirtyDays.contains(nextMonth);

            // If the Expense date was previously the 31st and the next Expense month has 31 days.
            if (wasThirtyFirst && !(nextMonthHasThirtyDays)) {
                // Return case 6.
                return 6;
            }

            // If the Expense month is January, there is an issue because February does not have
            // 30 days.
            if (expenseCalendarMonthField == Calendar.JANUARY) {
                // Return case 3
                return 3;
            }

            // If the Expense month is between March and November.
            if ((expenseCalendarMonthField >= Calendar.MARCH) && (expenseCalendarMonthField <= Calendar.NOVEMBER)) {
                // Return case 1.
                return 1;
            }

        }

        // If the Expense day is the 31st there is an issue because not all months have 31 days.
        if (expenseCalendarDateField == 31) {

            // If the current month is January there is an issue,
            // because February does not have 31 days.
            if (expenseCalendarMonthField == Calendar.JANUARY) {
                // Return case 3
                return 3;
            }

            // If the Expense month is between March and November.
            if (expenseCalendarMonthField >= Calendar.MARCH
                    && expenseCalendarMonthField <= Calendar.NOVEMBER) {

                // Create a variable to hold the integer of next month.
                int nextMonth = expenseCalendarMonthField + 1;

                // If the Expense month is July there no issues
                // because both July and August have 31 days.
                if (expenseCalendarMonthField == Calendar.JULY) {
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

        /**
         * A leap year is a year evenly divisible by 4.
         *
         * "The Gregorian calendar also stipulates that a years evenly divisible by 100
         * (for example, 1900) is a leap year only if it is ALSO evenly divisible by 400."
         *
         */
        if ((this.currentCalendarYearField % 100 == 0)
                && (this.currentCalendarYearField % 400 == 0)) {
            return true;
        } else return this.currentCalendarYearField % 4 == 0;
    }

    /**
     * Populates the list monthsWithThirtyDays with the Calendar.MONTH field of months that have 30
     * days.
     */
    private void assignMonthsWithThirtyDays() {

        // Add all the months with thirty days.
        this.monthsWithThirtyDays.add(Calendar.APRIL);
        this.monthsWithThirtyDays.add(Calendar.JUNE);
        this.monthsWithThirtyDays.add(Calendar.SEPTEMBER);
        this.monthsWithThirtyDays.add(Calendar.NOVEMBER);
    }
}
