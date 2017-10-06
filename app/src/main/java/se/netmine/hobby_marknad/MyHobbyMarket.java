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
import com.google.gson.Gson;
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
import java.util.Locale;


/**
 * Created by jusuf on 2017-09-28.
 */

public class MyHobbyMarket {

    private static final int  API_REGISTER = 0;
    private static final int  API_CONNECT = 1;
    private static final int API_LOGIN = 2;
    private static final int API_LOGOUT = 3;
    private static final int API_TELL = 4;
    private static final int API_SYNC = 5;
    private static final int API_CHANGE_PASSWORD = 6;
    private static final int API_FAQS = 7;

//    public static  String url = "https://admin.myhobby.nu/";
    public static String url = "http://192.168.20.183/hobby/";
//    public static String url = "http://192.168.0.12/hobby/";
    public static String baseUrl = url + "api/myhobby/";

    public User currentUser = null;
    public IMainActivity mainActivity;
    public Faq[] faqs;

    private static MyHobbyMarket ourInstance = new MyHobbyMarket();

    public static MyHobbyMarket getInstance() {
        return ourInstance;
    }

    private MyHobbyMarket() {
        currentUser = new User();
    }

    public void init(SharedPreferences settings)
    {
        this.currentUser.init(settings);
    }

    public boolean isUserLoggedIn()
    {
        return this.currentUser.userId != null &&
                this.currentUser.password != null &&
                this.currentUser.userId.isEmpty() == false &&
                this.currentUser.password.isEmpty() == false;
    }

    public String getEmail()
    {
        return this.currentUser.email;
    }

    public String getFirstName()
    {
        return this.currentUser.firstName;
    }

    public String getLastName()
    {
        return this.currentUser.lastName;
    }

    public void setDeviceToken(String deviceToken)
    {
        this.currentUser.deviceToken = deviceToken;
        this.currentUser.save();
    }

    private void showErrorDialog(String message)
    {
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

    protected void register(String email, String password, String firstName, String lastName)
    {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_REGISTER, loadingMessage, null, email, password, null, null, firstName, lastName, null, null, null, null, null, null);
        api.execute();
    }

    protected void registerDone(String result, String email, String password, String firstName, String lastName)
    {
        if(result == null || result.isEmpty())
        {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {
            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if(jObject.has("success"))
            {
                success = jObject.getBoolean("success");
            }

            if(success == true)
            {
                mainActivity.onRegistered();
            }
            else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }
        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void login(String email, String password)
    {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_LOGIN, loadingMessage, null, email, password, null, null, null, null, null, null, null, null, null, null);
        api.execute();
    }

    protected void loginDone(String result, String email, String password)
    {
        if(result == null || result.isEmpty())
        {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {
            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if(jObject.has("success"))
            {
                success = jObject.getBoolean("success");
            }

            if(success == true)
            {
                String userId = jObject.getString("userId");

                currentUser.email = email;
                currentUser.password = password;
                currentUser.userId = userId;
                currentUser.save();
                mainActivity.onLoggedIn();
            }
            else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }


        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }


    protected void changePassword(String oldPassword, String newPassword, String newPasswordConfirm)
    {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_CHANGE_PASSWORD, loadingMessage, currentUser.userId, null, currentUser.password, null, null, null, null, oldPassword, newPassword, newPasswordConfirm, null, null, null);
        api.execute();
    }

    protected void changePasswordDone(String result, String oldPassword, String newPassword, String newPasswordConfirm)
    {
        try
        {
            if(result == null || result.isEmpty())
            {
                result = "{\"success\": \"true\"}";
            }


            JSONObject jObject = new JSONObject(result);

            boolean success = true;

            if(jObject.has("success"))
            {
                success = jObject.getBoolean("success");
            }

            if(success == true)
            {
                currentUser.password = newPassword;
                currentUser.save();
                mainActivity.onNavigateBack();
            }
            else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }


        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }
    }

    protected void logout()
    {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_LOGOUT, loadingMessage, currentUser.userId, null, currentUser.password, null, null, null, null, null, null, null, null, null, null);
        api.execute();
    }

    protected void logoutDone(String result)
    {
        try
        {
            if(result == null || result.isEmpty())
            {
                showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
                return;
            }

            JSONObject jObject = new JSONObject(result);

            boolean success = jObject.getBoolean("success");

            if(success == true)
            {
                this.currentUser.password = null;
                this.currentUser.myHobbyKey = null;
                this.currentUser.userId = null;
                this.currentUser.firstName = null;
                this.currentUser.lastName = null;
                this.currentUser.save();
                mainActivity.onLoggedOut();
            }
            else {
                String message = jObject.getString("message");
                this.showErrorDialog(message);
            }

        } catch (JSONException e) {
            this.showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_internal));
        }


    }

    protected void getFaqList(String searchQuery, String deviceCulture, String tags)
    {
        String loadingMessage = mainActivity.getContext().getResources().getString(R.string.app_send_command_messsage);
        MyHobbyApi api = new MyHobbyApi(API_FAQS, loadingMessage,null, null, null, null, null, null, null, null, null, null, searchQuery, deviceCulture, tags);
        api.execute();
    }

    protected void getFaqListDone(String result, String searchQuery)
    {
        if(result == null || result.isEmpty())
        {
            showErrorDialog(mainActivity.getContext().getResources().getString(R.string.app_error_no_response));
            return;
        }

        try {

            FaqResult faqResult = new Gson().fromJson(result, FaqResult.class);

            faqs = faqResult.faqs;

            if(faqResult.success == true) {
                System.out.println("MyHobby - return from getFaq, count=" + faqs.length);
                mainActivity.onFaqsLoaded(faqs);
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
        private String myhobbyKey = null;
        private String command = null;
        private String firstName = null;
        private String lastName = null;
        private String oldPassword = null;
        private String newPassword = null;
        private String newPasswordConfirm = null;
        private String searchQuery = null;
        private String deviceCulture = null;
        private String faqTags = null;

        public MyHobbyApi(int apiMethod,
                          String loadingMessage,
                          String userId,
                          String email,
                          String password,
                          String myhobbyKey,
                          String command,
                          String firstName,
                          String lastName,
                          String oldPassword,
                          String newPassword,
                          String newPasswordConfirm,
                          String searchQuery,
                          String deviceCulture,
                          String faqTags)
        {
            this.apiMethod = apiMethod;
            this.loadingMessage = loadingMessage;
            this.userId = userId;
            this.email = email;
            this.password = password;
            this.myhobbyKey = myhobbyKey;
            this.command = command;
            this.firstName = firstName;
            this.lastName = lastName;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.newPasswordConfirm = newPasswordConfirm;
            this.searchQuery = searchQuery;
            this.deviceCulture = deviceCulture;
            this.faqTags = faqTags;
        }


        @Override
        protected void onPreExecute() {
            if(loadingMessage != null)
            {
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

            try
            {
                switch (apiMethod)
                {
                    case API_REGISTER:
                    {
                        apiUrl = baseUrl + "createAccount";

                        builder = new Uri.Builder()
                                .appendQueryParameter("Email", email)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("FirstName", firstName)
                                .appendQueryParameter("LastName", lastName)
                                .appendQueryParameter("Culture", Locale.getDefault().getLanguage());
                    }
                    break;

                    case API_CHANGE_PASSWORD:
                    {
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

                    case API_CONNECT:
                    {
                        apiUrl = baseUrl + "connectmyhobby";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("MyhobbyKey", myhobbyKey);
                    }
                    break;

                    case API_LOGIN:
                    {
                        apiUrl = baseUrl + "loginapi";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserName", email)
                                .appendQueryParameter("Password", password);

                    }
                    break;
                    case API_LOGOUT:
                    {
                        apiUrl = baseUrl + "logoutapi";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password);

                    }
                    break;
                    case API_TELL:
                    {
                        apiUrl = baseUrl + "tell";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("MyhobbyKey", myhobbyKey)
                                .appendQueryParameter("Command", command);
                    }
                    break;
                    case API_SYNC:
                    {
                        apiUrl = baseUrl + "androidsync";

                        builder = new Uri.Builder()
                                .appendQueryParameter("UserId", userId)
                                .appendQueryParameter("Password", password)
                                .appendQueryParameter("MyhobbyKey", myhobbyKey)
                                .appendQueryParameter("FirstName", MyHobbyMarket.getInstance().currentUser.firstName)
                                .appendQueryParameter("LastName", MyHobbyMarket.getInstance().currentUser.lastName)
                                .appendQueryParameter("Email", MyHobbyMarket.getInstance().currentUser.email)
                                .appendQueryParameter("SubPower", MyHobbyMarket.getInstance().currentUser.notifyBattery ? "true" : "false")
                                .appendQueryParameter("SubTemp", MyHobbyMarket.getInstance().currentUser.notifyTemp ? "true" : "false")
                                .appendQueryParameter("SubWater", MyHobbyMarket.getInstance().currentUser.notifyWater  ? "true" : "false")
                                .appendQueryParameter("SubPosition", MyHobbyMarket.getInstance().currentUser.notifyPosition  ? "true" : "false")
                                .appendQueryParameter("SubNews", MyHobbyMarket.getInstance().currentUser.notifyNews ? "true" : "false")
                                .appendQueryParameter("DeviceToken", MyHobbyMarket.getInstance().currentUser.deviceToken);

                    }
                    break;
                    case API_FAQS:
                    {
                        if (isUserLoggedIn()){
                            apiUrl = baseUrl + "faqListAuth";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture)
                                    .appendQueryParameter("tags", faqTags);
                        }
                        else{
                            apiUrl = baseUrl + "faqList";

                            builder = new Uri.Builder()
                                    .appendQueryParameter("UserName", currentUser.email)
                                    .appendQueryParameter("Password", currentUser.password)
                                    .appendQueryParameter("SearchQuery", searchQuery)
                                    .appendQueryParameter("DeviceCulture", deviceCulture)
                                    .appendQueryParameter("tags", faqTags);
                        }

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

                if(HttpResult == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    result = sb.toString();

                }
                else
                {
                    String errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_internal);
                    boolean logout = false;

                    if(HttpResult == HttpURLConnection.HTTP_UNAUTHORIZED)
                    {
                        errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_invalid_credentials);

                        // Wrong credentials when asumed to be correct. Logout the user and remove any credentials
                        currentUser.password = null;
                        currentUser.userId = null;
                        currentUser.myHobbyKey = null;
                        currentUser.firstName = null;
                        currentUser.lastName = null;
                        currentUser.save();
                        logout = true;

                    }
                    else if(HttpResult == HttpURLConnection.HTTP_INTERNAL_ERROR)
                    {
                        // Get the message from server
                        String json = "";
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(),"utf-8"));
                        String line = null;

                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        json = sb.toString();

                        JSONObject jObject = new JSONObject(json);

                        boolean success = true;

                        if(jObject.has("message"))
                        {
                            JSONArray array = jObject.getJSONArray("message");
                            errorMessage = array.getString(0);
                        }
                    }

                    if(logout == true && apiMethod != API_LOGIN) {
                        result = "{\"success\": \"false\", \"logout\": \"true\", \"message\": \"" + errorMessage + "\"}";
                    }
                    else {
                        result = "{\"success\": \"false\", \"message\": \"" + errorMessage + "\"}";
                    }
                }

            }
            catch(SocketTimeoutException ex)
            {
                String errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_no_response);
                result =  "{\"success\": \"false\", \"message\": \"" + errorMessage + "\"}";
            }
            catch (Exception e )
            {
                String errorMessage = mainActivity.getContext().getResources().getString(R.string.app_error_internal);
                result =  "{\"success\": \"false\", \"message\": \"" + errorMessage + "\"}";
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

                if(jObject.has("logout"))
                {
                    logout = jObject.getBoolean("logout");
                }

                if(logout == true)
                {
                    mainActivity.onLoggedOut();
                }

            } catch (JSONException e) {

            }

            switch (apiMethod)
            {
                case API_REGISTER:
                    registerDone(result, email, password, firstName, lastName);
                    break;
                case API_CHANGE_PASSWORD:
                    changePasswordDone(result, oldPassword, newPassword, newPasswordConfirm);
                    break;
//                case API_CONNECT:
//                    connectDone(result);
//                    break;
                case API_LOGIN:
                    loginDone(result, email, password);
                    break;
                case API_LOGOUT:
                    logoutDone(result);
                    break;
//                case API_TELL:
//                    tellDone(result);
//                    break;
//                case API_SYNC:
//                    syncDone(result);
//                    break;
                case API_FAQS:
                    getFaqListDone(result, searchQuery);
                    break;
            }
        }

    } // end CallAPI

}
