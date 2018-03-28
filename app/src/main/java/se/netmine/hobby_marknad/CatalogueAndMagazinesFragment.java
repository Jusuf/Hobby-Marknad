package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;

/**
 * Created by jusuf on 2017-08-15.
 */

public class CatalogueAndMagazinesFragment extends BaseFragment{
    private IMainActivity mainActivity;

    private LinearLayout catalogueMenuButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalogue_and_magazines, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.start_catalog_and_magazines));
        }

        catalogueMenuButton =  (LinearLayout) view.findViewById(R.id.catalogueMenuButton);

        catalogueMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogueFragment fragment = new CatalogueFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        return view;
    }

    public CatalogueAndMagazinesFragment(){}
}
