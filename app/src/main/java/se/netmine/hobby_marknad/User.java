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
    public String myHobbyKey;
    public String userId;
    public String dealerId;
    public String dealerName;
    public String workshopId;
    public String workshopName;
    public boolean notifyBattery;
    public boolean notifyWater;
    public boolean notifyTemp;
    public boolean notifyPosition;
    public boolean notifyNews;
    public String deviceToken;

    public void init(SharedPreferences settings)
    {
        this.settings = settings;

        this.firstName = settings.getString("firstName", "");
        this.lastName = settings.getString("lastName", "");
        this.password = settings.getString("password", null);
        this.email = settings.getString("email", null);
        this.myHobbyKey = settings.getString("myHobbyKey", "");
        this.userId = settings.getString("userId", "");
        this.deviceToken = settings.getString("deviceToken", "");
        this.dealerId = settings.getString("dealerId", "");
        this.dealerName = settings.getString("dealerName", "");
        this.workshopId = settings.getString("workshopId", "");
        this.workshopName = settings.getString("workshopName", "");
        this.notifyBattery = settings.getBoolean("notifyBattery", false);
        this.notifyWater = settings.getBoolean("notifyWater", false);
        this.notifyTemp = settings.getBoolean("notifyTemp", false);
        this.notifyPosition = settings.getBoolean("notifyPosition", false);
        this.notifyNews = settings.getBoolean("notifyNews", false);

    }

    public void save() {
        SharedPreferences.Editor editor = this.settings.edit();

        editor.putString("firstName", this.firstName);
        editor.putString("lastName", this.lastName);
        editor.putString("password", this.password);
        editor.putString("email", this.email);
        editor.putString("myHobbyKey", this.myHobbyKey);
        editor.putString("userId", this.userId);
        editor.putString("deviceToken", this.deviceToken);
        editor.putString("dealerId", this.dealerId);
        editor.putString("dealerName", this.dealerName);
        editor.putString("workshopId", this.dealerId);
        editor.putString("workshopName", this.workshopName);
        editor.putBoolean("notifyBattery", notifyBattery);
        editor.putBoolean("notifyWater", notifyWater);
        editor.putBoolean("notifyTemp", notifyTemp);
        editor.putBoolean("notifyPosition", notifyPosition);
        editor.putBoolean("notifyNews", notifyNews);
        editor.commit();
    }
}

