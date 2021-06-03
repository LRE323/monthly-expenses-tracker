package com.github.main.RecyclerViewRelated;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.github.main.RoomDatabase.Expense;

/**
 * The ListAdapter class for the RecyclerView in MainActivity
 * <p>
 * RecyclerView.Adapter base class for presenting List data in a RecyclerView, including computing
 * diffs between Lists on a background thread.
 * <p>
 * This class is a convenience wrapper around AsyncListDiffer that implements Adapter common default
 * behavior for item access and counting.
 * <p>
 * While using a LiveData<List> is an easy way to provide data to the adapter, it isn't required -
 * you can use submitList(List) when new lists are available.
 */
public class ExpenseListAdapter extends ListAdapter<Expense, ExpenseViewHolder> {

    /**
     * The listener for every expense in the list of expenses.
     */
    private final ExpenseViewHolder.OnExpenseListener onExpenseListener;

    /**
     * The context that will be passed to the ExpenseViewHolder
     */
    private final Context context;

    /**
     * Creates a new ExpenseListAdapter.
     *
     * @param diffCallback
     * @param onExpenseListener
     * @param context
     */
    public ExpenseListAdapter(@NonNull DiffUtil.ItemCallback<Expense> diffCallback,
                              ExpenseViewHolder.OnExpenseListener onExpenseListener,
                              Context context) {
        super(diffCallback);
        this.onExpenseListener = onExpenseListener;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items of the
     * given type. You can either create a new View manually or inflate it from an XML layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(ViewHolder, int, List). Since it will be re-used to display different items
     * in the data set, it is a good idea to cache references to sub views of the View to avoid
     * unnecessary View.findViewById(int) calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an
     *                 adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    @NonNull
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ExpenseViewHolder viewHolder = ExpenseViewHolder.create(parent, onExpenseListener);

        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the RecyclerView.ViewHolder.itemView to reflect the item at the given
     * position.
     * <p>
     * Note that unlike ListView, RecyclerView will not call this method again if the position of
     * the item changes in the data set unless the item itself is invalidated or the new position
     * cannot be determined. For this reason, you should only use the position parameter while
     * acquiring the related data item inside this method and should not keep a copy of it. If you
     * need the position of an item later on (e.g. in a click listener), use
     * RecyclerView.ViewHolder.getBindingAdapterPosition() which will have the updated adapter
     * position. Override onBindViewHolder(ViewHolder, int, List) instead if Adapter can handle
     * efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item
     *                 at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {

        // Get the expense at the index position.
        Expense expense = getItem(position);

        // Bind the expense to the ExpenseViewHolder.
        holder.bind(expense, this.context);
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     * <p>
     * DiffUtil.Callback serves two roles - list indexing, and item diffing. ItemCallback handles
     * just the second of these, which allows separation of code that indexes into an array or List
     * from the presentation-layer and content specific diffing code.
     */
    public static class ExpenseDiff extends DiffUtil.ItemCallback<Expense> {

        /**
         * Called to check whether two objects represent the same item.
         * <p>
         * For github, if your items have unique ids, this method should check their id equality.
         * <p>
         * Note: null items in the list are assumed to be the same as another null item and are
         * assumed to not be the same as a non-null item. This callback will not be invoked for
         * either of those cases.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the two items represent the same object or false if they are different.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem == newItem;
        }

        /**
         * Called to check whether two items have the same data.
         * <p>
         * This information is used to detect if the contents of an item have changed.
         * <p>
         * Use this method to check equality instead of Object.equals(Object) so that you can change
         * its behavior depending on your UI.
         * <p>
         * For github, if you are using DiffUtil with a RecyclerView.Adapter, you should return
         * whether the items' visual representations are the same.
         * <p>
         * This method is called only if areItemsTheSame(T, T) returns true for these items.
         * <p>
         * Note: Two null items are assumed to represent the same contents. This callback will not
         * be invoked for this case.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the contents of the items are the same or false if they are different.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {

            // For the sake of this app, if two Expenses have the same expenseName, they are equal.
            return oldItem.expenseName.equals(newItem.expenseName);
        }
    }
}