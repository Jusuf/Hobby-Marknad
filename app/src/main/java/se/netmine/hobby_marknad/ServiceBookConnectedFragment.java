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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by jusuf on 2017-08-14.
 */

public class ServiceBookConnectedFragment extends BaseFragment {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    public Caravan caravan;
    ExpandableHeightListView  listViewServiceItems = null;
    ArrayAdapter<Service> adapter;

    private ScrollView scrollViewServiceDemo;
    private TextView textModelName;
    private TextView textModelYearName;
    private TextView textVehicleIdentificationNumber;
    private TextView textInTraficDate;
    private TextView textGuaranteeDate;

    private Button btnFindReseller = null;
    private Button btnLogoutService = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_service_book_connected, container, false);


        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.service_book));
        }

        //createDemoData();

        adapter = new ServiceItemListAdapter(mainActivity.getContext(), caravan.serviceEntries);
        listViewServiceItems = (ExpandableHeightListView) view.findViewById(R.id.lvServiceItemList);
        listViewServiceItems.setAdapter(adapter);
        listViewServiceItems.setExpanded(true);

        textModelName = (TextView) view.findViewById(R.id.textModelName);
        textModelName.setText(caravan.serieName);

        textModelYearName = (TextView) view.findViewById(R.id.textModelYearName);
        textModelYearName.setText(caravan.year);

        textVehicleIdentificationNumber = (TextView) view.findViewById(R.id.textVehicleIdentificationNumber);
        textVehicleIdentificationNumber.setText(caravan.chassisNo);

        textInTraficDate = (TextView) view.findViewById(R.id.textInTraficDate);
        textInTraficDate.setText(caravan.trafficDate);

        textGuaranteeDate = (TextView) view.findViewById(R.id.textGuaranteeDate);
        textGuaranteeDate.setText(caravan.warrantyDate);

        scrollViewServiceDemo = (ScrollView) view.findViewById(R.id.scrollViewServiceDemo);
        OverScrollDecoratorHelper.setUpOverScroll(scrollViewServiceDemo);

        btnFindReseller = (Button) view.findViewById(R.id.btnFindReseller);
        btnFindReseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DealersFragment fragment = new DealersFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        btnLogoutService = (Button) view.findViewById(R.id.btnLogoutService);
        btnLogoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyHobbyMarket.getInstance().caravan = null;
                StartFragment fragment = new StartFragment();
                mainActivity.onNavigateToFragment(fragment);
            }
        });

        return view;
    }

    public class ServiceItemListAdapter extends ArrayAdapter<Service> {

        public ServiceItemListAdapter(Context context, ArrayList<Service> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Service item = getItem(position);

            convertView = inflater.inflate(R.layout.service_item, null);

            ImageView imageViewServiceIcon = (ImageView) convertView.findViewById(R.id.imageViewServiceIcon);

            TextView txtServiceItemDoneDate = (TextView) convertView.findViewById(R.id.txtServiceItemDoneDate);
            TextView txtServiceItemType = (TextView) convertView.findViewById(R.id.txtServiceItemType);
            TextView txtServiceItemStatus = (TextView) convertView.findViewById(R.id.txtServiceItemStatus);


            if (item.type.equals("10")) {
                imageViewServiceIcon.setImageResource(R.drawable.ic_service_item_seal_test);
            } else {
                imageViewServiceIcon.setImageResource(R.drawable.ic_service_item_warranty);
            }

            String dateString;
            try {
                dateString = mainActivity.formatDate(item.serviceDate);
            } catch (ParseException e) {
                e.printStackTrace();
                dateString = "";
            }
            txtServiceItemDoneDate.setText(dateString);

            String typeText;
            if(item.type.equals("10"))
            {
                typeText = "Täthetskontroll";
            }
            else{
                typeText = "Garantijobb";
            }

            txtServiceItemType.setText(typeText);

            String statusText;

            if(item.passed)
            {
                statusText = "Godkänd";
                txtServiceItemStatus.setTextColor(ContextCompat.getColor(mainActivity.getContext(), R.color.text_green));
            }
            else{
                statusText = "Ej godkänd";
                txtServiceItemStatus.setTextColor(ContextCompat.getColor(mainActivity.getContext(), R.color.text_red));
            }
            txtServiceItemStatus.setText(statusText);

            listViewServiceItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                    Service node = caravan.serviceEntries.get(pos);
                    ServiceDetailsFragment fragment = new ServiceDetailsFragment();
                    fragment.service = node;
                    mainActivity.onNavigateToFragment(fragment);
                }
            });

            return convertView;
        }

    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(getString(R.string.service_book));
    }

    public ServiceBookConnectedFragment(){}

}
