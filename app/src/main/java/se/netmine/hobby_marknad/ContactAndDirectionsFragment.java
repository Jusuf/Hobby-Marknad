package se.netmine.hobby_marknad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static se.netmine.hobby_marknad.R.id.map;

/**
 * Created by jusuf on 2017-08-16.
 */

public class ContactAndDirectionsFragment extends BaseFragment implements OnMapReadyCallback {

    private IMainActivity mainActivity;

    LayoutInflater inflater = null;

    public String fragmentName = null;

    public String address = "";
    public String phoneNumber = null;
    public String eMail = null;
    public String webPage = null;

    public String lat = null;
    public String lng = null;
    public String campingName = "";

    private LinearLayout linearLayoutAddress = null;
    private TextView txtAddress = null;

    private LinearLayout linearLayoutPhone = null;
    private TextView txtPhone = null;

    private LinearLayout linearLayoutEmail = null;
    private TextView txtEmail = null;

    Button btnCampingCall = null;
    Button btnCampingSendEmail = null;
    Button btnCampingVisitHompage = null;
    Button btnCampingShowRoute = null;

    private GoogleMap mMap;

    public ContactAndDirectionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.fragment_contact_and_directions, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(fragmentName);
        }

        linearLayoutAddress = (LinearLayout) view.findViewById(R.id.linearLayoutAddress);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress) ;
        linearLayoutPhone = (LinearLayout) view.findViewById(R.id.linearLayoutPhone);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        linearLayoutEmail = (LinearLayout) view.findViewById(R.id.linearLayoutEmail);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);

        btnCampingCall = (Button) view.findViewById(R.id.btnCampingCall);
        btnCampingSendEmail = (Button) view.findViewById(R.id.btnCampingSendEmail);
        btnCampingVisitHompage = (Button) view.findViewById(R.id.btnCampingVisitHomepage);
        btnCampingShowRoute = (Button) view.findViewById(R.id.btnCampingShowRoute);

        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapCampingDetails);


        mapFragment.getMapAsync(this);

        if(!empty(address))
        {
            txtAddress.setText(address);
            linearLayoutAddress.setVisibility(View.VISIBLE);

            if(!empty(lat) && !empty(lng) && !empty(campingName))
            {
                btnCampingShowRoute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + lat  + ">,<" + lng + ">?q=<" + lat  + ">,<" + lng + ">(" + campingName + ")"));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                });
            }

            btnCampingShowRoute.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayoutAddress.setVisibility(View.GONE);
            btnCampingShowRoute.setVisibility(View.GONE);
        }

        if(!empty(phoneNumber))
        {
            txtPhone.setText(phoneNumber);
            linearLayoutPhone.setVisibility(View.VISIBLE);

            btnCampingCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
            });

            btnCampingCall.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayoutPhone.setVisibility(View.GONE);
            btnCampingCall.setVisibility(View.GONE);
        }

        if(!empty(eMail))
        {
            txtEmail.setText(eMail);
            linearLayoutEmail.setVisibility(View.VISIBLE);

            btnCampingSendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { eMail });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(intent, ""));
                }
            });
        }
        else
        {
            linearLayoutEmail.setVisibility(View.GONE);
            btnCampingSendEmail.setVisibility(View.GONE);
        }

        if (!empty(webPage)){

            btnCampingVisitHompage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = webPage;

                    Uri webpage = Uri.parse(url);

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        webpage = Uri.parse("http://" + url);
                    }

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(webpage);
                    startActivity(browserIntent);
                }
            });
        }
        else
        {
            btnCampingVisitHompage.setVisibility(View.GONE);
        }

        if(mMap != null){
            MapFragment mapFrag = (MapFragment) getChildFragmentManager().findFragmentById(map);
            mapFrag.getMapAsync(this);
        }

        return view;
    }



    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(fragmentName);
    }

    public static boolean empty( final String s ) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        map.clear();

        if(!empty(lat) && !empty(lng))
        {
            LatLng marker = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

            map.addMarker(new MarkerOptions()
                    .title(campingName)
                    .snippet(address)
                    .position(marker));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
        }
    }


}
