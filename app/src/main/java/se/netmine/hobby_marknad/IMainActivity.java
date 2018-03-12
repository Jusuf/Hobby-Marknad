package se.netmine.hobby_marknad;

import android.app.Fragment;
import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by jusuf on 2017-07-10.
 */

public interface IMainActivity {

    void onLoggedIn();

    void onRegistered();

    void onLoggedOut();

    void onResetPassword();

    void setTitle(String title);

    Context getContext();

    void onNavigateToFragment(Fragment fragment);

    void onNavigateBack();

    void onFaqsLoaded(Faq[] faqs);

    void onDealersLoaded(Dealer[] dealers);

    void onCampingsLoaded(ArrayList<Camping> campings, ArrayList<FacilityOption> campingFacilityOptions );

    void showToast(String message);

    String formatDate(String date) throws ParseException;
}
