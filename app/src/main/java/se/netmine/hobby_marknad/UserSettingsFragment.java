package se.netmine.hobby_marknad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by jusuf on 2017-08-14.
 */

public class UserSettingsFragment extends BaseFragment {

    private IMainActivity mainActivity;

    private ScrollView scrollViewUserSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.my_page));
        }



        scrollViewUserSettings = (ScrollView) view.findViewById(R.id.scrollViewUserSettings);
        OverScrollDecoratorHelper.setUpOverScroll(scrollViewUserSettings);

        return view;
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(getString(R.string.my_page));
    }

    public UserSettingsFragment(){}

}
