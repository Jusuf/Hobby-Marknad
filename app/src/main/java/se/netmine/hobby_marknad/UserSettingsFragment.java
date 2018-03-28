package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by jusuf on 2017-08-14.
 */

public class UserSettingsFragment extends BaseFragment {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;

    Button btnMySettings = null;
    Button btnMyMessages = null;
    TextView textUserName = null;

    ScrollView scrollViewUserSettings = null;
    ScrollView scrollViewMyMessages = null;

    LinearLayout layoutChooseDealer = null;
    TextView txtDealerServiceDealerName = null;

    LinearLayout layoutChooseWorkshop = null;
    TextView txtDealerServiceWorkShopName = null;

    Switch switchNewsAndOffers = null;
    Switch switchServiceReminder = null;

    ArrayList<UserMessage> loadedMessages = new ArrayList<>();
    ExpandableHeightListView lvMessages = null;
    ArrayAdapter<UserMessage> messageAdapter;

    TextView txtNoMessages = null;

    Button btnLogin = null;
    Button btnLogout = null;
//    Button btnUpdateCampings = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        this.inflater = inflater;

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.my_page));
        }

        scrollViewUserSettings = (ScrollView) view.findViewById(R.id.scrollViewUserSettings);
        scrollViewMyMessages = (ScrollView) view.findViewById(R.id.scrollViewMyMessages);

        btnMySettings = (Button) view.findViewById(R.id.btnMySettings);

        btnMySettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scrollViewMyMessages.setVisibility(View.GONE);
                scrollViewUserSettings.setVisibility(View.VISIBLE);
                btnMySettings.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                btnMySettings.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                btnMyMessages.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                btnMyMessages.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
            }
        });

        btnMyMessages = (Button) view.findViewById(R.id.btnMyMessages);

        btnMyMessages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (MyHobbyMarket.getInstance().isUserLoggedIn()) {
                    scrollViewMyMessages.setVisibility(View.VISIBLE);
                    scrollViewUserSettings.setVisibility(View.GONE);
                    btnMyMessages.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));
                    btnMyMessages.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.white));
                    btnMySettings.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.whiteTransparent));
                    btnMySettings.setTextColor(ContextCompat.getColorStateList(mainActivity.getContext(), R.color.myhobby_blue));


                    MyHobbyMarket.getInstance().getMessageList();
                } else {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                }

            }
        });

        textUserName = (TextView) view.findViewById(R.id.textUserName);
        if (MyHobbyMarket.getInstance().isUserLoggedIn()) {
            if(MyHobbyMarket.getInstance().getFirstName() != null && MyHobbyMarket.getInstance().getLastName() != null)
            {
                String fullName = MyHobbyMarket.getInstance().getFirstName() + " " +MyHobbyMarket.getInstance().getLastName();
                textUserName.setText(fullName);
            }
            else
            {
                textUserName.setText(MyHobbyMarket.getInstance().getEmail());
            }

        }

        layoutChooseDealer = (LinearLayout) view.findViewById(R.id.layoutChooseDealer);
        layoutChooseDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyHobbyMarket.getInstance().currentUser.dealerId != null && MyHobbyMarket.getInstance().currentUser.dealerName != null) {
                    MyHobbyMarket.getInstance().getDealer(MyHobbyMarket.getInstance().currentUser.dealerId);
                } else {
                    DealersFragment fragment = new DealersFragment();
                    mainActivity.onNavigateToFragment(fragment);
                }

            }
        });

        layoutChooseWorkshop = (LinearLayout) view.findViewById(R.id.layoutChooseWorkshop);
        layoutChooseWorkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyHobbyMarket.getInstance().currentUser.workshopId != null && MyHobbyMarket.getInstance().currentUser.workshopName != null) {
                    MyHobbyMarket.getInstance().getDealer(MyHobbyMarket.getInstance().currentUser.workshopId);
                } else {
                    DealersFragment fragment = new DealersFragment();
                    mainActivity.onNavigateToFragment(fragment);
                }
            }
        });

        switchNewsAndOffers = (Switch) view.findViewById(R.id.switchNewsAndOffers);
        switchNewsAndOffers.setChecked(MyHobbyMarket.getInstance().currentUser.subNews);
        switchNewsAndOffers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (MyHobbyMarket.getInstance().isUserLoggedIn()) {
                    MyHobbyMarket.getInstance().currentUser.subNews = isChecked;
                    MyHobbyMarket.getInstance().currentUser.save();
                    MyHobbyMarket.getInstance().sync(true);
                    if(isChecked)
                    {
                        mainActivity.showToast(getString(R.string.dealer_offers_on));
                    }
                    else
                    {
                        mainActivity.showToast(getString(R.string.dealer_offers_off));
                    }

                } else {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                    switchNewsAndOffers.setChecked(false);
                }

            }
        });

        switchServiceReminder = (Switch) view.findViewById(R.id.switchServiceReminder);
        switchServiceReminder.setChecked(MyHobbyMarket.getInstance().currentUser.subService);
        switchServiceReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (MyHobbyMarket.getInstance().isUserLoggedIn()) {
                    MyHobbyMarket.getInstance().currentUser.subService = isChecked;
                    MyHobbyMarket.getInstance().currentUser.save();
                    MyHobbyMarket.getInstance().sync(true);

                } else {
                    mainActivity.showToast(getString(R.string.must_be_logged_in));
                    switchServiceReminder.setChecked(false);
                }

            }
        });

        txtDealerServiceDealerName = (TextView) view.findViewById(R.id.txtDealerServiceDealerName);
        txtDealerServiceWorkShopName = (TextView) view.findViewById(R.id.txtDealerServiceWorkShopName);


        txtNoMessages = (TextView) view.findViewById(R.id.txtNoMessages);

        lvMessages = (ExpandableHeightListView) view.findViewById(R.id.lvMessages);
        messageAdapter = new MessageListAdapter(mainActivity.getContext(), loadedMessages);
        lvMessages.setAdapter(messageAdapter);
        lvMessages.setExpanded(true);

        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserMessage node = loadedMessages.get(position);
                UserMessageFragment fragment = new UserMessageFragment();
                fragment.message = node;
                mainActivity.onNavigateToFragment(fragment);
            }
        });

//        btnUpdateCampings  = (Button) view.findViewById(R.id.btnUpdateCampings);
//        btnUpdateCampings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyHobbyMarket.getInstance().getLatestCampingList(Locale.getDefault().getCountry());
//            }
//        });

        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        if(MyHobbyMarket.getInstance().isUserLoggedIn())
        {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyHobbyMarket.getInstance().logout();
                }
            });
        }
        else
        {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginFragment fragment = new LoginFragment();
                    mainActivity.onNavigateToFragment(fragment);
                }
            });
        }

        return view;
    }


    public void setDealerAndWorkshopText() {
        if (MyHobbyMarket.getInstance().currentUser.dealerId != null && MyHobbyMarket.getInstance().currentUser.dealerName != null) {
            String dealerName = MyHobbyMarket.getInstance().getDealerName();
            txtDealerServiceDealerName.setText(dealerName);
        } else {
            txtDealerServiceDealerName.setText(getString(R.string.my_dealer_not_chosen));
        }

        if (MyHobbyMarket.getInstance().currentUser.workshopId != null && MyHobbyMarket.getInstance().currentUser.workshopName != null) {
            String workshopName = MyHobbyMarket.getInstance().getWorkshopName();
            txtDealerServiceWorkShopName.setText(workshopName);
        } else {
            txtDealerServiceWorkShopName.setText(getString(R.string.my_workshop_not_chosen));
        }
    }

    public class MessageListAdapter extends ArrayAdapter<UserMessage> {

        public MessageListAdapter(Context context, ArrayList<UserMessage> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            UserMessage item = getItem(position);

            convertView = inflater.inflate(R.layout.message_item, null);

            if(!item.isRead)
            {
                convertView.setBackgroundResource(R.drawable.bg_message_new);
            }

            TextView txtMessageTitle = (TextView) convertView.findViewById(R.id.txtMessageTitle);
            TextView txtMessageSender = (TextView) convertView.findViewById(R.id.txtMessageSender);

            txtMessageTitle.setText(item.title);
            txtMessageSender.setText(item.sender.name);

            return convertView;
        }

    }

    @Override
    public void onMessagesUpdated(UserMessage[] messages) {
        loadedMessages.clear();

        if (messages != null) {
            for (UserMessage message : messages) {
                loadedMessages.add(message);
            }

            messageAdapter.notifyDataSetChanged();
            if(loadedMessages.size() == 0)
            {
                txtNoMessages.setVisibility(View.VISIBLE);
                lvMessages.setVisibility(View.GONE);
            }
            else
            {
                txtNoMessages.setVisibility(View.GONE);
                lvMessages.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onResume() {
        // After a pause
        super.onResume();
        mainActivity.setTitle(getString(R.string.my_page));
        setDealerAndWorkshopText();

        if(scrollViewMyMessages.getVisibility() == View.VISIBLE)
        {
            MyHobbyMarket.getInstance().getMessageList();
        }
    }

    public UserSettingsFragment() {
    }

}
