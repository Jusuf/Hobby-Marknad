package se.netmine.hobby_marknad;

/**
 * Created by jusuf on 2017-09-28.
 */

public class MyHobbyMarket {
    public User currentUser = null;
    public IMainActivity mainActivity;

    private static MyHobbyMarket ourInstance = new MyHobbyMarket();

    public static MyHobbyMarket getInstance() {
        return ourInstance;
    }

    private MyHobbyMarket() {
        currentUser = new User();
    }

    public boolean isUserLoggedIn()
    {
        return this.currentUser.userId != null &&
                this.currentUser.password != null &&
                this.currentUser.userId.isEmpty() == false &&
                this.currentUser.password.isEmpty() == false;
    }

    public void setDeviceToken(String deviceToken)
    {
        this.currentUser.deviceToken = deviceToken;
        this.currentUser.save();
    }

}
