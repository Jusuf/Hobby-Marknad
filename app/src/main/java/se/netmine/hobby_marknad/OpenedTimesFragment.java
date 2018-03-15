package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jusuf on 2017-08-16.
 */

public class OpenedTimesFragment extends BaseFragment {

    private IMainActivity mainActivity;

    LayoutInflater inflater = null;

    public String listName = "";
    public String openTimes = "";
    public String fullServiceTimes = "";

    private LinearLayout linearLayoutOpenTimes = null;
    private TextView txtOpenedTimes = null;

    private LinearLayout linearLayoutFullServiceOpenTimes = null;
    private TextView txtFullServiceTimes = null;

    public OpenedTimesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.fragment_opened_times, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(listName);
        }

        linearLayoutOpenTimes = (LinearLayout) view.findViewById(R.id.linearLayoutOpenTimes);
        txtOpenedTimes = (TextView) view.findViewById(R.id.txtOpenedTimes) ;
        linearLayoutFullServiceOpenTimes = (LinearLayout) view.findViewById(R.id.linearLayoutFullServiceOpenTimes);
        txtFullServiceTimes = (TextView) view.findViewById(R.id.txtFullServiceTimes);

        if(!empty(openTimes))
        {
            txtOpenedTimes.setText(openTimes);
            linearLayoutOpenTimes.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayoutOpenTimes.setVisibility(View.GONE);
        }

        if(!empty(fullServiceTimes))
        {
            txtFullServiceTimes.setText(fullServiceTimes);
            linearLayoutFullServiceOpenTimes.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayoutFullServiceOpenTimes.setVisibility(View.GONE);
        }

        return view;
    }



    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(listName);
    }

    public static boolean empty( final String s ) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

}
