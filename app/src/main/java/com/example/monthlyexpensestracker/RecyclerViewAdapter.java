package com.example.monthlyexpensestracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //This will hold the data in the adapter.
    private final LinkedList<Expense> expenses;

    //RecyclerViewAdapter needs a constructor that initializes the word list from the data.
    //To create a View for a list item, the RecyclerViewAdapter needs to inflate the XML for a list item.
    //You use a layout inflater for that job. LayoutInflater reads a layout XML description and converts
    //it into the corresponding View items.
    private LayoutInflater inflater;

    //The constructor for RecyclerViewAdapter needs to have a context parameter, and a linked list
    //of words with the app's data. The method needs to instantiate a LayoutInflater for inflater
    //and set expenses to the passed in data.
    public RecyclerViewAdapter(Context context, LinkedList<Expense> expenses) {
        inflater = LayoutInflater.from(context);
        this.expenses = expenses;
    }

    @NonNull
    @Override
    // The onCreateViewHolder() method is similar to the onCreate() method. It inflates the item
    // layout, and returns a ViewHolder with the layout and the adapter.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.expenselist_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    // The onBindViewHolder() method connects your data to the view holder.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current Expense.
        Expense currentExpense = expenses.get(position);
        // Get the current expenseName
        String currentExpenseName = currentExpense.getExpenseName();
        //Set expenseNameTextView text to currentExpenseName
        holder.expenseNameTextView.setText(currentExpenseName);

        // Get the current expenseAmount
        double currentExpenseAmount = currentExpense.getExpenseAmount();
        //Set expenseAmountTextView text to currentExpenseAmount
        holder.expenseAmountTextView.setText("$" + String.valueOf(currentExpenseAmount));

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    //The inner ViewHolder class.
    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView expenseNameTextView;
        public final TextView expenseAmountTextView;
        public final TextView expenseDateTextView;
        final RecyclerViewAdapter adapter;

        public ViewHolder(View itemView, RecyclerViewAdapter adapter) {
            super(itemView);
            // Initialize all TextViews in headersHolder
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseAmountTextView = itemView.findViewById(R.id.expenseAmountTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
            this.adapter = adapter;
        }
    }

}
