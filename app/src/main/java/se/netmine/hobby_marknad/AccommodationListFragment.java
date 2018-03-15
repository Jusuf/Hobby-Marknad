package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jusuf on 2017-08-16.
 */

public class AccommodationListFragment extends BaseFragment {

    private IMainActivity mainActivity;

    ListView listViewAccommodations = null;
    LayoutInflater inflater = null;
    LinearLayout linearLayoutListImage = null;
    ImageView imgAccommodationListIcon = null;
    public ArrayList<Accommodation> accommodations = new  ArrayList();
    ArrayAdapter<Accommodation> adapter;

    public String listName = "";

    public AccommodationListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.fragment_accommodation_list, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(listName);
        }

        imgAccommodationListIcon = (ImageView) view.findViewById(R.id.imgAccommodationListIcon);
        linearLayoutListImage = (LinearLayout) view.findViewById(R.id.linearLayoutListImage);

        adapter = new AccommodationListAdapter(mainActivity.getContext(), accommodations);
        listViewAccommodations = (ListView) view.findViewById(R.id.listViewAccommodations);
        listViewAccommodations.setAdapter(adapter);

        return view;
    }

    public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {

        public AccommodationListAdapter(Context context, ArrayList<Accommodation> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Accommodation item = getItem(position);

            convertView = inflater.inflate(R.layout.accommodation_item, null);

            TextView txtAccommodationName = (TextView) convertView.findViewById(R.id.txtAccommodationName);

            txtAccommodationName.setText(item.name);

            return convertView;
        }

    }


    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(listName);
    }

}
