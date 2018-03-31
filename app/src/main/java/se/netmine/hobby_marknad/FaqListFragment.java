package se.netmine.hobby_marknad;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jusuf on 2017-08-16.
 */

public class FaqListFragment extends BaseFragment {

    private IMainActivity mainActivity;
    EditText txtSearchFaq = null;
    ListView listViewFaqs = null;
    ListView listViewTaqs = null;
    ImageView imageSearchFilter = null;
    LinearLayout layoutCheckBox = null;
    LinearLayout searchLayout = null;
    LayoutInflater inflater = null;
    Button btnFilterSubmit = null;
    public ArrayList<Faq> loadedFaqs = new  ArrayList<Faq>();
    ArrayAdapter<Faq> adapter;
    public ArrayList<FaqTag> tags = new  ArrayList<FaqTag>();
    ArrayAdapter<FaqTag> tagAdapter;
    String language;
    String joinedTags = "";
    String searchQuery;

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
        layoutCheckBox = (LinearLayout) view.findViewById(R.id.layoutCheckBox);

        adapter = new FaqListAdapter(mainActivity.getContext(), loadedFaqs);
        listViewFaqs = (ListView) view.findViewById(R.id.listViewFaqs);
        listViewFaqs.setAdapter(adapter);

        loadTags();
        tagAdapter = new TagListAdapter(mainActivity.getContext(), tags);
        listViewTaqs = (ListView) view.findViewById(R.id.listViewTagCheckBoxes);
        listViewTaqs.setAdapter(tagAdapter);

        btnFilterSubmit = (Button) view.findViewById(R.id.btnFilterSubmit);
        btnFilterSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                List<String> tagStringList = new ArrayList<String>();
                for(FaqTag faqTag : tags){
                    if(faqTag.value == true)
                    {
                        tagStringList.add(Integer.toString(faqTag.id));
                    }
                }

                joinedTags = TextUtils.join(", ", tagStringList);

                if(layoutCheckBox.getVisibility() == View.GONE){

                    layoutCheckBox.setVisibility(View.VISIBLE);
                    listViewFaqs.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                }
                else
                {
                    layoutCheckBox.setVisibility(View.GONE);
                    listViewFaqs.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.VISIBLE);
                }

                searchFaq(searchQuery);
            }
        });

        imageSearchFilter = (ImageView) view.findViewById(R.id.imageSearchFilter);
        imageSearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutCheckBox.getVisibility() == View.GONE){

                    layoutCheckBox.setVisibility(View.VISIBLE);
                    listViewFaqs.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                }
                else
                {
                    layoutCheckBox.setVisibility(View.GONE);
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
                    searchQuery = charSequence.toString();
                    searchFaq(searchQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void loadFaqs() {
        MyHobbyMarket.getInstance().getFaqList("", language, joinedTags);
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
            ImageView imageViewFaqIcon = (ImageView) convertView.findViewById(R.id.imageViewFaqIcon);


            txtFaqItemTitle.setText(item.question);

            imageViewFaqIcon.setImageResource(R.drawable.ic_faq_text);

            return convertView;
        }

    }

    public class TagListAdapter extends ArrayAdapter<FaqTag> {

        public TagListAdapter(Context context, ArrayList<FaqTag> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final FaqTag item = getItem(position);

            convertView = inflater.inflate(R.layout.faq_tag_item, null);

            ImageView imageTag = (ImageView) convertView.findViewById(R.id.imageViewTagIcon);
            TextView txtTagItem = (TextView) convertView.findViewById(R.id.txtTagItem);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxTag);

            if(item.tagText.equals("Sköta husvagnen"))
            {
                imageTag.setImageResource(R.drawable.ic_filter_handle_caravan);
            }
            if(item.tagText.equals("Tips för campinglivet"))
            {
                imageTag.setImageResource(R.drawable.ic_filter_tip);
            }
            if(item.tagText.equals("My Hobby"))
            {
                imageTag.setImageResource(R.drawable.ic_filter_my_hobby);
            }

            txtTagItem.setText(item.tagText);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setCheckboxValue(item.id, isChecked);
                }
            });

            return convertView;
        }

    }

    public void searchFaq(String textToSearch) {
        MyHobbyMarket.getInstance().getFaqList(textToSearch, language, joinedTags);
    }

    public void loadTags() {
        FaqTag tag1 = new FaqTag();
        tag1.id = 1;
        tag1.tagText = getString(R.string.faq_tag_manage_caravan);
        tags.add(tag1);

        FaqTag tag2 = new FaqTag();
        tag2.id = 2;
        tag2.tagText = getString(R.string.faq_tag_tips_for_camping_life);
        tags.add(tag2);

        FaqTag tag3 = new FaqTag();
        tag3.id = 3;
        tag3.tagText = getString(R.string.faq_tag_myhobby);
        tags.add(tag3);
    }

    public void setCheckboxValue(int id, boolean value)
    {
        for(FaqTag faqTag : tags){
            if(faqTag.id == id)
            {
                faqTag.value = value;
            }
        }
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
