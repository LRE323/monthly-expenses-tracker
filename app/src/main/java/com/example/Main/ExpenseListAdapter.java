package com.example.Main;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.Main.Room.Expense;

public class ExpenseListAdapter extends ListAdapter<Expense, ExpenseViewHolder> {

    public ExpenseListAdapter(@NonNull DiffUtil.ItemCallback<Expense> diffCallback) {
        super(diffCallback);
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ExpenseViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense currentExpense = getItem(position);
        holder.bind(currentExpense);
    }

    static class ExpenseDiff extends DiffUtil.ItemCallback<Expense> {
        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getExpenseName().equals(newItem.getExpenseName());
        }
    }
}