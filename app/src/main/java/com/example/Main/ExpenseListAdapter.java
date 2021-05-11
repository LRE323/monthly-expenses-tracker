package com.example.Main;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import RoomDatabase.Expense;

// ListAdapter is a RecyclerView.Adapter base class for presenting List data in a RecyclerView,
// including computing diffs between Lists on a background thread. While using a LiveData<List> is
// an easy way to provide data to the adapter, it isn't required - you can use submitList(List) when
// new lists are available.

public class ExpenseListAdapter extends ListAdapter<Expense, ExpenseViewHolder> {
    private final ExpenseViewHolder.OnExpenseListener onExpenseListener;


    // Constructor.
    public ExpenseListAdapter(@NonNull DiffUtil.ItemCallback<Expense> diffCallback, ExpenseViewHolder.OnExpenseListener onExpenseListener) {
        super(diffCallback);
        this.onExpenseListener = onExpenseListener;
    }

    /*
    Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent
    an item.

    This new ViewHolder should be constructed with a new View that can represent the items of the
    given type. You can either create a new View manually or inflate it from an XML layout file.

     */
    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ExpenseViewHolder.create(parent, onExpenseListener);
    }

    /*
    Called by RecyclerView to display the data at the specified position.

    Note that unlike ListView, RecyclerView will not call this method again if the position of the
    item changes in the data set unless the item itself is invalidated or the new position cannot be
    determined. For this reason, you should only use the position parameter while acquiring the
    related data item inside this method and should not keep a copy of it. If you need the position
    of an item later on (e.g. in a click listener), use
    RecyclerView.ViewHolder.getBindingAdapterPosition() which will have the updated adapter position.

     */
    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        /*
        holder: The ViewHolder which should be updated to represent the contents of the item at
        the given position in the data set.

        position: The position of the item within the adapter's data set.
        */
        Expense currentExpense = getItem(position);

        // onBindViewHolder should update the contents of RecyclerView.ViewHolder.itemView to reflect
        // the item at the given position.
        holder.bind(currentExpense);
    }

    /*
    Callback for calculating the diff between two non-null items in a list.

    DiffUtil.Callback serves two roles - list indexing and item diffing. ItemCallback handles just
    the second of these, which allows separation of code that indexes into an array or List from the
     presentation-layer and content specific diffing code.
    */
    static class ExpenseDiff extends DiffUtil.ItemCallback<Expense> {

        // Called to check whether two objects represent the same item.
        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem == newItem;
        }

        // Called to check whether two items have the same data.
        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {

            // For the sake of this app, if two Expenses have the expenseName, they are equal.
            return oldItem.getExpenseName().equals(newItem.getExpenseName());
        }
    }
}