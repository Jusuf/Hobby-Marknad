package se.netmine.hobby_marknad;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by jusuf on 2017-07-10.
 */

public interface IMainActivity {
    public void setTitle(String title);

    public Context getContext();

    public void onNavigateToFragment(Fragment fragment);

    public void onNavigateBack();
}
