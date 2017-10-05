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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jusuf on 2017-08-16.
 */

public class FaqListFragment extends BaseFragment {

    private IMainActivity mainActivity;
    EditText txtSearchFaq = null;
    ListView listViewFaqs = null;
    ImageView imageSearchFilter = null;
    FrameLayout frameCheckBox = null;
    LinearLayout searchLayout = null;
    LayoutInflater inflater = null;
    public ArrayList<Faq> loadedFaqs = new  ArrayList<Faq>();
    ArrayAdapter<Faq> adapter;
    public ArrayList<FaqTag> tags = new  ArrayList<FaqTag>();
    ArrayAdapter<FaqTag> tagAdapter;
    String language;
    boolean caravanTag = false;
    boolean campingTipsTag = false;
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



        searchLayout = (LinearLayout) view.findViewById(R.id.searchLaytout);

        adapter = new FaqListAdapter(mainActivity.getContext(), loadedFaqs);
        tagAdapter = new TagListAdapter(mainActivity.getContext(), tags);

        listViewFaqs = (ListView) view.findViewById(R.id.listViewFaqs);
        listViewFaqs.setAdapter(adapter);

        imageSearchFilter = (ImageView) view.findViewById(R.id.imageSearchFilter);
        imageSearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frameCheckBox.getVisibility() == View.GONE){
                    frameCheckBox.setVisibility(View.VISIBLE);
                    listViewFaqs.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                }
                else
                {
                    frameCheckBox.setVisibility(View.GONE);
                    listViewFaqs.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.VISIBLE);

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

    public class TagListAdapter extends ArrayAdapter<FaqTag> {

        public TagListAdapter(Context context, ArrayList<FaqTag> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FaqTag item = getItem(position);

            convertView = inflater.inflate(R.layout.faq_tag_item, null);

            TextView txtTagItem = (TextView) convertView.findViewById(R.id.txtTagItem);

            txtTagItem.setText(item.tagText);

            return convertView;
        }

    }

    public void searchFaq(String textToSearch) {
        MyHobbyMarket.getInstance().getFaqList(textToSearch, language);
    }

    public void addTags() {

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
