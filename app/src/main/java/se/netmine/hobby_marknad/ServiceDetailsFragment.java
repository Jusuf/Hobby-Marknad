package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;

/**
 * Created by jusuf on 2017-06-13.
 */

public class ServiceDetailsFragment extends BaseFragment{

    private IMainActivity mainActivity;
    public Service service = null;

    private TextView txtResponsibleName;
    private TextView txtStatus;
    private TextView txtDoneDate;

    public ServiceDetailsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_service_details, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_faq));
        }

        txtResponsibleName = (TextView) view.findViewById(R.id.txtResponsibleName);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtDoneDate = (TextView) view.findViewById(R.id.txtDoneDate);

        if (service != null)
        {
            if(service.type.equals("10"))
            {
                mainActivity.setTitle(getResources().getString(R.string.seal_test));
            }
            else
            {
                mainActivity.setTitle(getResources().getString(R.string.warranty));
            }

            txtResponsibleName.setText(service.dealerName);

            if (service.passed == true) {
                txtStatus.setText(getResources().getString(R.string.approved));
            }
            else
            {
                txtStatus.setText(getResources().getString(R.string.not_approved));
            }

            String dateString;
            try {
                dateString = mainActivity.formatDate(service.serviceDate);
            } catch (ParseException e) {
                e.printStackTrace();
                dateString = "";
            }
            txtDoneDate.setText(dateString);
        }

        return view;
    }

}


