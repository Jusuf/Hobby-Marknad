package se.netmine.hobby_marknad;

import android.app.Fragment;

/**
 * Created by jusuf on 2017-07-10.
 */

public interface IMainActivity {
    public void setTitle(String title);

    public void onNavigateToFragment(Fragment fragment);
}
