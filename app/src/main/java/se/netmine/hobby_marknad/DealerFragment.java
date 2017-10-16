package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jusuf on 2017-06-13.
 */

public class DealerFragment extends BaseFragment implements OnMapReadyCallback {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    private GoogleMap mMap;
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

        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapDealerDetails);

        mapFragment.getMapAsync(this);

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

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        map.clear();

        if(dealer != null)
        {
            LatLng marker = new LatLng(Double.parseDouble(dealer.lat), Double.parseDouble(dealer.lng));

            map.addMarker(new MarkerOptions()
                     .title(dealer.name)
                     .snippet(dealer.city)
                     .position(marker));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
        }
    }

}


