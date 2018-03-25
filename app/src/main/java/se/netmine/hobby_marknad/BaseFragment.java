package se.netmine.hobby_marknad;

import android.app.Fragment;

import java.util.ArrayList;

/**
 * Created by jusuf on 2017-08-14.
 */

public class BaseFragment extends Fragment implements IFragment{

    @Override
    public void setTitle() {

    }

    @Override
    public void onMessagesUpdated(UserMessage[] messages) {}

    @Override
    public void onFaqsUpdated(Faq[] faqs) {}

    @Override
    public void onDealersUpdated(Dealer[] dealers) {}

    @Override
    public void onCampingsUpdated(ArrayList<Camping> campings, ArrayList<FacilityOption> campingFacilityOptions) {}

}
