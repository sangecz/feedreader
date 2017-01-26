package cz.cvut.marekp11.feedreader.feed.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import cz.cvut.marekp11.feedreader.R;

import static cz.cvut.marekp11.feedreader.data.DbConstants.ID;

public class ClosableDialogFragment extends DialogFragment {


    private ClosableDialogFragmentListener mListener;

    public static ClosableDialogFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(ID, id);

        ClosableDialogFragment fragment = new ClosableDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public ClosableDialogFragment() {
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        try {
            mListener = (ClosableDialogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.must_implement)
                    + ClosableDialogFragment.ClosableDialogFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String toDeleteId = null;
        if(getArguments() != null) {
            toDeleteId = getArguments().getString(ID);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String finalToDeleteId = toDeleteId;
        builder.setMessage(R.string.feed_del_msg)
                .setTitle(R.string.feed_del_title)
                .setPositiveButton(R.string.btn_del, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDeleteBtnClicked(finalToDeleteId);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    public interface ClosableDialogFragmentListener {
        public void onDeleteBtnClicked(String id);
    }
}
