package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-09-29.
 */

public class RegisterFragment extends BaseFragment {
    private IMainActivity mainActivity;
    private TextView email = null;
    private TextView password = null;
    private TextView firstName = null;
    private TextView lastName = null;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
        }

        final LinearLayout registerLayout = (LinearLayout) view.findViewById(R.id.registerLayout);
        final LinearLayout registeredLayout = (LinearLayout) view.findViewById(R.id.registeredLayout);

        this.email = (TextView) view.findViewById(R.id.editRegisterEmail);
        this.password = (TextView) view.findViewById(R.id.editRegisterPassword);
        this.firstName = (TextView) view.findViewById(R.id.editRegisterFirstName);
        this.lastName = (TextView) view.findViewById(R.id.editRegisterLastName);

        Button btnRegistered = (Button) view.findViewById(R.id.btnRegistered);
        btnRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onNavigateBack();
            }
        });


        Button btnRegister = (Button) view.findViewById(R.id.btnRegisterRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                if( email.getText().toString().length() == 0 )
                {
                    email.setError(getResources().getString(R.string.register_error_email_required));
                    isValid = false;
                }

                if( password.getText().toString().length() == 0 )
                {
                    password.setError(getResources().getString(R.string.register_error_password_required));
                    isValid = false;
                }

                if( firstName.getText().toString().length() == 0 )
                {
                    firstName.setError(getResources().getString(R.string.register_error_firstname_required));
                    isValid = false;
                }

                if( lastName.getText().toString().length() == 0 )
                {
                    lastName.setError(getResources().getString(R.string.register_error_lastname_required));
                    isValid = false;
                }

                if(isValid == true)
                {
                    MyHobbyMarket.getInstance().register(email.getText().toString(), password.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                    registerLayout.setVisibility(View.GONE);
                    registeredLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        return view;
    }

}
