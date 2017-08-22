package org.capitalsav.user1.tasklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.example.user1.tasklist.R;

public class EditStageDialogFragment extends DialogFragment {
    private EditText mEditText;

    private EditStageDialogListener mEditStageDialogListener;

    public interface EditStageDialogListener {
        void onFinishEditStageDialog(DialogFragment dialogFragment);
    }

    public EditStageDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View transactionLayout = View.inflate(getActivity(), R.layout.edit_dialog_fragment, null);
        builder.setView(transactionLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send the positive button event back to the calling activity
                        mEditStageDialogListener.onFinishEditStageDialog(EditStageDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity){
            activity=(Activity) context;
            try {
                mEditStageDialogListener = (EditStageDialogListener) activity;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(activity.toString()
                        + " must implement EditStageDialogListener");
            }
        }

    }
}
