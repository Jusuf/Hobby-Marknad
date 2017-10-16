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

        if (dealer != null)
        {
            dealerName.setText(dealer.name);
        }

        return view;
    }

}


