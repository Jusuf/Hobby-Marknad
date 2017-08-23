package se.netmine.hobby_marknad;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jusuf on 2017-08-16.
 */

public class FaqListFragment extends BaseFragment {
    private IMainActivity mainActivity;

    ArrayAdapter<String> adapter;

    EditText txtSearchFaq = null;
    ListView listViewFaqs = null;
    String[] faqs = null;
    ArrayList<String> listFaqs = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq_list, container, false);

        if (getActivity() instanceof IMainActivity) {
            mainActivity = (IMainActivity) getActivity();
            mainActivity.setTitle("Faqs");
        }

        listViewFaqs = view.findViewById(R.id.listViewFaqs);
        txtSearchFaq = view.findViewById(R.id.txtSearchFaq);

        initList();

        txtSearchFaq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    initList();
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

    protected void initList() {
        faqs = getResources().getStringArray(R.array.array_faqs);
        listFaqs = new ArrayList<>(Arrays.asList(faqs));
        adapter = new ArrayAdapter<>(mainActivity.getContext(),
                android.R.layout.simple_list_item_1,
                listFaqs);

        listViewFaqs.setAdapter(adapter);
    }

    public void searchFaq(String textToSearch) {
        for (String item : faqs) {
            if (!item.contains(textToSearch)) {
                listFaqs.remove(item);
            }
            if(item.contains(textToSearch) && !listFaqs.contains(item)){
                listFaqs.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }

    public FaqListFragment() {
    }
}
