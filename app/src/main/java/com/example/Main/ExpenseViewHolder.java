package com.example.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import RoomDatabase.Expense;
import com.example.monthlyexpensestracker.R;

// A ViewHolder describes an item view and metadata about its place within  the RecyclerView.
public class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    private final TextView tvExpenseName;
    private final TextView tvExpenseAmount;
    private final TextView tvExpenseDate;
    private final OnExpenseListener onExpenseListener;

    private ExpenseViewHolder (View itemView, OnExpenseListener onExpenseListener) {
        super(itemView);
        tvExpenseName = itemView.findViewById(R.id.expenseNameTextView);
        tvExpenseAmount = itemView.findViewById(R.id.expenseAmountTextView);
        tvExpenseDate = itemView.findViewById(R.id.expenseDateTextView);
        this.onExpenseListener = onExpenseListener;

        // Attach the onLongClickListener to the entire ViewHolder.
        // "this" refers to the interface implemented, View.OnLongClickListener
        itemView.setOnLongClickListener(this);
    }

    public void bind (Expense currentExpense) {
        tvExpenseName.setText( currentExpense.getExpenseName() );
        tvExpenseAmount.setText( "$" +  String.format("%.2f", currentExpense.getExpenseAmount())  );
        tvExpenseDate.setText( currentExpense.getExpenseDate() );

    }

    static ExpenseViewHolder create(ViewGroup parent, OnExpenseListener onExpenseListener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenselist_item, parent, false);
        return new ExpenseViewHolder(view, onExpenseListener);
    }

    @Override
    public boolean onLongClick(View v) {
        onExpenseListener.onExpenseClick(getAdapterPosition());
        return true;
    }

    public interface OnExpenseListener {

         // This method will send the position of the clicked item.
        void onExpenseClick(int position);
    }
}
