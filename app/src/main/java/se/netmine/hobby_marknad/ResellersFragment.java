package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jusuf on 2017-06-13.
 */

public class ResellersFragment extends BaseFragment implements OnMapReadyCallback  {

    private IMainActivity mainActivity;
    private GoogleMap mMap;
    private MapView mapView;

    private Button btnMap = null;
    private Button btnList = null;
    private FragmentActivity myContext;
    private SupportMapFragment mMapFragment = null;

    public ResellersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_resellers, container, false);

        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);



        mapFragment.getMapAsync(this);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_resellers));
        }

        btnMap = (Button) view.findViewById(R.id.btnResellerMap);
        btnList = (Button) view.findViewById(R.id.btnResellerList);

//        mapView = (MapView) view.findViewById(R.id.mapView);

//        if (faq != null)
//        {
//            faqQuestion.setText(faq.question);
//            faqAnswer.setText(faq.answer);
//        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

//        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }

}


