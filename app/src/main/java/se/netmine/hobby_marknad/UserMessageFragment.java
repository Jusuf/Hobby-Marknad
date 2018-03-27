package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-06-13.
 */

public class UserMessageFragment extends BaseFragment{

    private IMainActivity mainActivity;
    public UserMessage message = null;

    private TextView txtMessageTitle = null;
    private TextView txtMessageBody = null;
    private Button btnGoToDealer = null;
    private Button btnRemoveMessage = null;

    public UserMessageFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_user_message, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_faq));
        }

        txtMessageTitle = (TextView) view.findViewById(R.id.txtMessageTitle);
        txtMessageBody = (TextView) view.findViewById(R.id.txtMessageBody);

        btnRemoveMessage  = (Button) view.findViewById(R.id.btnRemoveMessage);

        if (message != null)
        {
            txtMessageTitle.setText(message.title);
            txtMessageBody.setText(message.body);

            if(!message.isRead)
            {
                MyHobbyMarket.getInstance().setMessageAsRead(message.id);
            }

            btnRemoveMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   MyHobbyMarket.getInstance().removeMessage(message.id);
                   mainActivity.onNavigateBack();
                }
            });
        }




        btnGoToDealer = (Button) view.findViewById(R.id.btnGoToDealer);

        if(message.sender != null)
        {
            btnGoToDealer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DealerFragment fragment = new DealerFragment();
                    fragment.dealer = message.sender;
                    mainActivity.onNavigateToFragment(fragment);
                }
            });
        }
        else
        {
            btnGoToDealer.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        if(message != null)
        mainActivity.setTitle(message.title);
    }

}


