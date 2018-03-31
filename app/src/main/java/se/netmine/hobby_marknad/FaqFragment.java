package se.netmine.hobby_marknad;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jusuf on 2017-06-13.
 */

public class FaqFragment extends BaseFragment{

    private IMainActivity mainActivity;
    public Faq faq = null;

    private TextView faqQuestion = null;
    private TextView faqAnswer = null;

    public FaqFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_faq, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getResources().getString(R.string.nav_faq));
        }

        faqQuestion = (TextView) view.findViewById(R.id.txtFaqAnswerQuestion);
        faqAnswer = (TextView) view.findViewById(R.id.txtFaqAnswerAnswer);

        if (faq != null)
        {
            faqQuestion.setText(faq.question);
            faqAnswer.setText(Html.fromHtml(faq.answer));
        }

        return view;
    }

}


