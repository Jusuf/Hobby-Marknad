package se.netmine.hobby_marknad;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        listViewDealers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Dealer node = loadedDealers.get(pos);
                DealerFragment fragment = new DealerFragment();
                fragment.dealer = node;
                mainActivity.onNavigateToFragment(fragment);
            }
        });


        layoutMap = (LinearLayout) view.findViewById(R.id.mapLayout);
        layoutDealerList = (LinearLayout) view.findViewById(R.id.dealerListLayout);

        layoutDealerList.setVisibility(View.GONE);

        btnMap = (Button) view.findViewById(R.id.btnDealerMap);
        btnList = (Button) view.findViewById(R.id.btnDealerList);

        txtSearchDealer = (EditText) view.findViewById(R.id.txtSearchDealer);
        txtSearchDealer.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    loadDealers();
                } else {
                    searchQuery = charSequence.toString();
                    searchDealer(searchQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

    public void searchDealer(String textToSearch) {
        MyHobbyMarket.getInstance().getDealerList(textToSearch, language);
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

        mMap = map;
        map.clear();

        Double sumLat = 0.0;
        Double sumLng = 0.0;

        Double avgLat;
        Double avgLng;

        if(loadedDealers != null)
        {
            for (Dealer dealer : loadedDealers) {
                LatLng marker = new LatLng(Double.parseDouble(dealer.lat), Double.parseDouble(dealer.lng));

                map.addMarker(new MarkerOptions()
                        .title(dealer.name)
                        .snippet(dealer.city)
                        .position(marker));

                sumLat += Double.parseDouble(dealer.lat);
                sumLng += Double.parseDouble(dealer.lng);

            }
            avgLat = sumLat / loadedDealers.size();
            avgLng = sumLng / loadedDealers.size();

            LatLng avgPosition = new LatLng(avgLat, avgLng);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(avgPosition, 5));
        }
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

            if(mMap != null){
                MapFragment mapFrag = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                mapFrag.getMapAsync(this);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}

