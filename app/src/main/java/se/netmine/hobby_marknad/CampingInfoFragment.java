package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-06-13.
 */

public class CampingInfoFragment extends BaseFragment{

    private IMainActivity mainActivity;
    public CampingInfo campingInfo = null;

    private TextView txtCampingName = null;
    private TextView txtCampingInfo = null;

    public CampingInfoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_camping_info, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.camping_read_about_camping));
        }

        txtCampingName = (TextView) view.findViewById(R.id.txtCampingName);
        txtCampingInfo = (TextView) view.findViewById(R.id.txtCampingInfo);

        if (campingInfo != null)
        {
            txtCampingName.setText(campingInfo.campingName);
            txtCampingInfo.setText(campingInfo.infoText);
        }

        return view;
    }

}


