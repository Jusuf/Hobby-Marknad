package se.netmine.hobby_marknad;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jusuf on 2017-06-13.
 */

public class CampingFragment extends BaseFragment implements OnMapReadyCallback {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    View view = null;

    ViewPager viewPagerCaravanimages = null;
    private GoogleMap mMap;
    public Camping camping = null;
    private String imageBaseAddress = "http://scr.basetool.se/upload/";
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Facility> generalFacilities = new ArrayList<>();
    private ArrayList<Facility> activityFacilities = new ArrayList<>();
    private ArrayList<Facility> otherFacilities = new ArrayList<>();
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;
    private LinearLayout sliderDotspanel = null;
    private TextView campingName = null;
    private LinearLayout campingDetailsCampingStarLayout = null;
    private LinearLayout linearLayoutCampingInfo = null;
    private TextView txtCampingReadInfo = null;

    private String openTimes = null;
    private String fullServiceTimes = null;

    LinearLayout linearLayoutCampingGeneralFacilities = null;
    TextView txtCampingGeneralFacilitiesCounter = null;

    LinearLayout linearLayoutCampingActivityFacilities = null;
    TextView txtCampingActivityFacilitiesCounter = null;

    LinearLayout linearLayoutCampingOtherFacilities = null;
    TextView txtCampingOtherFacilitiesCounter = null;

    LinearLayout linearLayoutCampingOfHousing = null;
    TextView txtCampingOfHousingCounter = null;

    LinearLayout linearLayoutCampingOpenHours = null;
    TextView txtCampingOpenHoursTimes = null;




    private TextView campingAddress = null;
    private TextView campingTel = null;
    private TextView campingEmail = null;
    private TextView campingWebPage = null;

    private ImageView imageCampingHeart = null;
    private ImageView imageWorkshopHeart = null;
    private ImagePagerAdapter pageAdapter = null;

    private Button btnCampingCall = null;
    private Button btnCampingSendEmail = null;
    private Button btnCampingVisitHompage = null;
    private Button btnCampingShowRoute = null;

    public CampingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_camping, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(camping.name);
        }

        viewPagerCaravanimages = (ViewPager) view.findViewById(R.id.viewPagerCampingImages);
        pageAdapter = new ImagePagerAdapter(mainActivity.getContext(), imageUrls);
        viewPagerCaravanimages.setAdapter(pageAdapter);


        viewPagerCaravanimages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(mainActivity.getContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(mainActivity.getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapCampingDetails);


        mapFragment.getMapAsync(this);

        campingName = (TextView) view.findViewById(R.id.txtCampingName);
        campingDetailsCampingStarLayout = (LinearLayout) view.findViewById(R.id.campingDetailsCampingStarLayout) ;
        linearLayoutCampingInfo = (LinearLayout) view.findViewById(R.id.linearLayoutCampingInfo);
        txtCampingReadInfo = (TextView) view.findViewById(R.id.txtCampingReadInfo);

        linearLayoutCampingGeneralFacilities = (LinearLayout) view.findViewById(R.id.linearLayoutCampingGeneralFacilities);
        txtCampingGeneralFacilitiesCounter = (TextView) view.findViewById(R.id.txtCampingGeneralFacilitiesCounter);

        linearLayoutCampingActivityFacilities = (LinearLayout) view.findViewById(R.id.linearLayoutCampingActivityFacilities);
        txtCampingActivityFacilitiesCounter = (TextView) view.findViewById(R.id.txtCampingActivityFacilitiesCounter);

        linearLayoutCampingOtherFacilities = (LinearLayout) view.findViewById(R.id.linearLayoutCampingOtherFacilities);
        txtCampingOtherFacilitiesCounter = (TextView) view.findViewById(R.id.txtCampingOtherFacilitiesCounter);

        linearLayoutCampingOfHousing = (LinearLayout) view.findViewById(R.id.linearLayoutCampingOfHousing);
        txtCampingOfHousingCounter = (TextView) view.findViewById(R.id.txtCampingOfHousingCounter);

        linearLayoutCampingOpenHours = (LinearLayout) view.findViewById(R.id.linearLayoutCampingOpenHours);
        txtCampingOpenHoursTimes = (TextView) view.findViewById(R.id.txtCampingOpenHoursTimes);


//        campingAddress = (TextView) view.findViewById(R.id.txtCampingAddress);
//        campingTel = (TextView) view.findViewById(R.id.txtCampingTel);
//        campingEmail = (TextView) view.findViewById(R.id.txtCampingEmail);
//        campingWebPage = (TextView) view.findViewById(R.id.txtCampingWebPage);

//        imageCampingHeart = (ImageView) view.findViewById(R.id.imageCampingHeart);
//        imageWorkshopHeart = (ImageView) view.findViewById(R.id.imageWorkshopHeart);
//
//        btnCampingCall = (Button) view.findViewById(R.id.btnCampingCall);
//        btnCampingSendEmail = (Button) view.findViewById(R.id.btnCampingSendEmail);
//        btnCampingVisitHompage = (Button) view.findViewById(R.id.btnCampingVisitHomepage);
//        btnCampingShowRoute = (Button) view.findViewById(R.id.btnCampingShowRoute);


        if (camping != null)
        {
            if(camping.facilities != null)
            {
                sortFacilities(camping.facilities);
                sortAccommodations(camping.accommodations);
            }

            mainActivity.setTitle(camping.name);
            campingName.setText(camping.name);
//            campingAddress.setText(camping.street + ", " + camping.postalcode + " " + camping.city);
//            campingTel.setText(camping.phone);
//            campingEmail.setText(camping.email);
//            campingWebPage.setText(camping.webpage);

            if(camping.images != null && camping.images.size() > 0)
            {
                for (String imageUrl: camping.images) {
                    imageUrls.add(imageBaseAddress + imageUrl);
                }
                pageAdapter.notifyDataSetChanged();
                createDotlayout();
            }

            if(!empty(camping.stars))
            {
                int numberOfStars = Integer.parseInt(camping.stars);
                for (int i = 0; i < numberOfStars; i++) {
                    ImageView imageview = new ImageView(mainActivity.getContext());
                    LinearLayout.LayoutParams params = new LinearLayout
                            .LayoutParams(160, 160);
                    imageview.setLayoutParams(params);
                    imageview.setImageResource(R.drawable.ic_white_star);
                    imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                    campingDetailsCampingStarLayout.addView(imageview);
                }
            }

            if(!empty(camping.descSV))
            {
                linearLayoutCampingInfo.setVisibility(View.VISIBLE);
                linearLayoutCampingInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CampingInfo campingInfo = new CampingInfo();
                        campingInfo.campingName = camping.name;
                        campingInfo.infoText = camping.descSV;

                        CampingInfoFragment fragment = new CampingInfoFragment();
                        fragment.campingInfo = campingInfo;

                        mainActivity.onNavigateToFragment(fragment);
                    }
                });
            }
            else
            {
                linearLayoutCampingInfo.setVisibility(View.GONE);
            }

            if(generalFacilities.size() > 0)
            {
                txtCampingGeneralFacilitiesCounter.setText(generalFacilities.size() + " st");

                linearLayoutCampingGeneralFacilities.setVisibility(View.VISIBLE);
                linearLayoutCampingGeneralFacilities.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FacilityListFragment fragment = new FacilityListFragment();
                        fragment.facilities = generalFacilities;
                        fragment.listName = getString(R.string.camping_general_facilities);

                        mainActivity.onNavigateToFragment(fragment);
                    }
                });
            }
            else
            {
                linearLayoutCampingGeneralFacilities.setVisibility(View.GONE);
            }

            if(activityFacilities.size() > 0)
            {
                txtCampingActivityFacilitiesCounter.setText(activityFacilities.size() + " st");

                linearLayoutCampingActivityFacilities.setVisibility(View.VISIBLE);
                linearLayoutCampingActivityFacilities.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FacilityListFragment fragment = new FacilityListFragment();
                        fragment.facilities = activityFacilities;
                        fragment.listName = getString(R.string.camping_activity_facilities);

                        mainActivity.onNavigateToFragment(fragment);
                    }
                });
            }
            else
            {
                linearLayoutCampingActivityFacilities.setVisibility(View.GONE);
            }

            if(otherFacilities.size() > 0)
            {
                txtCampingOtherFacilitiesCounter.setText(otherFacilities.size() + " st");

                linearLayoutCampingOtherFacilities.setVisibility(View.VISIBLE);
                linearLayoutCampingOtherFacilities.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FacilityListFragment fragment = new FacilityListFragment();
                        fragment.facilities = otherFacilities;
                        fragment.listName = getString(R.string.camping_other_facilities);

                        mainActivity.onNavigateToFragment(fragment);
                    }
                });
            }
            else
            {
                linearLayoutCampingOtherFacilities.setVisibility(View.GONE);
            }

            if(accommodations.size() > 0)
            {
                txtCampingOfHousingCounter.setText(accommodations.size() + " st");

                linearLayoutCampingOfHousing.setVisibility(View.VISIBLE);
                linearLayoutCampingOfHousing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AccommodationListFragment fragment = new AccommodationListFragment();
                        fragment.accommodations = accommodations;
                        fragment.listName = getString(R.string.camping_of_housing);

                        mainActivity.onNavigateToFragment(fragment);
                    }
                });
            }
            else
            {
                linearLayoutCampingOfHousing.setVisibility(View.GONE);
            }

            if(!empty(camping.fullServiceFrom) && !empty(camping.fullServiceTo))
            {
                fullServiceTimes = camping.fullServiceFrom + " - " + camping.fullServiceTo;
            }

            if(!empty(camping.openFrom) && !empty(camping.openTo))
            {
                openTimes = camping.openFrom + " - " + camping.openTo;

                txtCampingOpenHoursTimes.setText(openTimes);
                linearLayoutCampingOpenHours.setVisibility(View.VISIBLE);

                if(!empty(openTimes) || !empty(fullServiceTimes))
                {
                    linearLayoutCampingOpenHours.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            OpenedTimesFragment fragment = new OpenedTimesFragment();
                            fragment.listName =  getString(R.string.camping_opening_hours_title);
                            fragment.openTimes = openTimes;
                            fragment.fullServiceTimes = fullServiceTimes;

                            mainActivity.onNavigateToFragment(fragment);
                        }
                    });
                }

            }
            else
                {
                    linearLayoutCampingOpenHours.setVisibility(View.GONE);
                }




        }

//        btnCampingCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
////                intent.setData(Uri.parse("tel:" + camping.phone));
//                startActivity(intent);
//            }
//        });
//
//        btnCampingSendEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("plain/text");
////                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { camping.email });
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                intent.putExtra(Intent.EXTRA_TEXT, "");
//                startActivity(Intent.createChooser(intent, ""));
//            }
//        });

//        btnCampingVisitHompage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = camping.webpage;

//                Uri webpage = Uri.parse(url);
//
//                if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                    webpage = Uri.parse("http://" + url);
//                }
//
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                browserIntent.setData(webpage);
//                startActivity(browserIntent);
//            }
//        });

//        btnCampingShowRoute.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + camping.lat  + ">,<" + camping.lng + ">?q=<" + camping.lat  + ">,<" + camping.lng + ">(" + camping.name + ")"));
//                intent.setPackage("com.google.android.apps.maps");
//                startActivity(intent);
//            }
//        });

        return view;
    }



    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        map.clear();

        if(camping != null)
        {
            LatLng marker = new LatLng(Double.parseDouble(camping.lng), Double.parseDouble(camping.lat));

            map.addMarker(new MarkerOptions()
                     .title(camping.name)
                     .snippet(camping.city)
                     .position(marker));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
        }
    }

    private void createDotlayout(){
        sliderDotspanel = (LinearLayout) view.findViewById(R.id.sliderDots);

        dotscount = pageAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(mainActivity.getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(mainActivity.getContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        if (dotscount > 0)
        {
            dots[0].setImageDrawable(ContextCompat.getDrawable(mainActivity.getContext(), R.drawable.active_dot));
        }
        else
        {
            viewPagerCaravanimages.setVisibility(View.GONE);
            sliderDotspanel.setVisibility(View.GONE);
        }

    }

    public static boolean empty( final String s ) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    private void sortFacilities(ArrayList<Facility> facilities)
    {
        for (Facility facility: facilities) {
            if(facility.facilityCategoryName.equals("Allmänna faciliteter"))
            {
                generalFacilities.add(facility);
            }
            if(facility.facilityCategoryName.equals("Aktivitetsfaciliteter"))
            {
                activityFacilities.add(facility);
            }
            if(facility.facilityCategoryName.equals("Övriga faciliteter"))
            {
                otherFacilities.add(facility);
            }
        }
    }

    private void sortAccommodations(ArrayList<Accommodation> campingAccommodations)
    {
        for (Accommodation accommodation: campingAccommodations) {
                accommodations.add(accommodation);
        }
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(camping.name);
    }


}


