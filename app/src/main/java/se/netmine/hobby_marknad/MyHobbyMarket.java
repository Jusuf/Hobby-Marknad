package se.netmine.hobby_marknad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.orm.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by jusuf on 2017-09-28.
 */

public class MyHobbyMarket {

    private static final int API_REGISTER = 0;
    private static final int API_CONNECT = 1;
    private static final int API_LOGIN = 2;
    private static final int API_LOGOUT = 3;
    private static final int API_MESSAGES = 4;
    private static final int API_SYNC = 5;
    private static final int API_CHANGE_PASSWORD = 6;
    private static final int API_FAQS = 7;
    private static final int API_DEALERS = 8;
    private static final int API_SERVICE = 9;
    private static final int API_CAMPINGS = 10;
    private static final int API_DEALER = 11;
    private static final int API_SET_MESSAGE_AS_READ = 12;
    private static final int API_REMOVE_MESSAGE = 1;

    //    public static  String url = "https://admin.myhobby.nu/";
    public static String url = "http://192.168.20.125/hobby/";
//    public static String url = "http://192.168.0.11/hobby/";
    public static String baseUrl = url + "api/myHobby/";
    public static String baseUrlAndroid = url + "api/hobbyMarketAndroid/";

    public User currentUser = null;
    public IMainActivity mainActivity;
    public UserMessage[] messages;
    public Faq[] faqs;
    public Dealer[] dealers;
    public Dealer dealer;
    public ArrayList<Camping> loadedCampings;
    public ArrayList<FacilityOption> campingFacilityOptions;
    public Caravan caravan;

    private static MyHobbyMarket ourInstance = new MyHobbyMarket();

    public static MyHobbyMarket getInstance() {
        return ourInstance;
    }

    private MyHobbyMarket() {
        currentUser = new User();
    }

    public void init(SharedPreferences settings) {
        this.currentUser.init(settings);
    }

    public boolean isUserLoggedIn() {
        return this.currentUser.userId != null &&
                this.currentUser.password != null &&
                this.currentUser.userId.isEmpty() == false &&
                this.currentUser.password.isEmpty() == false;
    }

    public String getEmail() {
        return this.currentUser.email;
    }

    public String getFirstName() {
        return this.currentUser.firstName;
    }

    public String getLastName() {
        return this.currentUser.lastName;
    }

    public String getDealerName() {
        return this.currentUser.dealerName;
    }

    public String getWorkshopName() {
        return this.currentUser.workshopName;
    }

    private void onUpdateCampingsFromDb(ArrayList<Camping> campingsFromDb, ArrayList<FacilityOption> campingFacilityOptionsFromDb) {
        if (campingsFromDb.size() > 0) {
            mainActivity.onCampingsLoaded(campingsFromDb, campingFacilityOptionsFromDb);
        }
    }

    private class UpdateDb extends AsyncTask<Void, Void, Void> {

        private ArrayList<Camping> campings = new ArrayList<>();
        private ArrayList<FacilityOption> campingFacilityOptions = new ArrayList<>();

        private ArrayList<Camping> campingsFromDb = new ArrayList<>();
        private ArrayList<FacilityOption> campingFacilityOptionsFromDb = new ArrayList<>();

        private UpdateDb(ArrayList<Camping> campings, ArrayList<FacilityOption> campingFacilityOptions) {
            this.campings = campings;
            this.campingFacilityOptions = campingFacilityOptions;
        }

        private ProgressDialog pDialog;
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.sync);

        @Override
        protected void onPreExecute() {
            if (loadingMessage != null) {
                pDialog = new ProgressDialog(mainActivity.getContext());
                pDialog.setMessage(loadingMessage);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Camping.deleteAll(Camping.class);
            Facility.deleteAll(Facility.class);
            FacilityOption.deleteAll(FacilityOption.class);
            CampingImage.deleteAll(CampingImage.class);
            Accommodation.deleteAll(Accommodation.class);

            try {
                String campingIdAsSQL = StringUtil.toSQLName("campingId") + "=?";
                String facilityIdAsSQL = StringUtil.toSQLName("facilityId") + "=?";
                String accommodationIdAsSQL = StringUtil.toSQLName("accommodationId") + "=?";
                String campingImageFileNameAsSQL = StringUtil.toSQLName("fileName") + "=?";

                for (Camping c : campings) {

                    if (c.campingId != null) {

                        List<Camping> foundCampings = Camping.find(Camping.class, campingIdAsSQL, c.campingId);

                        if (foundCampings.size() == 0) {
                            c.save();
                        } else {
                            for (Camping camping : foundCampings) {
                                Camping dbCamping = Camping.findById(Camping.class, camping.getId());
//                                dbCamping.save();
                            }
                        }

                    }

                    for (Facility f : c.facilities) {

                        if (f.facilityId != null) {

                            List<Facility> foundFacilities = Facility.find(Facility.class, campingIdAsSQL + " and " + facilityIdAsSQL, c.campingId, f.facilityId);

                            if (foundFacilities.size() == 0) {
                                f.camping = c;
                                f.campingId = c.campingId;
                                f.save();
                            } else {
                                for (Facility foundFacility : foundFacilities) {
                                    Facility dbFacility = Facility.findById(Facility.class, foundFacility.getId());
//                                    dbFacility.save();
                                }
                            }
                        }

                    }

                    for (Accommodation a : c.accommodations) {

                        if (a.accommodationId != null) {

                            List<Accommodation> foundAccommodations = Accommodation.find(Accommodation.class, campingIdAsSQL + " and " + accommodationIdAsSQL, c.campingId, a.accommodationId);

                            if (foundAccommodations.size() == 0) {
                                a.camping = c;

                                a.campingId = c.campingId;
                                a.save();
                            } else {
                                for (Accommodation foundAccommodation : foundAccommodations) {
                                    Accommodation dbAccommodation = Accommodation.findById(Accommodation.class, foundAccommodation.getId());
//                                    dbAccommodation.save();
                                }
                            }
                        }

                    }

                    for (String i : c.images) {

                        List<CampingImage> foundCampingImages = Facility.find(CampingImage.class, campingImageFileNameAsSQL, i);

                        if (foundCampingImages.size() == 0) {
                            CampingImage campingImage = new CampingImage();
                            campingImage.fileName = i;
                            campingImage.campingId = c.campingId;
                            campingImage.save();
                        } else {
                            for (CampingImage foundCampingImage : foundCampingImages) {
                                CampingImage dbCampingImage = Facility.findById(CampingImage.class, foundCampingImage.getId());
//                                    dbCampingImage.save();
                            }
                        }
                    }

                }

                if (campingFacilityOptions != null) {
                    if (campingFacilityOptions.size() > 0) {

                        for (FacilityOption f : campingFacilityOptions) {
                            List<FacilityOption> foundFacilityOptions = FacilityOption.find(FacilityOption.class, facilityIdAsSQL, f.facilityId);
                            if (foundFacilityOptions.size() == 0) {
                                f.save();
                            } else {
                                for (FacilityOption foundFacilityOption : foundFacilityOptions) {
                                    FacilityOption dbFacilityOption = FacilityOption.findById(FacilityOption.class, foundFacilityOption.getId());
//                                    dbFacilityOption.save();
                                }
                            }
                        }
                    }

                }


                List<Camping> dbCampings = Camping.listAll(Camping.class);
                campingsFromDb.addAll(dbCampings);

                for (Camping camping : this.campingsFromDb) {

                    List<CampingImage> campingImages = CampingImage.find(CampingImage.class, campingIdAsSQL, camping.campingId);
                    camping.images = new ArrayList<>();

                    for (CampingImage image : campingImages) {
                        camping.images.add(image.fileName);
                    }

                    List<Facility> campingFacilities = Facility.find(Facility.class, campingIdAsSQL, camping.campingId);
                    camping.facilities = new ArrayList<>();
                    camping.facilities.addAll(campingFacilities);

                    List<Accommodation> campingAccommodations = Accommodation.find(Accommodation.class, campingIdAsSQL, camping.campingId);
                    camping.accommodations = new ArrayList<>();
                    camping.accommodations.addAll(campingAccommodations);
                }

                List<FacilityOption> dbFacilityOptions = FacilityOption.listAll(FacilityOption.class);
                campingFacilityOptionsFromDb.addAll(dbFacilityOptions);

                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void voids) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }


            onUpdateCampingsFromDb(campingsFromDb, campingFacilityOptionsFromDb);
        }
    }

    private class LoadCampingsFromDbAsync extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_loading_title);

        @Override
        protected void onPreExecute() {
            if (loadingMessage != null) {
                pDialog = new ProgressDialog(mainActivity.getContext());
                pDialog.setMessage(loadingMessage);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LoadCampingsFromDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

        }
    }

    private void LoadCampingsFromDb() {

        List<Camping> campingsFromDbList = Camping.listAll(Camping.class);
        List<FacilityOption> facilityOptionsFromDb = FacilityOption.listAll(FacilityOption.class);

        String campingIdAsSQL = StringUtil.toSQLName("campingId") + "=?";

        ArrayList<Camping> campingsFromDb = new ArrayList<>();
        campingsFromDb.addAll(campingsFromDbList);
        ArrayList<FacilityOption> campingFacilityOptionsFromDb = new ArrayList<>();
        campingFacilityOptionsFromDb.addAll(facilityOptionsFromDb);

        for (Camping camping : campingsFromDb) {
            List<CampingImage> campingImages = CampingImage.find(CampingImage.class, campingIdAsSQL, camping.campingId);
            camping.images = new ArrayList<>();
            for (CampingImage image : campingImages) {
                camping.images.add(image.fileName);
            }

            List<Facility> campingFacilities = Facility.find(Facility.class, campingIdAsSQL, camping.campingId);
            camping.facilities = new ArrayList<>();
            camping.facilities.addAll(campingFacilities);

            List<Accommodation> campingAccommodations = Accommodation.find(Accommodation.class, campingIdAsSQL, camping.campingId);
            camping.accommodations = new ArrayList<>();
            camping.accommodations.addAll(campingAccommodations);
        }

        onUpdateCampingsFromDb(campingsFromDb, campingFacilityOptionsFromDb);
    }

    public void setDeviceToken(String deviceToken) {
        if(deviceToken.equals(""))
        {
            deviceToken = FirebaseInstanceId.getInstance().getToken();
        }
        FirebaseInstanceId.getInstance().getToken();
        this.currentUser.deviceToken = deviceToken;
        this.currentUser.save();
    }

    private void showErrorDialog(String message) {
        String title = mainActivity.getContext().getResources().getString(R.string.app_error_title);

        new AlertDialog.Builder(mainActivity.getContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected void register(String email, String password, String firstName, String lastName) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_REGISTER, loadingMessage,
                null,
                email,
                password,
                firstName,
                lastName,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        api.execute();
    }

    protected void registerDone(String result, String email, String password, String firstName, String lastName) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {
            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if (jObject.has("success")) {
                success = jObject.getBoolean("success");
            }

            if (success == true) {
                mainActivity.onRegistered();
            } else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }
        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void login(String email, String password) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_LOGIN, loadingMessage,
                null,
                email,
                password,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        api.execute();
    }

    protected void loginDone(String result, String email, String password) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {
            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if (jObject.has("success")) {
                success = jObject.getBoolean("success");
            }

            if (success == true) {
                String userId = jObject.getString("userId");

                currentUser.email = email;
                currentUser.password = password;
                currentUser.userId = userId;

                currentUser.save();
                mainActivity.onLoggedIn();
            } else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }


        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void changePassword(String oldPassword, String newPassword, String newPasswordConfirm) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_CHANGE_PASSWORD,
                loadingMessage,
                currentUser.userId,
                null,
                currentUser.password,
                null,
                null,
                oldPassword,
                newPassword,
                newPasswordConfirm,
                null,
                null,
                null,
                null,
                null,
                null);
        api.execute();
    }

    protected void changePasswordDone(String result, String oldPassword, String newPassword, String newPasswordConfirm) {
        try {
            if (result == null || result.isEmpty()) {
                result = "{\"success\": \"true\"}";
            }


            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if (jObject.has("success")) {
                success = jObject.getBoolean("success");
            }

            if (success == true) {
                currentUser.password = newPassword;
                currentUser.save();
                mainActivity.onNavigateBack();
            } else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }


        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void logout() {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_LOGOUT, loadingMessage,
                currentUser.userId,
                currentUser.email,
                currentUser.password,
                null, null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        api.execute();
    }

    protected void logoutDone(String result) {
        try {
            if (result == null || result.isEmpty()) {
                showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
                return;
            }

            JSONObject jObject = new JSONObject(result);

            boolean success = jObject.getBoolean("success");

            if (success == true) {
                this.currentUser.password = null;
                this.currentUser.userId = null;
                this.currentUser.firstName = null;
                this.currentUser.lastName = null;
                this.caravan = null;
                this.currentUser.notifyNews = false;
                this.currentUser.notifyService = false;
                this.currentUser.save();

                mainActivity.onLoggedOut();
            } else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }

        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }


    }

    protected void sync(boolean showDialog) {

        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);

        if (showDialog == false) {
            loadingMessage = null;
        }

        MyHobbyApi api = new MyHobbyApi(API_SYNC, loadingMessage,
                currentUser.userId,
                null,
                currentUser.password,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        api.execute();
    }

    protected void syncDone(String result) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {
            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if (jObject.has("success")) {
                success = jObject.getBoolean("success");
            }

            if (success == true) {
                //mainActivity.onUserDetailsChanged();
            } else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }
        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void getDealerList(String searchQuery, String deviceCulture) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_DEALERS, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                searchQuery,
                deviceCulture,
                null,
                null,
                null,
                null);
        api.execute();

    }

    protected void getDealerListDone(String result, String searchQuery) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            DealersResult dealerResult = new Gson().fromJson(result, DealersResult.class);

            dealers = dealerResult.dealers;

            if (dealerResult.success == true) {
                System.out.println("MyHobby - return from getDealer, count=" + dealers.length);
                mainActivity.onDealersLoaded(dealers);
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }


    }

    protected void getDealer(String dealerId) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_DEALER, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                dealerId,
                null);
        api.execute();

    }

    protected void getDealerDone(String result) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            DealerResult dealerResult = new Gson().fromJson(result, DealerResult.class);

            dealer = dealerResult.dealer;

            if (dealerResult.success == true) {
                System.out.println("MyHobby - return from getDealer, count=" + dealer.name);
                mainActivity.onDealerLoaded(dealer);
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }

    }

    protected void getCampingList(String searchQuery, String deviceCulture) {
//        List<Camping> campingsFromDb = Camping.listAll(Camping.class);
//        List<FacilityOption> facilityOptionsFromDb = FacilityOption.listAll(FacilityOption.class);
//        List<Facility> facilitiesFromDb = Facility.listAll(Facility.class);
//        List<Accommodation> accommodationsFromDb = Accommodation.listAll(Accommodation.class);

        long foundCampings = Camping.count(Camping.class, null, null, null, null, null);
        long foundFacilityOptions = FacilityOption.count(FacilityOption.class, null, null, null, null, null);
        long foundFacilities = Facility.count(Facility.class, null, null, null, null, null);
        long foundAccommodations = Accommodation.count(Accommodation.class, null, null, null, null, null);

        if (foundCampings == 0 || foundFacilityOptions == 0 || foundFacilities == 0 || foundAccommodations == 0) {
            String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_loading_title);
            MyHobbyApi api = new MyHobbyApi(API_CAMPINGS, loadingMessage,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    searchQuery,
                    deviceCulture,
                    null,
                    null,
                    null,
                    null);
            api.execute();

        } else {
            LoadCampingsFromDbAsync task = new LoadCampingsFromDbAsync();
            task.execute();
        }

    }

    protected void getCampingListDone(String result, String searchQuery) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            CampingsResult campingsResult = new Gson().fromJson(result, CampingsResult.class);
            loadedCampings = campingsResult.campings;
            campingFacilityOptions = campingsResult.campingFacilityOptions;

            if (campingsResult.success == true) {
                System.out.println("MyHobby - return from getDealer, count=" + loadedCampings.size());

                UpdateDb task = new UpdateDb(loadedCampings, campingFacilityOptions);

                task.execute();
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void getFaqList(String searchQuery, String deviceCulture, String tags) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_FAQS, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                searchQuery,
                deviceCulture,
                tags,
                null,
                null,
                null);
        api.execute();
    }

    protected void getFaqListDone(String result, String searchQuery) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            FaqResult faqResult = new Gson().fromJson(result, FaqResult.class);

            faqs = faqResult.faqs;

            if (faqResult.success == true) {
                System.out.println("MyHobby - return from getFaq, count=" + faqs.length);
                mainActivity.onFaqsLoaded(faqs);
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }


    }

    protected void getMessageList() {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_MESSAGES, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        api.execute();
    }

    protected void getMessageListDone(String result) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            MessageResult messageResult = new Gson().fromJson(result, MessageResult.class);

            messages = messageResult.messages;

            if (messageResult.success == true) {
                System.out.println("MyHobby - return from getMesssageList, count=" + messages.length);
                mainActivity.onMessagesLoaded(messages);
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void setMessageAsRead (String messageId) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_SET_MESSAGE_AS_READ, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                messageId);
        api.execute();

    }

    protected void setMessageAsReadDone(String result) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            MessageResult messageResult = new Gson().fromJson(result, MessageResult.class);

            if (messageResult.success == true) {
                System.out.println("MyHobby - return from setMessageAsReadDone, count=" + messageResult.success );
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }

    }

    protected void removeMessage (String messageId) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_REMOVE_MESSAGE, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                messageId);
        api.execute();

    }

    protected void removeMessageDone(String result) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            MessageResult messageResult = new Gson().fromJson(result, MessageResult.class);

            if (messageResult.success == true) {
                System.out.println("MyHobby - return from removeMessage, result success =" + messageResult.success );
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }

    }

    protected void connectToService(String vin) {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_SERVICE, loadingMessage,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                vin,
                null,
                null);
        api.execute();
    }

    protected void connectToServiceDone(String result) {
        if (result == null || result.isEmpty()) {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {
            ServiceResult serviceResult = new Gson().fromJson(result, ServiceResult.class);

            if (serviceResult.success == true) {
                caravan = serviceResult.caravan;
                System.out.println("MyHobby - return from connectToService, count=" + caravan.serviceEntries.size());
                ServiceBookConnectedFragment fragment = new ServiceBookConnectedFragment();
                fragment.caravan = caravan;
                mainActivity.onNavigateToFragment(fragment);
            } else {
                this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.caravan_not_found));
            }

        } catch (Exception e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    private class MyHobbyApi extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        private int apiMethod;
        private String loadingMessage = null;
        private String userId = null;
        private String email = null;
        private String password = null;
        private String firstName = null;
        private String lastName = null;
        private String oldPassword = null;
        private String newPassword = null;
        private String newPasswordConfirm = null;
        private String searchQuery = null;
        private String deviceCulture = null;
        private String faqTags = null;
        private String vin = null;
        private String dealerId = null;
        private String messageId = null;

        public MyHobbyApi(int apiMethod,
                          String loadingMessage,
                          String userId,
                          String email,
                          String password,
                          String firstName,
                          String lastName,
                          String oldPassword,
                          String newPassword,
                          String newPasswordConfirm,
                          String searchQuery,
                          String deviceCulture,
                          String faqTags,
                          String vin,
                          String dealerId,
                          String messageId) {
            this.apiMethod = apiMethod;
            this.loadingMessage = loadingMessage;
            this.userId = userId;
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.newPasswordConfirm = newPasswordConfirm;
            this.searchQuery = searchQuery;
            this.deviceCulture = deviceCulture;
            this.faqTags = faqTags;
            this.vin = vin;
            this.dealerId = dealerId;
            this.messageId = messageId;
        }

        @Override
        protected void onPreExecute() {
            if (loadingMessage != null) {
                pDialog = new ProgressDialog(mainActivity.getContext());
                pDialog.setMessage(loadingMessage);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String apiUrl = null;
            InputStream in = null;
            Uri.Builder builder = null;

            try {
                switch (apiMethod) {
                    case API_REGISTER: {
                        apiUrl = baseUrl + "createAccount";

                        builder = new Uri.Builder()
                                .appendQueryParameter("Email", email)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("FirstName", firstName)
                                .appendQueryParameter("LastName", lastName)
                                .appendQueryParameter("Culture", Locale.getDefault().getLanguage());
                    }
                    break;

                    case API_CHANGE_PASSWORD: {
                        apiUrl = baseUrl + "changePasswordApp";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("Id", userId)
                                .appendQueryParameter("OldPassword", oldPassword)
                                .appendQueryParameter("NewPassword", newPassword)
                                .appendQueryParameter("ConfirmPassword", newPasswordConfirm);
                    }
                    break;
                    case API_LOGIN: {
                        apiUrl = baseUrl + "loginapi";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserName", email)
                                .appendQueryParameter("Password", password);

                    }
                    break;
                    case API_LOGOUT: {
                        apiUrl = baseUrl + "logoutapi";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password);

                    }
                    break;
                    case API_SYNC: {
                        apiUrl = baseUrlAndroid + "androidSync";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("FirstName", MyHobbyMarket.getInstance().currentUser.firstName)
                                .appendQueryParameter("LastName", MyHobbyMarket.getInstance().currentUser.lastName)
                                .appendQueryParameter("Email", MyHobbyMarket.getInstance().currentUser.email)
                                .appendQueryParameter("SubNews", MyHobbyMarket.getInstance().currentUser.notifyNews ? "true" : "false")
                                .appendQueryParameter("SubService", MyHobbyMarket.getInstance().currentUser.notifyService ? "true" : "false")
                                .appendQueryParameter("DealerId", MyHobbyMarket.getInstance().currentUser.dealerId)
                                .appendQueryParameter("WorkshopId", MyHobbyMarket.getInstance().currentUser.workshopId)
                                .appendQueryParameter("DeviceToken", MyHobbyMarket.getInstance().currentUser.deviceToken);
                    }
                    break;
                    case API_MESSAGES: {
                        if (isUserLoggedIn()) {
                            apiUrl = baseUrlAndroid + "messageListAuth";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("userId", currentUser.userId);
                        }
                    }
                    break;
                    case API_FAQS: {
                        if (isUserLoggedIn()) {
                            apiUrl = baseUrlAndroid + "faqListAuth";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture)
                                    .appendQueryParameter("Tags", faqTags);
                        } else {
                            apiUrl = baseUrlAndroid + "faqList";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture)
                                    .appendQueryParameter("Tags", faqTags);
                        }

                    }
                    break;
                    case API_DEALERS: {
                        if (isUserLoggedIn()) {
                            apiUrl = baseUrlAndroid + "dealerListAuth";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture);
                        } else {
                            apiUrl = baseUrlAndroid + "dealerList";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture);
                        }

                    }
                    break;
                    case API_DEALER: {
                        apiUrl = baseUrlAndroid + "dealer";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserName", currentUser.email)
                                .appendQueryParameter("Password", currentUser.password)
                                .appendQueryParameter("DealerId", dealerId);
                    }
                    break;
                    case API_CAMPINGS: {
                        if (isUserLoggedIn()) {
                            apiUrl = baseUrlAndroid + "campingListAuth";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture);
                        } else {
                            apiUrl = baseUrlAndroid + "campingList";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture);
                        }

                    }
                    break;
                    case API_SERVICE: {
                        if (isUserLoggedIn()) {
                            apiUrl = baseUrl + "syncServices";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("Vin", vin);
                        }
                    }
                    break;
                    case API_SET_MESSAGE_AS_READ: {
                        apiUrl = baseUrlAndroid + "markMessageAsRead";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserName", currentUser.email)
                                .appendQueryParameter("Password", currentUser.password)
                                .appendQueryParameter("messageId", messageId);
                    }
                    break;
                    case API_REMOVE_MESSAGE: {
                        apiUrl = baseUrlAndroid + "removeMessage";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserName", currentUser.email)
                                .appendQueryParameter("Password", currentUser.password)
                                .appendQueryParameter("messageId", messageId);
                    }
                    break;

                }


                System.out.println("MyHobby - connect to:" + apiUrl);
                URL url = new URL(apiUrl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(30000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.connect();

                String query = builder.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                int HttpResult = urlConnection.getResponseCode();
                StringBuilder sb = new StringBuilder();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    result = sb.toString();

                } else {
                    String errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_internal);
                    boolean logout = false;

                    if (HttpResult == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_invalid_credentials);

                        // Wrong credentials when asumed to be correct. Logout the user and remove any credentials
                        currentUser.password = null;
                        currentUser.userId = null;
                        currentUser.firstName = null;
                        currentUser.lastName = null;
                        currentUser.save();
                        logout = true;

                    } else if (HttpResult == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                        // Get the message from server
                        String json = "";
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "utf-8"));
                        String line = null;

                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        json = sb.toString();

                        JSONObject jObject = new JSONObject(json);

                        boolean success = true;

                        if (jObject.has("message")) {
                            JSONArray array = jObject.getJSONArray("message");
                            errorMessage = array.getString(0);
                        }
                    }

                    if (logout == true && apiMethod != API_LOGIN) {
                        result = "{\"success\": \"false\", \"logout\": \"true\", \"message\": \"" + errorMessage + "\"}";
                    } else {
                        result = "{\"success\": \"false\", \"message\": \"" + errorMessage + "\"}";
                    }
                }

            } catch (SocketTimeoutException ex) {
                String errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_no_response);
                result = "{\"success\": \"false\", \"message\": \"" + errorMessage + "\"}";
            } catch (Exception e) {
                String errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_internal);
                result = "{\"success\": \"false\", \"message\": \"" + errorMessage + "\"}";
            }

            return result;
        }

        protected void onPostExecute(String result) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            try {
                JSONObject jObject = new JSONObject(result);

                boolean logout = false;

                if (jObject.has("logout")) {
                    logout = jObject.getBoolean("logout");
                }

                if (logout == true) {
                    mainActivity.onLoggedOut();
                }

            } catch (JSONException e) {

            }

            switch (apiMethod) {
                case API_REGISTER:
                    registerDone(result, email, password, firstName, lastName);
                    break;
                case API_CHANGE_PASSWORD:
                    changePasswordDone(result, oldPassword, newPassword, newPasswordConfirm);
                    break;
                case API_LOGIN:
                    loginDone(result, email, password);
                    break;
                case API_LOGOUT:
                    logoutDone(result);
                    break;
                case API_SYNC:
                    syncDone(result);
                    break;
                case API_MESSAGES:
                    getMessageListDone(result);
                    break;
                case API_FAQS:
                    getFaqListDone(result, searchQuery);
                    break;
                case API_DEALERS:
                    getDealerListDone(result, searchQuery);
                    break;
                case API_DEALER:
                    getDealerDone(result);
                    break;
                case API_CAMPINGS:
                    getCampingListDone(result, searchQuery);
                    break;
                case API_SERVICE:
                    connectToServiceDone(result);
                    break;
                case API_SET_MESSAGE_AS_READ:
                    setMessageAsReadDone(result);
                    break;
                case API_REMOVE_MESSAGE:
                    removeMessageDone(result);
                    break;
            }
        }

    } // end CallAPI

}
