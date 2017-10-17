package se.netmine.hobby_marknad;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ImageView imageDealerHeart = null;
    private ImageView imageWorkshopHeart = null;

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

        imageDealerHeart = (ImageView) view.findViewById(R.id.imageDealerHeart);
        imageWorkshopHeart = (ImageView) view.findViewById(R.id.imageWorkshopHeart);

        imageDealerHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyHobbyMarket.getInstance().isUserLoggedIn())
                {
                    User currentUser = MyHobbyMarket.getInstance().currentUser;

                    if(currentUser.dealerId.equals(dealer.id) )
                    {
                        currentUser.dealerId = null;
                        currentUser.save();
                    }
                    else
                    {
                        currentUser.dealerId = dealer.id;
                        currentUser.save();
                    }

                    setUserDealerAndWorkshopIcon();
                }
                else
                {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                }
            }
        });

        if (dealer != null)
        {
            mainActivity.setTitle(dealer.name);
            dealerName.setText(dealer.name);
            dealerAddress.setText(dealer.street + ", " + dealer.postalcode + " " + dealer.city);
            dealerTel.setText(dealer.phone);
            dealerEmail.setText(dealer.email);
            dealerWebPage.setText(dealer.webpage);

            setUserDealerAndWorkshopIcon();

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

    public  void setUserDealerAndWorkshopIcon()
    {
        if(MyHobbyMarket.getInstance().isUserLoggedIn())
        {
            User currentUser = MyHobbyMarket.getInstance().currentUser;

            if(currentUser.dealerId != null && currentUser.dealerId.equals(dealer.id) )
            {
                Drawable d = getResources().getDrawable(R.drawable.ic_heart_full, mainActivity.getContext().getTheme());
                imageDealerHeart.setImageDrawable(d);
            }
            else
            {
                Drawable d = getResources().getDrawable(R.drawable.ic_heart_empty, mainActivity.getContext().getTheme());
                imageDealerHeart.setImageDrawable(d);
            }

            if(currentUser.workshopId != null && currentUser.workshopId.equals(dealer.id))
            {
                Drawable d = getResources().getDrawable(R.drawable.ic_heart_full, mainActivity.getContext().getTheme());
                imageWorkshopHeart.setImageDrawable(d);
            }
            else
            {
                Drawable d = getResources().getDrawable(R.drawable.ic_heart_empty, mainActivity.getContext().getTheme());
                imageWorkshopHeart.setImageDrawable(d);
            }
        }
    }

}


