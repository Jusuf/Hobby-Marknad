package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jusuf on 2017-08-16.
 */

public class ConnectToServiceFragment extends BaseFragment {

    private IMainActivity mainActivity;
    EditText txtVinNumber = null;
    LayoutInflater inflater = null;
    Button btnConnectToServiceSubmit = null;
    String vin;

    public ConnectToServiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.fragment_connect_to_service, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.nav_faq));
        }

        btnConnectToServiceSubmit = (Button) view.findViewById(R.id.btnConnectToServiceSubmit);
        btnConnectToServiceSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                connectToService();
            }
        });

        txtVinNumber = (EditText) view.findViewById(R.id.txtVinNumber);

        txtVinNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    vin = "";
                } else {
                    vin = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void connectToService() {
//        vin = "Fd32696";
        MyHobbyMarket.getInstance().connectToService(vin);
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        mainActivity.setTitle(getString(R.string.enter_vin_number));
    }

}
