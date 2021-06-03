package com.github.main.RoomDatabase;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * The @Entity annotation indicates Room that this class is an entity.
 *
 * Each @Entity class represents a SQLite database table.
 */
@Entity(tableName = "expense_table")

public class Expense implements Parcelable {
    /** A primary key the uniquely identifies each row in the database table must declared.*/
    @PrimaryKey
    @NonNull
    public String expenseName; // Name of the Expense.

    public double expenseAmount; // Money amount of the Expense.

    @NonNull
    public String expenseDate; // The full date on which the Expense will be charged.

    public int previousCalendarDateField; // A previous field of Calendar.DATE for this Expense.

    // If the Expense has a previous Calendar.DATE field.
    public boolean hasPreviousCalendarDateField;

    // The the Calendar.DATE field of this Expense was previously set to 31.
    public boolean wasThirtyFirst;

    // Instance variables to be ignored.
    @Ignore
    private final DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.LONG);

    @Ignore
    private final String LOG_TAG = "ExpenseClass";

    public Expense(@NonNull String expenseName, double expenseAmount, @NonNull String expenseDate) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;

    }

    /**
     * Parses the value of expenseDate into a Calendar object and returns the Calendar object.
     *
     * @return calendar A Calendar object, parsed from expenseDate
     */
    public Calendar getCalendar() {

        // Create a null Date object, to hold the value of the parsed expenseDate
        Date date = null;

        try {

            date = this.mDateFormat.parse(this.expenseDate);

        } catch (ParseException e) {

        }
        // Create new Calendar and set the time to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    /**
     * Returns the Calendar.DATE field of the parsed expenseDate.
     *
     * @return calendarDateField The Calendar.DATE field from the parsed expenseDate
     */
    public int getCalendarDateField() {
        int calendarDateField = getCalendar().get(Calendar.DATE);

        return calendarDateField;
    }


    /**
     * Returns the Calendar.MONTH field of the parsed expenseDate.
     *
     * @return calendarMonthField The Calendar.MONTH field from the parsed expenseDate
     */
    public int getCalendarMonthField() {
        int calendarMonthField = getCalendar().get(Calendar.MONTH);

        return calendarMonthField;
    }

    @Override
    public String toString() {
        return this.expenseName;
    }

    /**
     * The following are all implemented parcelable methods.
     */

    protected Expense(Parcel in) {
        expenseName = in.readString();
        expenseAmount = in.readDouble();
        expenseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expenseName);
        dest.writeDouble(expenseAmount);
        dest.writeString(expenseDate);
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}
