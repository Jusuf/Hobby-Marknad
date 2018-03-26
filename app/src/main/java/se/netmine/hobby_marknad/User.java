package se.netmine.hobby_marknad;

import android.content.SharedPreferences;

/**
 * Created by jusuf on 2017-09-26.
 */

public class User {
    private SharedPreferences settings = null;

    public String firstName;
    public String lastName;
    public String password;
    public String email;
    public String userId;
    public String dealerId;
    public String dealerName;
    public String workshopId;
    public String workshopName;
    public boolean notifyNews;
    public boolean notifyService;
    public String deviceToken;

    public void init(SharedPreferences settings)
    {
        this.settings = settings;

        this.firstName = settings.getString("firstName", "");
        this.lastName = settings.getString("lastName", "");
        this.password = settings.getString("password", null);
        this.email = settings.getString("email", null);
        this.userId = settings.getString("userId", "");
        this.deviceToken = settings.getString("deviceToken", "");
        this.dealerId = settings.getString("dealerId", null);
        this.dealerName = settings.getString("dealerName", null);
        this.workshopId = settings.getString("workshopId", null);
        this.workshopName = settings.getString("workshopName", null);
        this.notifyNews = settings.getBoolean("notifyNews", false);
        this.notifyService = settings.getBoolean("notifyNews", false);

    }

    public void save() {
        SharedPreferences.Editor editor = this.settings.edit();

        editor.putString("firstName", this.firstName);
        editor.putString("lastName", this.lastName);
        editor.putString("password", this.password);
        editor.putString("email", this.email);
        editor.putString("userId", this.userId);
        editor.putString("deviceToken", this.deviceToken);
        editor.putString("dealerId", this.dealerId);
        editor.putString("dealerName", this.dealerName);
        editor.putString("workshopId", this.dealerId);
        editor.putString("workshopName", this.workshopName);
        editor.putBoolean("notifyNews", notifyNews);
        editor.putBoolean("notifyService", notifyService);
        editor.commit();
    }
}

