package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jusuf on 2017-08-14.
 */

public class ServiceBookDemoFragment extends BaseFragment {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;
    private Caravan caravan;
    ExpandableHeightListView  listViewServiceItems = null;
    ArrayAdapter<Service> adapter;

    private TextView textModelName;
    private TextView textModelYearName;
    private TextView textVehicleIdentificationNumber;
    private TextView textInTraficDate;
    private TextView textGuaranteeDate;

    private LinearLayout testDemoButton = null;
    private LinearLayout connectButton = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_service_book_demo, container, false);


        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("Service book demo");
        }

        createDemoData();

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


//        testDemoButton = (LinearLayout) view.findViewById(R.id.testDemoButton);
//
//        testDemoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CatalogueAndMagazinesFragment fragment = new CatalogueAndMagazinesFragment();
//                mainActivity.onNavigateToFragment(fragment);
//            }
//        });



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

            TextView txtServiceItemDoneDate = (TextView) convertView.findViewById(R.id.txtServiceItemDoneDate);
            TextView txtServiceItemType = (TextView) convertView.findViewById(R.id.txtServiceItemType);
            TextView txtServiceItemStatus = (TextView) convertView.findViewById(R.id.txtServiceItemStatus);

            txtServiceItemDoneDate.setText(item.serviceDate);

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

            return convertView;
        }

    }

    public void createDemoData() {
        caravan = new Caravan();
        caravan.id = "";
        caravan.serieId = "";
        caravan.floorplanId = "";
        caravan.warrantyDate = "2020-01-01";
        caravan.trafficDate = "2015-01-01";
        caravan.chassisNo = "xxxxxxx";
        caravan.serieName = "Excellent 540 UL";
        caravan.floorplanName = "";
        caravan.year = "2015";
        caravan.serviceEntries = new ArrayList<>();


        Service serviceEntry1 = new Service();
        serviceEntry1.id = "1";
        serviceEntry1.serviceDate = "2012-07-06";
        serviceEntry1.type = "10";
        serviceEntry1.protocol = "";
        serviceEntry1.passed = true;
        serviceEntry1.dealerId = "";

        caravan.serviceEntries.add(serviceEntry1);

        Service serviceEntry2 = new Service();
        serviceEntry2.id = "1";
        serviceEntry2.serviceDate = "2012-01-12";
        serviceEntry2.type = "10";
        serviceEntry2.protocol = "";
        serviceEntry2.passed = true;
        serviceEntry2.dealerId = "";

        caravan.serviceEntries.add(serviceEntry2);

        Service serviceEntry3 = new Service();
        serviceEntry3.id = "1";
        serviceEntry3.serviceDate = "2012-07-12";
        serviceEntry3.type = "10";
        serviceEntry3.protocol = "";
        serviceEntry3.passed = false;
        serviceEntry3.dealerId = "";

        caravan.serviceEntries.add(serviceEntry3);

        Service serviceEntry4 = new Service();
        serviceEntry4.id = "1";
        serviceEntry4.serviceDate = "2012-08-27";
        serviceEntry4.type = "20";
        serviceEntry4.protocol = "";
        serviceEntry4.passed = true;
        serviceEntry4.dealerId = "";

        caravan.serviceEntries.add(serviceEntry4);

        Service serviceEntry5 = new Service();
        serviceEntry5.id = "1";
        serviceEntry5.serviceDate = "2012-01-09";
        serviceEntry5.type = "20";
        serviceEntry5.protocol = "";
        serviceEntry5.passed = true;
        serviceEntry5.dealerId = "";

        caravan.serviceEntries.add(serviceEntry5);


    }

    public ServiceBookDemoFragment(){}

}
