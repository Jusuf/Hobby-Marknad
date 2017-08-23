package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-08-14.
 */

public class StartFragment extends BaseFragment {

    private IMainActivity mainActivity;

    private LinearLayout catalogueAndMagazinesMenuButton = null;
    private LinearLayout faqMenuButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);


        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("Hobby");
        }

        catalogueAndMagazinesMenuButton =  view.findViewById(R.id.catalogueAndMagazinesMenu);

        catalogueAndMagazinesMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogueAndMagazinesFragment fragment = new CatalogueAndMagazinesFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        faqMenuButton =  view.findViewById(R.id.faqStartMenu);

        faqMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaqListFragment fragment = new FaqListFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        return view;
    }

    public StartFragment(){}

}
