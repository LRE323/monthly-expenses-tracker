package com.example.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.monthlyexpensestracker.R;

import RoomDatabase.Expense;

/**
 * The ViewHolder class for the RecyclerView in MainActivity.
 * <p>
 * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
 * <p>
 * RecyclerView.Adapter implementations should subclass ViewHolder and add fields for caching
 * potentially expensive View.findViewById(int) results.
 * <p>
 * While RecyclerView.LayoutParams belong to the RecyclerView.LayoutManager, ViewHolders belong to
 * the adapter. Adapters should feel free to use their own custom ViewHolder implementations to
 * store data that makes binding view contents easier. Implementations should assume that individual
 * item views will hold strong references to ViewHolder objects and that RecyclerView instances may
 * hold strong references to extra off-screen item views for caching purposes
 */
public class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    /**
     * The TextView that shows the expenseName of an expense.
     */
    private final TextView tvExpenseName;

    /**
     * The TextView that shows the expenseAmount of an expense.
     */
    private final TextView tvExpenseAmount;

    /**
     * The TextView that  shows the expenseDate of an expense.
     */
    private final TextView tvExpenseDate;

    /**
     * The OnExpenseListener for each expense.
     */
    private final OnExpenseListener onExpenseListener;

    private ExpenseViewHolder(View itemView, OnExpenseListener onExpenseListener) {
        super(itemView);

        // Create the required UI objects.
        tvExpenseName = itemView.findViewById(R.id.expenseNameTextView);
        tvExpenseAmount = itemView.findViewById(R.id.expenseAmountTextView);
        tvExpenseDate = itemView.findViewById(R.id.expenseDateTextView);

        // Create the onExpenseListener for each expense.
        this.onExpenseListener = onExpenseListener;

        // Attach the onLongClickListener to the entire ViewHolder.
        // "this" refers to the interface implemented, View.OnLongClickListener
        itemView.setOnLongClickListener(this);
    }

    /**
     * Sets the text for expenseName, expenseAmount, and expenseDate for each item in the list of
     * expenses.
     *
     * @param expense The current expense in the list.
     */
    public void bind(Expense expense, Context context) {

        // Set the text of expenseName.
        tvExpenseName.setText(expense.expenseName);

        // Format expenseAmount to two decimal places.
        String expenseAmountFormatted = String.format("%.2f", expense.expenseAmount);

        // The text that expenseAmount will be set to.
        String text = context.getString(R.string.expense_amount, expenseAmountFormatted);

        // Set the text for expenseAmount.
        tvExpenseAmount.setText(text);

        // Set the text for expenseDate.
        tvExpenseDate.setText(expense.expenseDate);

    }

    /**
     * Creates a new ExpenseViewHolder
     *
     * @param parent            The parent ViewGroup
     * @param onExpenseListener The onExpenseListener interface for this ExpenseViewHolder.
     * @return A new ExpenseViewHolder.
     */
    static ExpenseViewHolder create(ViewGroup parent, OnExpenseListener onExpenseListener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenselist_item, parent, false);
        return new ExpenseViewHolder(view, onExpenseListener);
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {
        onExpenseListener.onExpenseClick(getAdapterPosition());
        return true;
    }

    /**
     * The OnClickListener for each expense.
     */
    public interface OnExpenseListener {
        // This method will send the position of the clicked item.
        void onExpenseClick(int position);
    }
}
