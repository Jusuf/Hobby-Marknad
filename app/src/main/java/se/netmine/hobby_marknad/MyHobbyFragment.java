package se.netmine.hobby_marknad;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-07-11.
 */

public class MyHobbyFragment extends Fragment implements IFragment {

    private IMainActivity mainActivity;

    private ImageView imgBattery = null;
    private TextView txtBatteryPercentage = null;
    private TextView txtBatteryStatus = null;

    private ImageView imgWater = null;
    private TextView txtWaterPercentage = null;
    private TextView txtWaterStatus = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_hobby, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("My Hobby");
        }

        txtBatteryPercentage = view.findViewById(R.id.batteryPercentText);
        txtBatteryStatus = view.findViewById(R.id.batteryChargingText);

        txtWaterPercentage = view.findViewById(R.id.waterPercentText);
        txtWaterStatus = view.findViewById(R.id.waterInTankText);

        txtBatteryPercentage.setText("100 %");
        txtBatteryStatus.setText("Laddar");

        txtWaterPercentage.setText("85%");
        txtWaterStatus.setText("Vatten i tanken");

        return view;
    }



    public MyHobbyFragment(){}


}
