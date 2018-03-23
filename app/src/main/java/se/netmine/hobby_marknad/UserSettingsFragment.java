package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


/**
 * Created by jusuf on 2017-08-14.
 */

public class UserSettingsFragment extends BaseFragment {

    private IMainActivity mainActivity;

    TextView textUserName = null;

    LinearLayout layoutChooseDealer = null;
    TextView txtDealerServiceDealerName = null;

    LinearLayout layoutChooseWorkshop = null;
    TextView txtDealerServiceWorkShopName = null;

    Switch switchNewsAndOffers = null;
    Switch switchServiceReminder = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.my_page));
        }

        textUserName = (TextView) view.findViewById(R.id.textUserName);
        if(MyHobbyMarket.getInstance().isUserLoggedIn())
        {
            textUserName.setText(MyHobbyMarket.getInstance().currentUser.email);
        }

        layoutChooseDealer = (LinearLayout) view.findViewById(R.id.layoutChooseDealer);
        layoutChooseDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(MyHobbyMarket.getInstance().currentUser.dealerId != null && MyHobbyMarket.getInstance().currentUser.dealerName != null)
                    {
                        MyHobbyMarket.getInstance().getDealer(MyHobbyMarket.getInstance().currentUser.dealerId);
                    }
                    else
                    {
                        DealersFragment fragment = new DealersFragment();
                        mainActivity.onNavigateToFragment(fragment);
                    }

            }
        });

        layoutChooseWorkshop = (LinearLayout) view.findViewById(R.id.layoutChooseWorkshop);
        layoutChooseWorkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(MyHobbyMarket.getInstance().currentUser.workshopId != null && MyHobbyMarket.getInstance().currentUser.workshopName != null)
                    {
                        MyHobbyMarket.getInstance().getDealer(MyHobbyMarket.getInstance().currentUser.workshopId);
                    }
                    else
                    {
                        DealersFragment fragment = new DealersFragment();
                        mainActivity.onNavigateToFragment(fragment);
                    }
                }
        });

        switchNewsAndOffers = (Switch) view.findViewById(R.id.switchNewsAndOffers);
        switchNewsAndOffers.setChecked(MyHobbyMarket.getInstance().currentUser.notifyNews);
        switchNewsAndOffers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MyHobbyMarket.getInstance().isUserLoggedIn())
                {
                MyHobbyMarket.getInstance().currentUser.notifyNews = isChecked;
                MyHobbyMarket.getInstance().currentUser.save();
                MyHobbyMarket.getInstance().sync(true);
                }
                else
                {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                    switchNewsAndOffers.setChecked(false);
                }

            }
        });

        switchServiceReminder = (Switch) view.findViewById(R.id.switchServiceReminder);
        switchServiceReminder.setChecked(MyHobbyMarket.getInstance().currentUser.notifyService);
        switchServiceReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MyHobbyMarket.getInstance().isUserLoggedIn())
                {
                    MyHobbyMarket.getInstance().currentUser.notifyService = isChecked;
                    MyHobbyMarket.getInstance().currentUser.save();
                    MyHobbyMarket.getInstance().sync(true);
                }
                else
                {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                    switchServiceReminder.setChecked(false);
                }

            }
        });

        txtDealerServiceDealerName = (TextView) view.findViewById(R.id.txtDealerServiceDealerName);
        txtDealerServiceWorkShopName = (TextView) view.findViewById(R.id.txtDealerServiceWorkShopName);

        return view;
    }

    public void setDealerAndWorkshopText()
    {
        if (MyHobbyMarket.getInstance().currentUser.dealerId != null && MyHobbyMarket.getInstance().currentUser.dealerName != null)
        {
            String dealerName = MyHobbyMarket.getInstance().getDealerName();
            txtDealerServiceDealerName.setText(dealerName);
        }
        else
        {
            txtDealerServiceDealerName.setText(getString(R.string.my_dealer_not_chosen));
        }

        if (MyHobbyMarket.getInstance().currentUser.workshopId != null && MyHobbyMarket.getInstance().currentUser.workshopName != null)
        {
            String workshopName = MyHobbyMarket.getInstance().getWorkshopName();
            txtDealerServiceWorkShopName.setText(workshopName);
        }
        else
        {
            txtDealerServiceWorkShopName.setText(getString(R.string.my_workshop_not_chosen));
        }
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(getString(R.string.my_page));
        setDealerAndWorkshopText();
    }

    public UserSettingsFragment(){}

}
