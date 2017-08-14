package se.netmine.hobby_marknad;

/**
 * Created by jusuf on 2017-08-14.
 */

public class Hobby {

    private static Hobby ourInstance = new Hobby();

    public static Hobby getInstance() {
        return ourInstance;
    }

    public boolean isUserLoggedIn()
    {
//        return this.currentUser.userId != null &&
//                this.currentUser.password != null &&
//                this.currentUser.userId.isEmpty() == false &&
//                this.currentUser.password.isEmpty() == false;
        return true;
    }
}
