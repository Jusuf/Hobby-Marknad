package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jusuf on 2017-08-16.
 */

public class FacilityListFragment extends BaseFragment {

    private IMainActivity mainActivity;

    ListView listViewFacilities = null;
    LayoutInflater inflater = null;
    public ArrayList<Facility> facilities = new  ArrayList();
    ArrayAdapter<Facility> adapter;

    public String listName = "";

    public FacilityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.fragment_facility_list, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(listName);
        }

        adapter = new FacilityListAdapter(mainActivity.getContext(), facilities);
        listViewFacilities = (ListView) view.findViewById(R.id.listViewFacilities);
        listViewFacilities.setAdapter(adapter);

        return view;
    }

    public class FacilityListAdapter extends ArrayAdapter<Facility> {

        public FacilityListAdapter(Context context, ArrayList<Facility> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Facility item = getItem(position);

            convertView = inflater.inflate(R.layout.facility_item, null);

            TextView txtFacilityName = (TextView) convertView.findViewById(R.id.txtFacilityName);

            txtFacilityName.setText(item.name);

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
