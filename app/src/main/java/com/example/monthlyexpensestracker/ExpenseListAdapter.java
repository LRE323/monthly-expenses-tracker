package com.example.monthlyexpensestracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {

    //The inner ViewHolder class.
    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public final TextView expenseItemView;
        final ExpenseListAdapter adapter;

        public ExpenseViewHolder(View itemView, ExpenseListAdapter adapter) {
            super(itemView);
            expenseItemView = itemView.findViewById(R.id.expense);
            this.adapter = adapter;
        }
    }

    //This will hold the data in the adapter.
    private final LinkedList<String> expenses;

    //WordListAdapter needs a constructor that initializes the word list from the data.
    //To create a View for a list item, the WordListAdapter needs to inflate the XML for a list item.
    //You use a layout inflater for that job. LayoutInflater reads a layout XML description and converts
    //it into the corresponding View items.
    private LayoutInflater inflater;

    //The constructor for ExpenseListAdapter needs to have a context parameter, and a linked list
    //of words with the app's data. The method needs to instantiate a LayoutInflater for inflater
    //and set expenses to the passed in data.
    public ExpenseListAdapter(Context context, LinkedList<String> expenses) {
        inflater = LayoutInflater.from(context);
        this.expenses = expenses;
    }

    @NonNull
    @Override
    // The onCreateViewHolder() method is similar to the onCreate() method. It inflates the item
    // layout, and returns a ViewHolder with the layout and the adapter.
    public ExpenseListAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.expenselist_item, parent, false);
        return new ExpenseViewHolder(itemView, this);
    }

    @Override
    // The onBindViewHolder() method connects your data to the view holder.
    public void onBindViewHolder(@NonNull ExpenseListAdapter.ExpenseViewHolder holder, int position) {
        String current = expenses.get(position);
        holder.expenseItemView.setText(current);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
