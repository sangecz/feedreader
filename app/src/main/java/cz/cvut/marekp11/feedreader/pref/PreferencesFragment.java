package cz.cvut.marekp11.feedreader.pref;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cz.cvut.marekp11.feedreader.R;

/**
 * Created by sange on 18/05/16.
 */
public class PreferencesFragment extends Fragment {

    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    public PreferencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref, container, false);
    }
}
