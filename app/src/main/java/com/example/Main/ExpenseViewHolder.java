package com.example.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Main.ExpenseRoom.Expense;
import com.example.monthlyexpensestracker.R;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    private final TextView expenseNameTextView;
    private final TextView expenseAmountTextView;
    private final TextView expenseDateTextView;

    private ExpenseViewHolder (View itemView) {
        super(itemView);
        expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
        expenseAmountTextView = itemView.findViewById(R.id.expenseAmountTextView);
        expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
    }

    public void bind (Expense currentExpense) {

        expenseNameTextView.setText( currentExpense.getExpenseName() );
        expenseAmountTextView.setText( "$" +  String.format("%.2f", currentExpense.getExpenseAmount())  );
        expenseDateTextView.setText( currentExpense.getExpenseDate() );

    }

    static ExpenseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenselist_item, parent, false);
        return new ExpenseViewHolder(view);
    }


}
