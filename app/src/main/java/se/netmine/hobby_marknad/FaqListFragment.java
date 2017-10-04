package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    ImageView imageSearchFilter = null;
    RadioGroup radioGroup = null;
    FrameLayout radioFrame = null;
    LayoutInflater inflater = null;
    public ArrayList<Faq> loadedFaqs = new  ArrayList<Faq>();
    ArrayAdapter<Faq> adapter;
    String language;
    boolean caravanTag = false;
    boolean campingtipsTag = false;
    boolean myHobbyTag = false;

    public FaqListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.fragment_faq_list, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle(getString(R.string.nav_faq));
        }

        adapter = new FaqListAdapter(mainActivity.getContext(), loadedFaqs);

        listViewFaqs = (ListView) view.findViewById(R.id.listViewFaqs);
        listViewFaqs.setAdapter(adapter);

        radioFrame = (FrameLayout) view.findViewById(R.id.frameFaqTags);

        imageSearchFilter = (ImageView) view.findViewById(R.id.imageSearchFilter);
        imageSearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioFrame.getVisibility() == View.GONE){
                    radioFrame.setVisibility(View.VISIBLE);
                    listViewFaqs.setVisibility(View.GONE);

                    txtSearchFaq.setEnabled(false);


                }
                else
                {
                    radioFrame.setVisibility(View.GONE);
                    listViewFaqs.setVisibility(View.VISIBLE);

                    txtSearchFaq.setEnabled(true);

                }

            }
        });


        radioGroup = (RadioGroup) view.findViewById(R.id.radioTag);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new  RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    // checkedId is the RadioButton selected
                    switch (checkedId) {
                        case R.id.faqTagManageCaravan:
                            if(rb.isChecked())
                            {
                                caravanTag = true;
                            }
                            else
                            {
                                caravanTag = false;
                            }
                            break;

                        case R.id.faqTagCampingTips:
                            if(rb.isChecked())
                            {
                                campingtipsTag = true;
                            }
                            else
                            {
                                campingtipsTag = false;
                            }
                            break;

                        case R.id.faqTagMyHobby:
                            if(rb.isChecked())
                            {
                                myHobbyTag = true;
                            }
                            else
                            {
                                myHobbyTag = false;
                            }
                            break;
                    }
                }
            }

        });

        listViewFaqs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

                Faq node = loadedFaqs.get(pos);
                FaqFragment fragment = new FaqFragment();
                fragment.faq = node;
                mainActivity.onNavigateToFragment(fragment);
            }
        });

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
        mainActivity.setTitle(getString(R.string.nav_faq));
    }

}
