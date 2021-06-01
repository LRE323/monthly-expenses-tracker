package com.example.Main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * A DialogFragment is a special fragment subclass that is designed for creating and hosting
 * dialogs. Strictly speaking, you do not need to host your dialog within a fragment, but doing so
 * allows the FragmentManager to manage the state of the dialog and automatically restore the dialog
 * when a configuration change occurs.
 */
public class DatePickerFragment extends DialogFragment {

    /**
     * Override to build your own custom Dialog container. This is typically used to show an
     * AlertDialog instead of a generic Dialog; when doing so,
     * onCreateView(LayoutInflater, ViewGroup, Bundle) does not need to be implemented since the
     * AlertDialog takes care of its own content.
     * <p>
     * This method will be called after onCreate(Bundle) and before
     * onCreateView(LayoutInflater, ViewGroup, Bundle). The default implementation simply
     * instantiates and returns a Dialog class.
     * <p>
     * Note: DialogFragment own the Dialog.setOnCancelListener and Dialog.setOnDismissListener
     * callbacks. You must not set them yourself. To find out about these events,
     * override onCancel(DialogInterface) and onDismiss(DialogInterface).
     *
     * @param saveInstanceState The last saved instance state of the Fragment, or null if this is a
     *                          freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }
}
