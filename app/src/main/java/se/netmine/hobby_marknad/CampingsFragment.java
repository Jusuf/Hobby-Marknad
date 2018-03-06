package se.netmine.hobby_marknad;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static se.netmine.hobby_marknad.R.id.map;

/**
 * Created by jusuf on 2017-06-13.
 */

public class CampingsFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private IMainActivity mainActivity;
    View view;
    LayoutInflater inflater = null;
    private String imageBaseAddress = "http://scr.basetool.se/upload/";
    private GoogleMap mMap;
    private LinearLayout layoutMap;
    EditText txtSearchCamping = null;
    ListView listViewCampings = null;
    ExpandableHeightListView listViewGeneralFacilities = null;
    ExpandableHeightListView listViewActivityFacilities = null;
    ExpandableHeightListView listViewOtherFacilities = null;
    LinearLayout layoutCampingList = null;
    ScrollView scrollViewFacilityOptions = null;
    public ArrayList<Camping> loadedCampings = new ArrayList<>();
    public ArrayList<Facility> loadedGeneralFacilities = new  ArrayList<>();
    public ArrayList<Facility> loadedActivityFacilities = new  ArrayList<>();
    public ArrayList<Facility> loadedOtherFacilities = new  ArrayList<>();
    public ArrayList<String> filteredFacilities = new  ArrayList<>();
    ArrayAdapter<Camping> adapter;
    ArrayAdapter<Facility> generalFacilityAdapter;
    ArrayAdapter<Facility> activityFacilityAdapter;
    ArrayAdapter<Facility> otherFacilityAdapter;
    String language;
    String searchQuery;
    private Camping markedCamping = null;

    private Button btnMap = null;
    private Button btnList = null;

    private LinearLayout layoutShowCamping = null;
    private TextView txtShowCampingName = null;
    private TextView txtShowCampingAddress = null;
    private Button btnShow = null;

    public CampingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        view =  inflater.inflate(R.layout.fragment_campings, container, false);

        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(map);

        mapFragment.getMapAsync(this);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_campings));
        }

        language = Locale.getDefault().getCountry();

        loadCampings();


        listViewCampings = (ListView) view.findViewById(R.id.listViewCampings);
        adapter = new CampingListAdapter(mainActivity.getContext(), loadedCampings);
        listViewCampings.setAdapter(adapter);

        listViewCampings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Camping node = loadedCampings.get(pos);
                CampingFragment fragment = new CampingFragment();
                fragment.camping = node;
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        listViewGeneralFacilities = (ExpandableHeightListView) view.findViewById(R.id.lvGeneralFacilitiesItemList);
        generalFacilityAdapter = new GeneralFacilityListAdapter(mainActivity.getContext(), loadedGeneralFacilities);
        listViewGeneralFacilities.setAdapter(generalFacilityAdapter);
        listViewGeneralFacilities.setExpanded(true);

        listViewActivityFacilities = (ExpandableHeightListView) view.findViewById(R.id.lvActivityFacilitiesItemList);
        activityFacilityAdapter = new ActivityFacilityListAdapter(mainActivity.getContext(), loadedActivityFacilities);
        listViewActivityFacilities.setAdapter(activityFacilityAdapter);
        listViewActivityFacilities.setExpanded(true);

        listViewOtherFacilities = (ExpandableHeightListView) view.findViewById(R.id.lvOtherFacilitiesItemList);
        otherFacilityAdapter = new OtherFacilityListAdapter(mainActivity.getContext(), loadedOtherFacilities);
        listViewOtherFacilities.setAdapter(otherFacilityAdapter);
        listViewOtherFacilities.setExpanded(true);

        layoutMap = (LinearLayout) view.findViewById(R.id.mapLayout);
        layoutCampingList = (LinearLayout) view.findViewById(R.id.campingListLayout);
        layoutCampingList.setVisibility(View.GONE);

        btnMap = (Button) view.findViewById(R.id.btnCampingMap);
        btnList = (Button) view.findViewById(R.id.btnCampingList);

        scrollViewFacilityOptions = (ScrollView) view.findViewById(R.id.scrollViewFacilityOptions);

//        layoutCampingList.setVisibility(View.GONE);
//        layoutMap.setVisibility(View.GONE);
//        btnList.setVisibility(View.GONE);
//        btnMap.setVisibility(View.GONE);

        scrollViewFacilityOptions.setVisibility(View.GONE);

        txtSearchCamping = (EditText) view.findViewById(R.id.txtSearchCamping);
        txtSearchCamping.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    loadCampings();
                } else {
                    searchQuery = charSequence.toString();
                    searchCamping(searchQuery);
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
                layoutCampingList.setVisibility(View.VISIBLE);
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
                layoutCampingList.setVisibility(View.GONE);
                btnMap.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                btnMap.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                btnList.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                btnList.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));

            }
        });

        layoutShowCamping = (LinearLayout) view.findViewById(R.id.layoutShowCamping);
        layoutShowCamping.setVisibility(View.GONE);

        txtShowCampingName = (TextView) view.findViewById(R.id.txtShowCampingName);
        txtShowCampingAddress = (TextView) view.findViewById(R.id.txtShowCampingAddress);

        btnShow = (Button) view.findViewById(R.id.btnCampingShowCamping);
        btnShow.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CampingFragment fragment = new CampingFragment();
                fragment.camping = markedCamping;
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        return view;
    }

    private void loadCampings() {
        MyHobbyMarket.getInstance().getCampingList("", language);
    }

    public void searchCamping(String textToSearch) {
        MyHobbyMarket.getInstance().getCampingList(textToSearch, language);
    }

    public class CampingListAdapter extends ArrayAdapter<Camping> {

        public CampingListAdapter(Context context, ArrayList<Camping> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Camping item = getItem(position);

            convertView = inflater.inflate(R.layout.camping_item, null);

            final ImageView layoutCampingItem = (ImageView) convertView.findViewById(R.id.iv__camping_background);
            TextView txtCampingItemName = (TextView) convertView.findViewById(R.id.txtCampingItemName);
            TextView txtCampingItemCity = (TextView) convertView.findViewById(R.id.txtCampingItemCity);

            if(item.images.size() > 0){

                DownloadImage task = new DownloadImage(new AsyncResponse() {
                    @Override
                    public void processFinish(Drawable output) {

                        layoutCampingItem.setImageDrawable(output);
                        layoutCampingItem.setScaleType(ImageView.ScaleType.FIT_XY);

                    }
                });

                task.execute(new String[] {imageBaseAddress + item.images.get(0)});
            }

            txtCampingItemName.setText(item.name);
            txtCampingItemCity.setText(item.city);

            return convertView;
        }

    }

    public class GeneralFacilityListAdapter extends ArrayAdapter<Facility> {

        public GeneralFacilityListAdapter(Context context, ArrayList<Facility> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Facility item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);
            CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkboxFacility);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = listViewGeneralFacilities.getPositionForView(buttonView);

                    if(pos != ListView.INVALID_POSITION)
                    {
                        Facility facility = loadedGeneralFacilities.get(pos);
                        facility.setSelected(isChecked);

                        if(isChecked)
                        {
                            filteredFacilities.add(facility.getId());
                        }
                        else
                        {
                            filteredFacilities.remove(facility.getId());
                        }

                    }
                }
            });

            txtFacilityName.setText(item.getName());
            chkBox.setChecked(item.isSelected());

            return convertView;
        }

    }

    public class ActivityFacilityListAdapter extends ArrayAdapter<Facility> {

        public ActivityFacilityListAdapter(Context context, ArrayList<Facility> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Facility item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);
            CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkboxFacility);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = listViewActivityFacilities.getPositionForView(buttonView);

                    if(pos != ListView.INVALID_POSITION)
                    {
                        Facility facility = loadedActivityFacilities.get(pos);
                        facility.setSelected(isChecked);

                        if(isChecked)
                        {
                            filteredFacilities.add(facility.getId());
                        }
                        else
                        {
                            filteredFacilities.remove(facility.getId());
                        }

                    }
                }
            });

            txtFacilityName.setText(item.getName());
            chkBox.setChecked(item.isSelected());

            return convertView;
        }

    }

    public class OtherFacilityListAdapter extends ArrayAdapter<Facility> {

        public OtherFacilityListAdapter(Context context, ArrayList<Facility> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Facility item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);
            CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkboxFacility);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = listViewOtherFacilities.getPositionForView(buttonView);

                    if(pos != ListView.INVALID_POSITION)
                    {
                        Facility facility = loadedOtherFacilities.get(pos);
                        facility.setSelected(isChecked);

                        if(isChecked)
                        {
                            filteredFacilities.add(facility.getId());
                        }
                        else
                        {
                            filteredFacilities.remove(facility.getId());
                        }

                    }
                }
            });

            txtFacilityName.setText(item.getName());
            chkBox.setChecked(item.isSelected());

            return convertView;
        }

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        mMap.clear();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                layoutShowCamping.setVisibility(View.GONE);
            }
        });

        Double sumLat = 0.0;
        Double sumLng = 0.0;

        Double avgLat;
        Double avgLng;

        if(loadedCampings != null)
        {
            for (Camping camping : loadedCampings) {

                if(!empty( camping.lng ) || !empty( camping.lat ))
                {
                    LatLng marker = new LatLng(Double.parseDouble(camping.lng), Double.parseDouble(camping.lat));
                    mMap.addMarker(new MarkerOptions()
                            .title(camping.name)
                            .snippet(camping.city)
                            .position(marker));

                    sumLat += Double.parseDouble(camping.lng);
                    sumLng += Double.parseDouble(camping.lat);
                }

            }
            avgLat = sumLat / loadedCampings.size();
            avgLng = sumLng / loadedCampings.size();

            LatLng avgPosition = new LatLng(avgLat, avgLng);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(avgPosition, 5));
        }

    }

    @Override
    public void onCampingsUpdated(ArrayList<Camping> campings, CampingFacilityOptions loadedCampingFacilityOptions)
    {
        loadedCampings.clear();

        if (campings != null)
        {
            for (Camping camping : campings) {
                loadedCampings.add(camping);
            }

            for (Facility facility : loadedCampingFacilityOptions.generalFacilities) {
                loadedGeneralFacilities.add(facility);
            }
            for (Facility facility : loadedCampingFacilityOptions.activityFacilities) {
                loadedActivityFacilities.add(facility);
            }
            for (Facility facility : loadedCampingFacilityOptions.otherFacilities) {
                loadedOtherFacilities.add(facility);
            }

            if(mMap != null){
                MapFragment mapFrag = (MapFragment) getChildFragmentManager().findFragmentById(map);
                mapFrag.getMapAsync(this);
            }
        }
        if (generalFacilityAdapter != null) {
            generalFacilityAdapter.notifyDataSetChanged();
        }
        if (activityFacilityAdapter != null) {
            activityFacilityAdapter.notifyDataSetChanged();
        }
        if (otherFacilityAdapter != null) {
            otherFacilityAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String name = marker.getTitle();

        for (Camping camping : loadedCampings) {

            if (camping.name.equalsIgnoreCase(name))
            {
                markedCamping = camping;

                txtShowCampingName.setText(camping.name);
                txtShowCampingAddress.setText(camping.street + ", " + camping.city);
                layoutShowCamping.setVisibility(View.VISIBLE);
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

        return true;
    }

    public static boolean empty( final String s ) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    private class DownloadImage extends AsyncTask<String, Drawable, Drawable> {

        public AsyncResponse delegate = null;

        public DownloadImage(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Drawable doInBackground(String... urls) {
            Drawable img = LoadImageFromWebOperations(urls[0]);
            return img;
        }

        @Override
        protected void onProgressUpdate(Drawable[] result) {
            delegate.processFinish(result[0]);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            delegate.processFinish(result);
        }
    }

    public interface AsyncResponse {
        void processFinish(Drawable output);
    }



}


