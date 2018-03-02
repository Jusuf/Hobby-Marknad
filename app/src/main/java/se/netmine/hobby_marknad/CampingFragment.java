package se.netmine.hobby_marknad;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class CampingFragment extends BaseFragment implements OnMapReadyCallback {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    private GoogleMap mMap;
    public CampingMin camping = null;

    private TextView campingName = null;
    private TextView campingAddress = null;
    private TextView campingTel = null;
    private TextView campingEmail = null;
    private TextView campingWebPage = null;

    private ImageView imageCampingHeart = null;
    private ImageView imageWorkshopHeart = null;

    private Button btnCampingCall = null;
    private Button btnCampingSendEmail = null;
    private Button btnCampingVisitHompage = null;
    private Button btnCampingShowRoute = null;

    public CampingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_camping, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.reseller));
        }

        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapCampingDetails);

        mapFragment.getMapAsync(this);

        campingName = (TextView) view.findViewById(R.id.txtCampingName);
        campingAddress = (TextView) view.findViewById(R.id.txtCampingAddress);
        campingTel = (TextView) view.findViewById(R.id.txtCampingTel);
        campingEmail = (TextView) view.findViewById(R.id.txtCampingEmail);
        campingWebPage = (TextView) view.findViewById(R.id.txtCampingWebPage);

        imageCampingHeart = (ImageView) view.findViewById(R.id.imageCampingHeart);
        imageWorkshopHeart = (ImageView) view.findViewById(R.id.imageWorkshopHeart);

        btnCampingCall = (Button) view.findViewById(R.id.btnCampingCall);
        btnCampingSendEmail = (Button) view.findViewById(R.id.btnCampingSendEmail);
        btnCampingVisitHompage = (Button) view.findViewById(R.id.btnCampingVisitHomepage);
        btnCampingShowRoute = (Button) view.findViewById(R.id.btnCampingShowRoute);


        if (camping != null)
        {
            mainActivity.setTitle(camping.name);
            campingName.setText(camping.name);
            campingAddress.setText(camping.street + ", " + camping.postalcode + " " + camping.city);
//            campingTel.setText(camping.phone);
//            campingEmail.setText(camping.email);
//            campingWebPage.setText(camping.webpage);
        }

        btnCampingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + camping.phone));
                startActivity(intent);
            }
        });

        btnCampingSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { camping.email });
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        btnCampingVisitHompage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        btnCampingShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + camping.lat  + ">,<" + camping.lng + ">?q=<" + camping.lat  + ">,<" + camping.lng + ">(" + camping.name + ")"));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

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

}


