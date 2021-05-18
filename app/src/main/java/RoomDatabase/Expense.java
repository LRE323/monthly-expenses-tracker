package RoomDatabase;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/*
Entity: Annotated class that describes a database table when working with Room.
Each @Entity class represents a SQLite table. Annotate your class declaration to indicate that it's
an entity. You can specify the name of the table if you want it to be different from the name
of the class. This names the table "word_table".
 */
@Entity(tableName = "expense_table")
public class Expense implements Parcelable {
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


    // Instance variables to be ignored.
    @Ignore
    private final DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.LONG);


    @Ignore
    private final String LOG_TAG = "Expense";

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

    public Calendar getExpenseDateCalendar() {

        Date date = null;

        try {

            date = this.mDateFormat.parse(this.expenseDate);

        } catch (ParseException e) {

        }
        // Create new Calendar and set the time.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    public int getDayInt() {

        int day = getExpenseDateCalendar().get(Calendar.DATE);

        Log.i(LOG_TAG, this.toString() + ": DAY = " + day);

        return day;
    }

    public int getMonthInt() {
        int month = getExpenseDateCalendar().get(Calendar.MONTH);

        Log.i(LOG_TAG, this.toString() + ": MONTH = " + month);

        return month;
    }

    public int getYearInt() {
        int year = getExpenseDateCalendar().get(Calendar.YEAR);

        Log.i(LOG_TAG, this.toString() + ": YEAR = " + year);

        return year;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public void setExpenseDate(Calendar calendar) {

    }

    @Override
    public String toString() {
        return this.expenseName;
    }

    // Parcelable methods
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
