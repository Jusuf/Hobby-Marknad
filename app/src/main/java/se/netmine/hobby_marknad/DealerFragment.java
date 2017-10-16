package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-06-13.
 */

public class DealerFragment extends BaseFragment{

    private IMainActivity mainActivity;
    public Dealer dealer = null;

    private TextView dealerName = null;

    private TextView dealerAddress = null;
    private TextView dealerTel = null;
    private TextView dealerEmail = null;
    private TextView dealerWebPage = null;

    public DealerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_dealer, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.reseller));
        }

        dealerName = (TextView) view.findViewById(R.id.txtDealerName);
        dealerAddress = (TextView) view.findViewById(R.id.txtDealerAddress);
        dealerTel = (TextView) view.findViewById(R.id.txtDealerTel);
        dealerEmail = (TextView) view.findViewById(R.id.txtDealerEmail);
        dealerWebPage = (TextView) view.findViewById(R.id.txtDealerWebPage);

        if (dealer != null)
        {
            mainActivity.setTitle(dealer.name);

            dealerName.setText(dealer.name);
            dealerAddress.setText(dealer.street + ", " + dealer.postalcode + " " + dealer.city);
            dealerTel.setText(dealer.phone);
            dealerEmail.setText(dealer.email);
            dealerWebPage.setText(dealer.webpage);

        }

        return view;
    }

}


