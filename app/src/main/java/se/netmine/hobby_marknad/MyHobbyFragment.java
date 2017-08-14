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

public class MyHobbyFragment extends BaseFragment {

    private IMainActivity mainActivity;

    private ImageView imgBattery = null;
    private TextView txtBatteryPercentage = null;

    private ImageView imgWater = null;
    private TextView txtWaterPercentage = null;

    private ImageView imgTemp = null;
    private TextView txtTempInCaravanHeat = null;

    private ImageView imgCarvan = null;
    private TextView txtTempOutsideHeat = null;
    private TextView txtCity = null;

    private TextView txtAmountOfTimers = null;
    private TextView txtAmountOfLamps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_hobby, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("My Hobby");
        }

        txtBatteryPercentage = view.findViewById(R.id.batteryPercentText);
        txtWaterPercentage = view.findViewById(R.id.waterPercentText);

        txtTempInCaravanHeat = view.findViewById(R.id.tempInCaravanHeatText);
        txtCity  = view.findViewById(R.id.cityText);
        txtTempOutsideHeat = view.findViewById(R.id.tempOutsideHeatText);

        txtAmountOfTimers = view.findViewById(R.id.amountOfTimersText);
        txtAmountOfLamps = view.findViewById(R.id.amountOfLampsText);

        txtBatteryPercentage.setText("50%");

        txtWaterPercentage.setText("85%");

        txtTempInCaravanHeat.setText("22°C");

        txtCity.setText("Värnamo");
        txtTempOutsideHeat.setText("22°C" + " " + getString(R.string.temp_outside ));

        txtAmountOfTimers.setText("0");
        txtAmountOfLamps.setText("0");

        return view;
    }



    public MyHobbyFragment(){}


}
