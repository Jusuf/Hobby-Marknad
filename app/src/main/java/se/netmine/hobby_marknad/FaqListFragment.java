package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by jusuf on 2017-08-16.
 */

public class FaqListFragment extends BaseFragment {

    private IMainActivity mainActivity;
    EditText txtSearchFaq = null;
    ListView listViewFaqs = null;
    LayoutInflater inflater = null;
    public ArrayList<Faq> loadedFaqs = new  ArrayList<Faq>();
    ArrayAdapter<Faq> adapter;
    String language;

    public FaqListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_faq_list, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("FAQ");
        }

        adapter = new FaqListAdapter(mainActivity.getContext(), loadedFaqs);

        listViewFaqs = (ListView) view.findViewById(R.id.listViewFaqs);
        listViewFaqs.setAdapter(adapter);

        txtSearchFaq = (EditText) view.findViewById(R.id.txtSearchFaq);

        language = Locale.getDefault().getCountry();

        loadFaqs();

        txtSearchFaq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    loadFaqs();
                } else {
                    searchFaq(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    private void loadFaqs() {
        MyHobbyMarket.getInstance().getFaqList("", language);
    }

    public class FaqListAdapter extends ArrayAdapter<Faq> {

        public FaqListAdapter(Context context, ArrayList<Faq> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Faq item = getItem(position);

            convertView = inflater.inflate(R.layout.faq_item, null);

            TextView txtFaqItemTitle = (TextView) convertView.findViewById(R.id.txtFaqItem);

            txtFaqItemTitle.setText(item.question);

            return convertView;
        }

    }
    public void searchFaq(String textToSearch) {
        MyHobbyMarket.getInstance().getFaqList(textToSearch, language);
    }

    @Override
    public void onFaqsUpdated(Faq[] faqs)
    {
        loadedFaqs.clear();

        if (faqs != null)
        {
            for (Faq faq : faqs) {
                loadedFaqs.add(faq);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume()
    {
        // After a pause
        super.onResume();
        loadFaqs();
        mainActivity.setTitle("FAQ");
    }

}
