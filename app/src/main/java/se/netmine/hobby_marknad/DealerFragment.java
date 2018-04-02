package se.netmine.hobby_marknad;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
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

import java.util.Locale;

/**
 * Created by jusuf on 2017-06-13.
 */

public class DealerFragment extends BaseFragment implements OnMapReadyCallback {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    LinearLayout dealerDetailsMapLayout = null;
    private GoogleMap mMap;
    public Dealer dealer = null;

    private TextView dealerName = null;
    private TextView dealerAddress = null;
    private TextView dealerTel = null;
    private TextView dealerEmail = null;

    private TextView txtDealerServiceWorkShop = null;

    private ImageView imageDealerHeart = null;
    private ImageView imageWorkshopHeart = null;

    private Button btnDealerCall = null;
    private Button btnDealerSendEmail = null;
    private Button btnDealerVisitHompage = null;
    private Button btnDealerShowRoute = null;

    public DealerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dealer, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.reseller));
        }

        final MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapDealerDetails);

        dealerDetailsMapLayout = (LinearLayout) view.findViewById(R.id.dealerDetailsMapLayout);

        mapFragment.getMapAsync(this);

        dealerName = (TextView) view.findViewById(R.id.txtDealerName);
        dealerAddress = (TextView) view.findViewById(R.id.txtDealerAddress);
        dealerTel = (TextView) view.findViewById(R.id.txtDealerTel);
        dealerEmail = (TextView) view.findViewById(R.id.txtDealerEmail);

        imageDealerHeart = (ImageView) view.findViewById(R.id.imageDealerHeart);
        imageWorkshopHeart = (ImageView) view.findViewById(R.id.imageWorkshopHeart);

        btnDealerCall = (Button) view.findViewById(R.id.btnDealerCall);
        btnDealerSendEmail = (Button) view.findViewById(R.id.btnDealerSendEmail);
        btnDealerVisitHompage = (Button) view.findViewById(R.id.btnDealerVisitHomepage);
        btnDealerShowRoute = (Button) view.findViewById(R.id.btnDealerShowRoute);

        txtDealerServiceWorkShop = (TextView) view.findViewById(R.id.txtDealerServiceWorkShop);

        imageDealerHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyHobbyMarket.getInstance().isUserLoggedIn()) {
                    if (MyHobbyMarket.getInstance().currentUser.dealerId != null && MyHobbyMarket.getInstance().currentUser.dealerId.equals(dealer.id)) {
                        MyHobbyMarket.getInstance().currentUser.dealerId = null;
                        MyHobbyMarket.getInstance().currentUser.dealerName = null;
                    } else {
                        MyHobbyMarket.getInstance().currentUser.dealerId = dealer.id;
                        MyHobbyMarket.getInstance().currentUser.dealerName = dealer.name;
                    }
                    MyHobbyMarket.getInstance().currentUser.save();
                    MyHobbyMarket.getInstance().sync(true);
                    setUserDealerAndWorkshopIcon();
                } else {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                }
            }
        });

        imageWorkshopHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyHobbyMarket.getInstance().isUserLoggedIn()) {
                    if (MyHobbyMarket.getInstance().currentUser.workshopId != null && MyHobbyMarket.getInstance().currentUser.workshopId.equals(dealer.id)) {
                        MyHobbyMarket.getInstance().currentUser.workshopId = null;
                        MyHobbyMarket.getInstance().currentUser.workshopName = null;
                    } else {
                        MyHobbyMarket.getInstance().currentUser.workshopId = dealer.id;
                        MyHobbyMarket.getInstance().currentUser.workshopName = dealer.name;
                    }
                    MyHobbyMarket.getInstance().currentUser.save();
                    MyHobbyMarket.getInstance().sync(true);
                    setUserDealerAndWorkshopIcon();
                } else {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                }
            }
        });

        if (dealer != null) {
            mainActivity.setTitle(dealer.name);
            dealerName.setText(dealer.name);
            dealerAddress.setText(dealer.street + ", " + dealer.postalcode + " " + dealer.city);
            dealerTel.setText(dealer.phone);
            dealerEmail.setText(dealer.email);

            if(!dealer.isWorkShop)
            {
                txtDealerServiceWorkShop.setText(getString(R.string.no_workshop));
                txtDealerServiceWorkShop.setTextColor(getResources().getColor(R.color.light_grey));
                imageWorkshopHeart.setVisibility(View.GONE);
            }

            if(empty(dealer.lat) || empty(dealer.lng))
            {
                dealerDetailsMapLayout.setVisibility(View.GONE);
            }

            setUserDealerAndWorkshopIcon();
        }

        btnDealerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dealer.phone));
                startActivity(intent);
            }
        });

        btnDealerSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{dealer.email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        btnDealerVisitHompage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = dealer.webpage;

                Uri webpage = Uri.parse(url);

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    webpage = Uri.parse("http://" + url);
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(webpage);
                startActivity(browserIntent);
            }
        });

        btnDealerShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + dealer.lat + ">,<" + dealer.lng + ">?q=<" + dealer.lat + ">,<" + dealer.lng + ">(" + dealer.name + ")"));
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

        if (dealer != null && !empty(dealer.lat) && !empty(dealer.lng))
        {
            LatLng marker = new LatLng(Double.parseDouble(dealer.lat), Double.parseDouble(dealer.lng));

            map.addMarker(new MarkerOptions()
                    .title(dealer.name)
                    .snippet(dealer.city)
                    .position(marker));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
        }
    }

    public void setUserDealerAndWorkshopIcon() {
        User currentUser = MyHobbyMarket.getInstance().currentUser;

        if (currentUser.dealerId != null && currentUser.dealerId.equals(dealer.id)) {
            Drawable d = getResources().getDrawable(R.drawable.ic_heart_full, mainActivity.getContext().getTheme());
            imageDealerHeart.setImageDrawable(d);
        } else {
            Drawable d = getResources().getDrawable(R.drawable.ic_heart_empty, mainActivity.getContext().getTheme());
            imageDealerHeart.setImageDrawable(d);
        }

        if (currentUser.workshopId != null && currentUser.workshopId.equals(dealer.id)) {
            Drawable d = getResources().getDrawable(R.drawable.ic_heart_full, mainActivity.getContext().getTheme());
            imageWorkshopHeart.setImageDrawable(d);
        } else {
            Drawable d = getResources().getDrawable(R.drawable.ic_heart_empty, mainActivity.getContext().getTheme());
            imageWorkshopHeart.setImageDrawable(d);
        }
    }

    public static boolean empty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

}


