package cz.cvut.marekp11.feedreader.feed.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import cz.cvut.marekp11.feedreader.R;

/**
 * Created by gingo on 29.12.13.
 */
public class CustomLayoutDialogFragment extends DialogFragment {

    CustomLayoutDialogFragmentListener mListener;

    public static CustomLayoutDialogFragment newInstance() {
        return new CustomLayoutDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            if(context instanceof Activity) {
                mListener = (CustomLayoutDialogFragmentListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.must_implement)
                    + CustomLayoutDialogFragment.CustomLayoutDialogFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog_custom, null);

        TextView text = (TextView) customView.findViewById(R.id.title);
        text.setText(getString(R.string.feed_add_title));

        final EditText url = (EditText) customView.findViewById(R.id.url);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customView)
                .setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onAddUrlBtnClicked(url.getText().toString());
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null);

        return builder.create();
    }

    public interface CustomLayoutDialogFragmentListener {
        public void onAddUrlBtnClicked(String url);
    }

}
