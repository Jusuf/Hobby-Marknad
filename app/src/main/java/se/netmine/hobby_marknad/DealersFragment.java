package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jusuf on 2017-06-13.
 */

public class DealersFragment extends BaseFragment implements OnMapReadyCallback  {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    private GoogleMap mMap;
    private LinearLayout layoutMap;
    EditText txtSearchDealer = null;
    ListView listViewDealers = null;
    LinearLayout layoutDealerList = null;
    public ArrayList<Dealer> loadedDealers = new  ArrayList<Dealer>();
    ArrayAdapter<Dealer> adapter;
    String language;
    String searchQuery;

    private Button btnMap = null;
    private Button btnList = null;

    public DealersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View view =  inflater.inflate(R.layout.fragment_dealers, container, false);

        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_resellers));
        }

        language = Locale.getDefault().getCountry();

        loadDealers();

        adapter = new DealerListAdapter(mainActivity.getContext(), loadedDealers);
        listViewDealers = (ListView) view.findViewById(R.id.listViewDealers);
        listViewDealers.setAdapter(adapter);

        layoutMap = (LinearLayout) view.findViewById(R.id.mapLayout);
        layoutDealerList = (LinearLayout) view.findViewById(R.id.dealerListLayout);

        layoutDealerList.setVisibility(View.GONE);

        btnMap = (Button) view.findViewById(R.id.btnDealerMap);
        btnList = (Button) view.findViewById(R.id.btnDealerList);

        btnList.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                layoutMap.setVisibility(View.GONE);
                layoutDealerList.setVisibility(View.VISIBLE);
                btnList.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                btnList.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                btnMap.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                btnMap.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
            }
        });

        btnMap.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                layoutMap.setVisibility(View.VISIBLE);
                layoutDealerList.setVisibility(View.GONE);
                btnMap.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                btnMap.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                btnList.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                btnList.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));

            }
        });

        return view;
    }

    private void loadDealers() {
        MyHobbyMarket.getInstance().getDealerList("", language);
    }

    public class DealerListAdapter extends ArrayAdapter<Dealer> {

        public DealerListAdapter(Context context, ArrayList<Dealer> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Dealer item = getItem(position);

            convertView = inflater.inflate(R.layout.dealer_item, null);

            TextView txtDealerItemName = (TextView) convertView.findViewById(R.id.txtDealerItemName);
            TextView txtDealerItemCity = (TextView) convertView.findViewById(R.id.txtDealerItemCity);

            txtDealerItemName.setText(item.name);
            txtDealerItemCity.setText(item.city);

            return convertView;
        }

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

    @Override
    public void onDealersUpdated(Dealer[] dealers)
    {
        loadedDealers.clear();

        if (dealers != null)
        {
            for (Dealer dealer : dealers) {
                loadedDealers.add(dealer);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}


