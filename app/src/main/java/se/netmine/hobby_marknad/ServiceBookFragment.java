package se.netmine.hobby_marknad;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by jusuf on 2017-08-14.
 */

public class ServiceBookFragment extends BaseFragment {

    private IMainActivity mainActivity;
    private LinearLayout testDemoButton = null;
    private LinearLayout connectButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_book, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("Service book");
        }

        testDemoButton = (LinearLayout) view.findViewById(R.id.testDemoButton);

        testDemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceBookDemoFragment fragment = new ServiceBookDemoFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        connectButton = (LinearLayout) view.findViewById(R.id.connectButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment connectServiceDialog = new ConnectServiceDialog();
                connectServiceDialog.show(getFragmentManager(), "Modal");
            }
        });

        return view;
    }

    public ServiceBookFragment(){}

}
