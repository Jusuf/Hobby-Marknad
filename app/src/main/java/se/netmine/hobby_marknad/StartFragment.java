package se.netmine.hobby_marknad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by jusuf on 2017-08-14.
 */

public class StartFragment extends BaseFragment {

    private IMainActivity mainActivity;

    private ScrollView scrollViewStart;
    private LinearLayout myHobbyBookMenuButton;
    private LinearLayout serviceBookMenuButton;
    private LinearLayout catalogueAndMagazinesMenuButton;
    private LinearLayout faqMenuButton;
    private LinearLayout campingsMenuButton;
    private LinearLayout resellersMenuButton;
    private LinearLayout userSettingsMenuButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("Hobby");
        }

        myHobbyBookMenuButton = (LinearLayout) view.findViewById(R.id.myHobbyMenu);
        myHobbyBookMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mainActivity.getContext().getPackageManager().getLaunchIntentForPackage("se.netmine.myhobby");
                if (intent != null) {
                    // We found the activity now start the activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Bring user to the market or let them choose an app?
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "se.netmine.myhobby"));
                    startActivity(intent);
                }
            }
        });

        serviceBookMenuButton = (LinearLayout) view.findViewById(R.id.serviceBookMenu);
        serviceBookMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceBookFragment fragment = new ServiceBookFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        catalogueAndMagazinesMenuButton = (LinearLayout) view.findViewById(R.id.catalogueAndMagazinesMenu);

        catalogueAndMagazinesMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogueAndMagazinesFragment fragment = new CatalogueAndMagazinesFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        faqMenuButton = (LinearLayout) view.findViewById(R.id.faqStartMenu);

        faqMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaqListFragment fragment = new FaqListFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        campingsMenuButton = (LinearLayout) view.findViewById(R.id.campingsMenu);

        campingsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CampingsFragment fragment = new CampingsFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        resellersMenuButton = (LinearLayout) view.findViewById(R.id.resellersMenu);

        resellersMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DealersFragment fragment = new DealersFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        userSettingsMenuButton = (LinearLayout) view.findViewById(R.id.userSettingsMenuButton);

        userSettingsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettingsFragment fragment = new UserSettingsFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });



        scrollViewStart = (ScrollView) view.findViewById(R.id.scrollViewStart);
        OverScrollDecoratorHelper.setUpOverScroll(scrollViewStart);

        return view;
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle("");
    }

    public StartFragment(){}

}
