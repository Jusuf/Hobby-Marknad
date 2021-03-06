package se.netmine.hobby_marknad;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.DatePicker;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.orm.util.NamingHelper;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

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
    LinearLayout linearLayoutCampingsWrapper = null;
    LinearLayout layoutCampingList = null;
    ScrollView scrollViewFacilityOptions = null;
    ImageView imageSearchFilter = null;
    public ArrayList<Camping> loadedCampings = new ArrayList<>();
    public ArrayList<Camping> filteredCampings = new ArrayList<>();
    public ArrayList<FacilityOption> loadedGeneralFacilities = new ArrayList<>();
    public ArrayList<FacilityOption> loadedActivityFacilities = new ArrayList<>();
    public ArrayList<FacilityOption> loadedOtherFacilities = new ArrayList<>();
    public ArrayList<FacilityOption> filteredFacilities = new ArrayList<>();
    ArrayAdapter<Camping> adapter;
    ArrayAdapter<FacilityOption> generalFacilityAdapter;
    ArrayAdapter<FacilityOption> activityFacilityAdapter;
    ArrayAdapter<FacilityOption> otherFacilityAdapter;
    String language;
    String searchQuery = null;
    private Camping markedCamping = null;

    private Button btnMap = null;
    private Button btnList = null;

    private LinearLayout layoutShowCamping = null;
    private LinearLayout layoutShowCampingResults = null;
    private TextView txtShowCampingName = null;
    private TextView txtShowCampingAddress = null;
    private Button btnShow = null;
    private Button btnCampingShowCampingResults = null;

    private LinearLayout layoutFromDate = null;
    private TextView txtFromDate = null;
    private LinearLayout layoutToDate = null;
    private TextView txtToDate = null;

    private Calendar mCurrentDate;
    private int year, month, day;
    private String chosenFromDate = null;
    private String chosenToDate = null;
    MapFragment mapFragment = null;
    Handler uiHandler = null;
    Runnable runnable = null;
    Marker oldMarker = null;

    LinearLayout layoutCampingOneStar = null;
    LinearLayout layoutCampingTwoStars = null;
    LinearLayout layoutCampingThreeStars = null;
    LinearLayout layoutCampingFourStars = null;
    LinearLayout layoutCampingFiveStars = null;

    CheckBox checkboxOneStar = null;
    CheckBox checkboxTwoStars = null;
    CheckBox checkboxThreeStars = null;
    CheckBox checkboxFourStars = null;
    CheckBox checkboxFiveStars = null;

    private boolean oneStar = false;
    private boolean twoStars = false;
    private boolean threeStars = false;
    private boolean fourStars = false;
    private boolean fiveStars = false;

    public CampingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_campings, container, false);

        loadCampings();

        mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(map);

        mapFragment.getMapAsync(this);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_campings));
        }

        uiHandler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                refreshMarkers();
            }

        };

        language = Locale.getDefault().getCountry();


        listViewCampings = (ListView) view.findViewById(R.id.listViewCampings);
        adapter = new CampingListAdapter(mainActivity.getContext(), filteredCampings);
        listViewCampings.setAdapter(adapter);

        listViewCampings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Camping node = filteredCampings.get(pos);
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

        linearLayoutCampingsWrapper = (LinearLayout) view.findViewById(R.id.linearLayoutCampingsWrapper);

        layoutShowCampingResults = (LinearLayout) view.findViewById(R.id.layoutShowCampingResults);
        layoutShowCampingResults.setVisibility(View.GONE);

        imageSearchFilter = (ImageView) view.findViewById(R.id.imageSearchFilter);

        imageSearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollViewFacilityOptions.getVisibility() == View.VISIBLE) {
                    scrollViewFacilityOptions.setVisibility(View.GONE);
                    layoutShowCampingResults.setVisibility(View.GONE);
                    linearLayoutCampingsWrapper.setVisibility(View.VISIBLE);

                    layoutShowCamping.setVisibility(View.GONE);
                } else {
                    scrollViewFacilityOptions.setVisibility(View.VISIBLE);
                    layoutShowCampingResults.setVisibility(View.VISIBLE);
                    linearLayoutCampingsWrapper.setVisibility(View.GONE);

                    layoutShowCamping.setVisibility(View.GONE);
                    btnCampingShowCampingResults.setText("Visa " + filteredCampings.size() + " träffar");
                }

            }
        });

        scrollViewFacilityOptions.setVisibility(View.GONE);

        txtSearchCamping = (EditText) view.findViewById(R.id.txtSearchCamping);
        txtSearchCamping.addTextChangedListener(new TextWatcher() {

            final android.os.Handler handler = new android.os.Handler();
            Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                layoutShowCamping.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    searchQuery = null;
                } else {
                    searchQuery = charSequence.toString().toLowerCase();
                }

                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        //do some work with s.toString()

                        new FilterCampings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                };
                handler.postDelayed(runnable, 1000);
            }

        });

        txtSearchCamping.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                layoutShowCamping.setVisibility(View.GONE);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                layoutMap.setVisibility(View.GONE);
                layoutCampingList.setVisibility(View.VISIBLE);
                btnList.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                btnList.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                btnMap.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                btnMap.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));

                layoutShowCamping.setVisibility(View.GONE);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                layoutMap.setVisibility(View.VISIBLE);
                layoutCampingList.setVisibility(View.GONE);
                btnMap.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                btnMap.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                btnList.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                btnList.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));

                layoutShowCamping.setVisibility(View.GONE);
            }
        });

        layoutShowCamping = (LinearLayout) view.findViewById(R.id.layoutShowCamping);
        layoutShowCamping.setVisibility(View.GONE);

        txtShowCampingName = (TextView) view.findViewById(R.id.txtShowCampingName);
        txtShowCampingAddress = (TextView) view.findViewById(R.id.txtShowCampingAddress);

        btnShow = (Button) view.findViewById(R.id.btnCampingShowCamping);
        btnShow.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
                CampingFragment fragment = new CampingFragment();
                fragment.camping = markedCamping;
                mainActivity.onNavigateToFragment(fragment);
                layoutShowCamping.setVisibility(View.GONE);
            }
        });

        btnCampingShowCampingResults = (Button) view.findViewById(R.id.btnCampingShowCampingResults);
        btnCampingShowCampingResults.setText("Visa " + filteredCampings.size() + " träffar");
        btnCampingShowCampingResults.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                scrollViewFacilityOptions.setVisibility(View.GONE);
                linearLayoutCampingsWrapper.setVisibility(View.VISIBLE);
                layoutShowCampingResults.setVisibility(View.GONE);
            }
        });

        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        layoutCampingOneStar = (LinearLayout) view.findViewById(R.id.layoutCampingOneStar);
        addStarsToFilterByGrade(1, layoutCampingOneStar);
        layoutCampingTwoStars = (LinearLayout) view.findViewById(R.id.layoutCampingTwoStars);
        addStarsToFilterByGrade(2, layoutCampingTwoStars);
        layoutCampingThreeStars = (LinearLayout) view.findViewById(R.id.layoutCampingThreeStars);
        addStarsToFilterByGrade(3, layoutCampingThreeStars);
        layoutCampingFourStars = (LinearLayout) view.findViewById(R.id.layoutCampingFourStars);
        addStarsToFilterByGrade(4, layoutCampingFourStars);
        layoutCampingFiveStars = (LinearLayout) view.findViewById(R.id.layoutCampingFiveStars);
        addStarsToFilterByGrade(5, layoutCampingFiveStars);

        checkboxOneStar = (CheckBox) view.findViewById(R.id.checkboxOneStar);
        checkboxOneStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                oneStar = isChecked;
                new FilterCampings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        checkboxTwoStars = (CheckBox) view.findViewById(R.id.checkboxTwoStars);
        checkboxTwoStars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                twoStars = isChecked;
                new FilterCampings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        checkboxThreeStars = (CheckBox) view.findViewById(R.id.checkboxThreeStars);
        checkboxThreeStars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                threeStars = isChecked;
                new FilterCampings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        checkboxFourStars = (CheckBox) view.findViewById(R.id.checkboxFourStars);
        checkboxFourStars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fourStars = isChecked;
                new FilterCampings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        checkboxFiveStars = (CheckBox) view.findViewById(R.id.checkboxFiveStars);
        checkboxFiveStars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fiveStars = isChecked;
                new FilterCampings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        layoutFromDate = (LinearLayout) view.findViewById(R.id.layoutFromDate);
        txtFromDate = (TextView) view.findViewById(R.id.txtFromDate);
        layoutFromDate.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String date = formatter.format(newDate.getTime());

                        txtFromDate.setText(date);
                        chosenFromDate = date;

                        FilterCampings filterCampings = new FilterCampings();
                        filterCampings.execute();
                    }
                }, year, month, day);

                datePickerDialog.setButton(android.content.DialogInterface.BUTTON_NEGATIVE,
                        getString(R.string.camping_clear_date), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chosenFromDate = null;
                                txtFromDate.setText(getString(R.string.camping_choose_date));

                                FilterCampings filterCampings = new FilterCampings();
                                filterCampings.execute();
                            }
                        });

                datePickerDialog.show();
            }
        });

        layoutToDate = (LinearLayout) view.findViewById(R.id.layoutToDate);
        txtToDate = (TextView) view.findViewById(R.id.txtToDate);

        layoutToDate.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String date = formatter.format(newDate.getTime());

                        chosenToDate = date;
                        txtToDate.setText(date);

                        FilterCampings filterCampings = new FilterCampings();
                        filterCampings.execute();
                    }
                }, year, month, day);

                datePickerDialog.setButton(android.content.DialogInterface.BUTTON_NEGATIVE,
                        getString(R.string.camping_clear_date), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chosenToDate = null;
                                txtToDate.setText(getString(R.string.camping_choose_date));

                                FilterCampings filterCampings = new FilterCampings();
                                filterCampings.execute();
                            }
                        });

                datePickerDialog.show();
            }
        });

        return view;
    }

    private void loadCampings() {
        MyHobbyMarket.getInstance().getCampingsCount(language);
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
            LinearLayout campingDetailsCampingStarLayout = (LinearLayout) convertView.findViewById(R.id.campingDetailsCampingStarLayout);

            if (item.images != null && item.images.size() > 0) {

                DownloadImage task = new DownloadImage(new AsyncResponse() {
                    @Override
                    public void processFinish(Drawable output) {

                        layoutCampingItem.setImageDrawable(output);
                        layoutCampingItem.setScaleType(ImageView.ScaleType.FIT_XY);

                    }
                });

                task.execute(new String[]{imageBaseAddress + item.images.get(0)});
            }

            if (!empty(item.stars)) {
                int numberOfStars = Integer.parseInt(item.stars);
                for (int i = 0; i < numberOfStars; i++) {
                    ImageView imageview = new ImageView(mainActivity.getContext());
                    LinearLayout.LayoutParams params = new LinearLayout
                            .LayoutParams(100, 100);
                    imageview.setLayoutParams(params);
                    imageview.setImageResource(R.drawable.ic_orange_star);
                    imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                    campingDetailsCampingStarLayout.addView(imageview);
                }
            }

            txtCampingItemName.setText(item.name);
            txtCampingItemCity.setText(item.city);

            return convertView;
        }

    }

    public class GeneralFacilityListAdapter extends ArrayAdapter<FacilityOption> {

        public GeneralFacilityListAdapter(Context context, ArrayList<FacilityOption> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FacilityOption item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_select_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);
            CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkboxFacility);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = listViewGeneralFacilities.getPositionForView(buttonView);

                    if (pos != ListView.INVALID_POSITION) {
                        FacilityOption facility = loadedGeneralFacilities.get(pos);
                        facility.setSelected(isChecked);

                        if (isChecked) {
                            filteredFacilities.add(facility);
                        } else {
                            filteredFacilities.remove(facility);
                        }

                        FilterCampings filterCampings = new FilterCampings();
                        filterCampings.execute();
                    }
                }
            });

            txtFacilityName.setText(item.name);
            chkBox.setChecked(item.isSelected);

            return convertView;
        }

    }

    public class ActivityFacilityListAdapter extends ArrayAdapter<FacilityOption> {

        public ActivityFacilityListAdapter(Context context, ArrayList<FacilityOption> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FacilityOption item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_select_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);
            CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkboxFacility);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = listViewActivityFacilities.getPositionForView(buttonView);

                    if (pos != ListView.INVALID_POSITION) {
                        FacilityOption facility = loadedActivityFacilities.get(pos);
                        facility.setSelected(isChecked);

                        if (isChecked) {
                            filteredFacilities.add(facility);
                        } else {
                            filteredFacilities.remove(facility);
                        }

//                        filterCampings();
                        FilterCampings filterCampings = new FilterCampings();
                        filterCampings.execute();
                    }
                }
            });

            txtFacilityName.setText(item.name);
            chkBox.setChecked(item.isSelected);

            return convertView;
        }

    }

    public class OtherFacilityListAdapter extends ArrayAdapter<FacilityOption> {

        public OtherFacilityListAdapter(Context context, ArrayList<FacilityOption> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FacilityOption item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_select_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);
            CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkboxFacility);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = listViewOtherFacilities.getPositionForView(buttonView);

                    if (pos != ListView.INVALID_POSITION) {
                        FacilityOption facility = loadedOtherFacilities.get(pos);
                        facility.setSelected(isChecked);

                        if (isChecked) {
                            filteredFacilities.add(facility);
                        } else {
                            filteredFacilities.remove(facility);
                        }

                        // filterCampings();
                        FilterCampings filterCampings = new FilterCampings();
                        filterCampings.execute();
                    }
                }
            });

            txtFacilityName.setText(item.name);
            chkBox.setChecked(item.isSelected);

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
        refreshMarkers();
    }

    @Override
    public void onCampingsUpdated(ArrayList<Camping> campings, ArrayList<FacilityOption> loadedCampingFacilityOptions) {
        String facilityCategoryGeneral = "Allmänna faciliteter";
        String facilityCategoryActivity = "Aktivitetsfaciliteter";
        String facilityCategoryOther = "Övriga faciliteter";

        loadedCampings.clear();

        if (campings != null) {
            for (Camping camping : campings) {
                loadedCampings.add(camping);
            }

        }

        if (loadedCampingFacilityOptions != null) {

            for (FacilityOption facilityOption : loadedCampingFacilityOptions) {
                if (facilityOption.facilityCategoryName.equals(facilityCategoryGeneral)) {
                    loadedGeneralFacilities.add(facilityOption);
                }
                if (facilityOption.facilityCategoryName.equals(facilityCategoryActivity)) {
                    loadedActivityFacilities.add(facilityOption);
                }
                if (facilityOption.facilityCategoryName.equals(facilityCategoryOther)) {
                    loadedOtherFacilities.add(facilityOption);
                }
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

        filteredCampings.addAll(loadedCampings);

        if (mMap != null) {
            uiHandler.post(runnable);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        int defaultWidth = 150;
        int defaultHeight = 150;

        BitmapDrawable bitmapDefault = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_map_marker);
        Bitmap bDefault = bitmapDefault.getBitmap();
        Bitmap defaultMarker = Bitmap.createScaledBitmap(bDefault, defaultWidth, defaultHeight, false);

        if (oldMarker != null) {
            oldMarker.setIcon(BitmapDescriptorFactory.fromBitmap(defaultMarker));
        }

        int chosenWidth = 180;
        int chosenHeight = 180;

        BitmapDrawable bitmapChosen = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_map_marker_choosen);
        Bitmap bChosen = bitmapChosen.getBitmap();
        Bitmap choosenMarker = Bitmap.createScaledBitmap(bChosen, chosenWidth, chosenHeight, false);

        String name = marker.getTitle();
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(choosenMarker));

        for (Camping camping : filteredCampings) {

            if (camping.name.equalsIgnoreCase(name)) {
                markedCamping = camping;

                txtShowCampingName.setText(camping.name);
                txtShowCampingAddress.setText(camping.street + ", " + camping.city);
                layoutShowCamping.setVisibility(View.VISIBLE);
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

        oldMarker = marker;

        return true;
    }

    public static boolean empty(final String s) {
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

    public boolean containsFacility(ArrayList<Facility> campingFacilities, FacilityOption filteredFacility) {
        for (Facility f : campingFacilities) {

            if (f != null && f.facilityId.equals(filteredFacility.facilityId)) {
                return true;
            }
        }
        return false;
    }

    public final class ArticleFilter
            implements Predicate<Camping> {
        private final Pattern pattern;

        public ArticleFilter(final String regex) {
            pattern = Pattern.compile(regex.trim().toLowerCase());
        }

        @Override
        public boolean apply(final Camping input) {
            return pattern.matcher(input.name.trim().toLowerCase()).find() ||
                    pattern.matcher(input.city.trim().toLowerCase()).find() ||
                    pattern.matcher(input.street.trim().toLowerCase()).find() ||
                    pattern.matcher(input.postalcode.trim().toLowerCase()).find();
        }
    }

    public final class GradeFilter
            implements Predicate<Camping> {
        private final Pattern patternOne;
        private final Pattern patternTwo;
        private final Pattern patternThree;
        private final Pattern patternFour;
        private final Pattern patternFive;

        public GradeFilter(boolean oneStar, boolean twoStars, boolean threeStars, boolean fourStars, boolean fiveStars) {
            if (oneStar) {
                patternOne = Pattern.compile("1");
            } else {
                patternOne = Pattern.compile("z");
            }
            if (twoStars) {
                patternTwo = Pattern.compile("2");
            } else {
                patternTwo = Pattern.compile("z");
            }
            if (threeStars) {
                patternThree = Pattern.compile("3");
            } else {
                patternThree = Pattern.compile("z");
            }
            if (fourStars) {
                patternFour = Pattern.compile("4");
            } else {
                patternFour = Pattern.compile("z");
            }
            if (fiveStars) {
                patternFive = Pattern.compile("5");
            } else {
                patternFive = Pattern.compile("z");
            }
        }

        @Override
        public boolean apply(final Camping input) {

            return patternOne.matcher(input.stars).find() ||
                    patternTwo.matcher(input.stars).find() ||
                    patternThree.matcher(input.stars).find()||
                    patternFour.matcher(input.stars).find() ||
                    patternFive.matcher(input.stars).find();
        }
    }

    @Override
    public void onResume() {
        // After a pause
        super.onResume();
        mainActivity.setTitle(getString(R.string.nav_campings));

    }

    public void refreshMarkers() {
        mMap.clear();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                layoutShowCamping.setVisibility(View.GONE);
            }
        });

        Double sumLat = 0.0;
        Double sumLng = 0.0;

        Double avgLat;
        Double avgLng;

        if (filteredCampings != null) {
            for (Camping camping : filteredCampings) {

                if (!empty(camping.lng) || !empty(camping.lat)) {
                    int height = 150;
                    int width = 150;
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_map_marker);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                    LatLng marker = new LatLng(Double.parseDouble(camping.lng), Double.parseDouble(camping.lat));
                    mMap.addMarker(new MarkerOptions()
                            .title(camping.name)
                            .snippet(camping.city)
                            .position(marker).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                    sumLat += Double.parseDouble(camping.lng);
                    sumLng += Double.parseDouble(camping.lat);
                }

            }
            avgLat = sumLat / filteredCampings.size();
            avgLng = sumLng / filteredCampings.size();

            LatLng avgPosition = new LatLng(avgLat, avgLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(avgPosition, 7));
        }
    }

    private void addStarsToFilterByGrade(int numberOfStars, LinearLayout gradeLayout) {
        for (int i = 0; i < numberOfStars; i++) {
            ImageView imageview = new ImageView(mainActivity.getContext());
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(120, 120);
            imageview.setLayoutParams(params);
            imageview.setImageResource(R.drawable.ic_orange_star);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);

            gradeLayout.addView(imageview);
        }
    }

    private class FilterCampings extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.filter);

        @Override
        protected void onPreExecute() {
            if (loadingMessage != null) {
                pDialog = new ProgressDialog(mainActivity.getContext());
                pDialog.setMessage(loadingMessage);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            String campingIdAsSQL = NamingHelper.toSQLNameDefault("campingId") + "=?";

            boolean filterByGrade = false;

            if (oneStar || twoStars || threeStars || fourStars || fiveStars) {
                filterByGrade = true;
            }

            ArrayList<Camping> filteredCampingsByGrade = new ArrayList<>();
            ArrayList<Camping> filteredCampingsByDate = new ArrayList<>();
            ArrayList<Camping> filteredCampingsBySearchQuery = new ArrayList<>();


            filteredCampings.clear();

            if (filteredFacilities.size() == 0 && chosenFromDate == null && chosenToDate == null || filterByGrade) {
                filteredCampings.clear();
            }

            if (chosenFromDate != null && chosenToDate == null) {
                List<Camping> result = Camping.find(Camping.class, "OPEN_FROM <= ?", chosenFromDate.toString());
                filteredCampingsByDate.clear();
                filteredCampingsByDate.addAll(result);
            }

            if (chosenFromDate == null && chosenToDate != null) {
                List<Camping> result = Camping.find(Camping.class, "OPEN_TO = ? or OPEN_TO >= ?", chosenToDate.toString(), chosenToDate.toString());
                filteredCampingsByDate.clear();
                filteredCampingsByDate.addAll(result);
            }

            if (chosenFromDate != null && chosenToDate != null) {
                List<Camping> result = Camping.find(Camping.class, "OPEN_FROM <= ? and OPEN_TO >= ?", chosenFromDate.toString(), chosenToDate.toString());
                filteredCampingsByDate.clear();
                filteredCampingsByDate.addAll(result);
            }

            if (chosenFromDate == null && chosenToDate == null) {
                List<Camping> result = Camping.listAll(Camping.class);
                filteredCampingsByDate.clear();
                filteredCampingsByDate.addAll(result);
            }

            if (oneStar || twoStars || threeStars || fourStars || fiveStars) {
                ArrayList<Camping> filteredList
                        = Lists.newArrayList(Collections2.filter(filteredCampingsByDate,
                        new GradeFilter(oneStar, twoStars, threeStars, fourStars, fiveStars)));

                filteredCampingsByGrade.addAll(filteredList);
            } else {
                filteredCampingsByGrade.addAll(filteredCampingsByDate);
            }

            for (Camping camping : filteredCampingsByGrade) {
                List<CampingImage> campingImages = CampingImage.find(CampingImage.class, campingIdAsSQL, camping.campingId);
                camping.images = new ArrayList<>();

                Collection<String> imageUrls = Collections2.transform(
                        campingImages,
                        new Function<CampingImage, String>() {
                            @Override
                            public String apply(CampingImage entity) {
                                return entity.fileName;
                            }
                        }
                );

                camping.images.addAll(imageUrls);

                List<Facility> campingFacilities = Facility.find(Facility.class, campingIdAsSQL, camping.campingId);
                camping.facilities = new ArrayList<>();
                camping.facilities.addAll(campingFacilities);
            }

            if (searchQuery != null) {
                ArrayList<Camping> filteredList
                        = Lists.newArrayList(Collections2.filter(filteredCampingsByGrade,
                        new ArticleFilter(searchQuery)));

                filteredCampingsBySearchQuery.clear();

                filteredCampingsBySearchQuery.addAll(filteredList);
            } else {
                filteredCampingsBySearchQuery.addAll(filteredCampingsByGrade);
            }

            for (Camping camping : filteredCampingsBySearchQuery) {
                boolean foundFacilityById = true;

                for (FacilityOption filteredFacility : filteredFacilities) {
                    if (containsFacility(camping.facilities, filteredFacility)) {
                        foundFacilityById = true;
                    } else {
                        foundFacilityById = false;
                        break;
                    }
                }

                if (foundFacilityById) {
                    filteredCampings.add(camping);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {

            oldMarker = null;
            adapter.notifyDataSetChanged();
            btnCampingShowCampingResults.setText("Visa " + filteredCampings.size() + " träffar");

            uiHandler.post(runnable);

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

}


