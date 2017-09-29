package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-09-28.
 */

public class LoginFragment extends BaseFragment {

    private IMainActivity mainActivity;
    LayoutInflater inflater = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        View view = this.inflater.inflate(R.layout.fragment_login, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
        }

        final TextView email = (TextView) view.findViewById(R.id.editLoginEmail);
        final TextView password = (TextView) view.findViewById(R.id.editLoginPassword);

        TextView btnRegister = (TextView) view.findViewById(R.id.txtLoginRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onNavigateToFragment(new RegisterFragment());
            }
        });

        TextView btnforgotPassword = (TextView) view.findViewById(R.id.txtForgotPassword);
        btnforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onResetPassword();
            }
        });

        Button btnLogin = (Button) view.findViewById(R.id.btnLoginLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyHobbyMarket.getInstance().login(email.getText().toString(), password.getText().toString());
            }
        });


        return view;
    }

}
