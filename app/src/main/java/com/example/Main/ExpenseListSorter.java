package com.example.Main;

import java.util.Calendar;
import java.util.List;

import RoomDatabase.Expense;

public class ExpenseListSorter {

    ExpenseListSorter() {

    }

    /**
     * Sorts the passed list of expenses by date, from earliest to latest date.
     *
     * @param expenseList The expense list to be sorted.
     */
    public void sort(List<Expense> expenseList) {
        // Go through each expense in expenseList
        for (int i = 1; i < expenseList.size(); i++) {

            // Variable j is the placeholder for the original, pre-sorted position of the current
            // expense being iterated. It is used so that the for-loop variable (i) is not modified.
            int j = i;

            /*
              This while loop checks if the expense in expenseList at index j has an expenseDate
              that is before the previous expense in expenseList, at index (j - 1).

              If it is, the two expenses are swapped and it continues checking with the new position
              of the expense that was originally at index i in expenseList.
             */
            while (j > 0) {

                // Get the expense at the current index (j) and the expense at the previous
                // index (j - 1).
                Expense currentExpenseObject = expenseList.get(j);
                Expense previousExpenseObject = expenseList.get(j - 1);

                // Get the Calendar of currentExpenseObject and previousExpenseObject.
                Calendar currentExpenseCalendar = currentExpenseObject.getCalendar();
                Calendar previousExpenseCalendar = previousExpenseObject.getCalendar();

                // Check currentExpenseCalendar is before previousExpenseCalendar. If so, swap.
                if (currentExpenseCalendar.before(previousExpenseCalendar)) {

                    // Swap.
                    expenseList.set(j - 1, currentExpenseObject);
                    expenseList.set(j, previousExpenseObject);

                    // Go down the list and continue checking.
                    j--;
                } else {
                    break;
                }
            }
        }
    }
}
