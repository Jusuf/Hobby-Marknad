package se.netmine.hobby_marknad;

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
    private LinearLayout serviceBookMenuButton = null;
    private LinearLayout catalogueAndMagazinesMenuButton = null;
    private LinearLayout faqMenuButton = null;
    private LinearLayout resellersMenuButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("Hobby");
        }

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

        resellersMenuButton = (LinearLayout) view.findViewById(R.id.resellersMenu);

        resellersMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DealersFragment fragment = new DealersFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        scrollViewStart = (ScrollView) view.findViewById(R.id.scrollViewStart);
        OverScrollDecoratorHelper.setUpOverScroll(scrollViewStart);

        return view;
    }

    public StartFragment(){}

}
