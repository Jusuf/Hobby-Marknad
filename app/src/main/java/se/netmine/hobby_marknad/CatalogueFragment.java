package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

/**
 * Created by jusuf on 2017-08-15.
 */

public class CatalogueFragment extends BaseFragment{
    private IMainActivity mainActivity;
    private PDFView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalogue, container, false);


        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.catalogue));
        }

        pdfView = (PDFView) view.findViewById(R.id.catalogueView);
        pdfView.fromAsset("Hobby_Katalog_2018.pdf").load();

        return view;
    }

    public CatalogueFragment(){}
}
