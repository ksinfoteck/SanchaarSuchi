package in.kamalaenterprize.sncharsuchi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.LocaleHelper;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.Tools;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.commonutility.WebServiceWithoutDialog;
import in.kamalaenterprize.notification.app.Config;
import in.kamalaenterprize.notification.util.NotificationUtils;

public class ActivityMain extends AppCompatActivity implements WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private boolean isInternerPresent;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private RelativeLayout layConnection, progressbarInternet;
    private TextView textError;

    public static final String TAG_RESULT = "result";
    public static final String TAG_LOGINDETAIL = "logindetail";
    public static final String TAG_YODDHAID = "yoddhaId";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";
    public static final String TAG_STATE = "state";

    public static final String TAG_HOME         = "Home";
    public static final String TAG_CONTACTHIS   = "Add Contact History";
    public static final String TAG_TRACECON     = "Trace Contacts";
    public static final String TAG_MAKEALERT    = "Make Alert";
    public static final String TAG_HISOFFLINE   = "History Offline";
    public static final String TAG_SHAREAPP     = "Share App";
    public static final String TAG_RATEAPP      = "Rate App";
    public static final String TAG_FAQ          = "FAQ";
    public static final String TAG_SCANQR       = "ScanQR Code";
    public static final String TAG_GENERATEQR   = "Generate QR Code";
    public static final String TAG_ABOUT        = "About";
    public static final String TAG_LOGOUT       = "Logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.act_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        svContext = this;
        hideKeyboard();
        gd = new GlobalData(this);
        isInternerPresent = gd.isConnectingToInternet();

        FirebaseApp.initializeApp(svContext);

        layConnection = (RelativeLayout) findViewById(R.id.lay_connection);
        progressbarInternet = (RelativeLayout) findViewById(R.id.lay_dialog);
        textError = (TextView) findViewById(R.id.text_error);

        progressbarInternet.setVisibility(View.INVISIBLE);
        textError.setVisibility(View.VISIBLE);

        layConnection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textError.setVisibility(View.INVISIBLE);
                progressbarInternet.setVisibility(View.VISIBLE);

                if (gd.isConnectingToInternet()) {
                    hideNoConnectionError();
                    switchContent(new FragDashboard());
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textError.setVisibility(View.VISIBLE);
                            progressbarInternet.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                    showErrorMessage(svContext, GlobalVariables.ERRORIDINTERNETNOTAVAILABLE, "");
                }
            }
        });


        if((PreferenceConnector.readString(svContext, PreferenceConnector.LANGUAGE, "").equalsIgnoreCase("hi"))){
            LocaleHelper.setLocale(getApplicationContext(), "hi");
        }else if(PreferenceConnector.readString(svContext, PreferenceConnector.LANGUAGE, "").equalsIgnoreCase("ma")){
            LocaleHelper.setLocale(getApplicationContext(), "ma");
        }else {
            LocaleHelper.setLocale(getApplicationContext(), "en");
        }
        LocaleHelper.setLocale(getApplicationContext(), "en");

        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISNOTIFICATION, false)) {
            Intent svIntent = new Intent(svContext, ActivityNotification.class);
            startActivity(svIntent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

//        customizeActionBar("DashBoard", false, false);
        //		setUpMenuBar();
//        if (isInternerPresent) {
//            hideNoConnectionError();
        switchContent(new FragDashboard());
//        } else {
//            showErrorMessage(svContext, GlobalVariables.ERRORIDINTERNETNOTAVAILABLE, "");
//        }


    }



    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebServiceWithoutDialog webService = new WebServiceWithoutDialog(svContext, postUrl, hash, this, WebService.POST);
        webService.execute();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);

        if (url.contains(GlobalVariables.LOGIN)) {

            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString(TAG_RESULT);
                if (str_result.equals("success")) {
                    if (json.has(TAG_LOGINDETAIL)) {
                        JSONObject logindetail_obj = json.getJSONObject(TAG_LOGINDETAIL);
                        String str_yoddhaid = logindetail_obj.getString(TAG_YODDHAID);
//                        String str_pin = logindetail_obj.getString(TAG_PIN);
                        String str_phone = logindetail_obj.getString(TAG_PHONE);
                        String str_name = logindetail_obj.getString(TAG_NAME);
                        String str_id = logindetail_obj.getString(TAG_ID);
                        String str_state = logindetail_obj.getString(TAG_STATE);
                        String str_Gender = logindetail_obj.getString(TAG_GENDER);

                        String str_alert = "";
                        str_alert = logindetail_obj.getString("alert");

                        String str_userid = "";
                        str_userid = logindetail_obj.getString("id");
//                        String str_email = logindetail_obj.getString(TAG_EMAIL);

                        String str_Profilepic = "";
                        if (logindetail_obj.has("image")) {
                            str_Profilepic = logindetail_obj.getString("image");
                        }

                        String adminfcm_id = logindetail_obj.getString("adminfcm_id");

                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERID, str_userid);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPHONE, str_phone);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.YODDHAID, str_yoddhaid);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINNAME, str_name);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINID, str_id);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINSTATE, str_state);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINGENDER, str_Gender);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.ISALERT, str_alert);
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPROFILEPIC, str_Profilepic);

                        PreferenceConnector.writeString(svContext, PreferenceConnector.ADMINLOGINFCMID, adminfcm_id);

                        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);

//                        Intent svIntent;
//                        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.PASSCODESET, false)) {
//                            svIntent = new Intent(svContext, VerificationPasscode.class);
//                            startActivity(svIntent);
//                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                            finish();
//                        } else {
//                            svIntent = new Intent(svContext, ActivityMain.class);
//                            startActivity(svIntent);
//                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                            finish();
//                        }
                    }
                } else {
                    Logout();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        String strAlertStatus = PreferenceConnector.readString(svContext, PreferenceConnector.ISALERT, "0");
//        if (strAlertStatus.equalsIgnoreCase("1")) {
//            if (! strAlertShow.equalsIgnoreCase("2")) {
//                Intent svIntent = new Intent(svContext, ActivityAlert.class);
//                startActivity(svIntent);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            }
//        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.app_name));

        ImageButton toolbarNoti = (ImageButton) toolbar.findViewById(R.id.toolbar_notification);
        toolbarNoti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent svIntent = new Intent(svContext, ActivityNotification.class);
                startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        ImageButton toolbarLang = (ImageButton) toolbar.findViewById(R.id.toolbar_language);
        toolbarLang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openLanguageDialog();
            }
        });

        Tools.setSystemBarColor(this);
    }

    private void openLanguageDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_language, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);

        TextView lEnglish = v.findViewById(R.id.l_english);
        TextView lHindi = v.findViewById(R.id.l_hindi);
        TextView lMarathi = v.findViewById(R.id.l_marathi);

        final AlertDialog dialog = builder.create();

        lEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleHelper.setLocale(getApplication(), "en");
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                PreferenceConnector.writeString(svContext, PreferenceConnector.LANGUAGE, "en");

                recreate();
                dialog.dismiss();
            }
        });

        lHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleHelper.setLocale(getApplication(), "hi");
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                PreferenceConnector.writeString(svContext, PreferenceConnector.LANGUAGE, "hi");


                recreate();
                dialog.dismiss();
            }
        });

        lMarathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleHelper.setLocale(getApplication(), "ma");
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                PreferenceConnector.writeString(svContext, PreferenceConnector.LANGUAGE, "ma");

                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                String menuValue = item.getTitle().toString();
                Intent svIntent;

                if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_home)) || menuValue.equalsIgnoreCase(TAG_HOME)) {
                    switchContent(new FragDashboard());
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_addcontacthistory)) || menuValue.equalsIgnoreCase(TAG_CONTACTHIS)) {
                    svIntent = new Intent(svContext, ActivityAddTravelHistory.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_addreport)) || menuValue.equalsIgnoreCase(TAG_MAKEALERT)) {
                    svIntent = new Intent(svContext, ActivityAddReport.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_contacthistory)) || menuValue.equalsIgnoreCase(TAG_TRACECON)) {
                    svIntent = new Intent(svContext, ActivityShowTravelHistory.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_historyoffline)) || menuValue.equalsIgnoreCase(TAG_HISOFFLINE)) {
                    svIntent = new Intent(svContext, ActivityShowTravelHistoryOffline.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_share)) || menuValue.equalsIgnoreCase(TAG_SHAREAPP)) {
                    shareApp();
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_rate)) || menuValue.equalsIgnoreCase(TAG_RATEAPP)) {
                    reviewOnApp();
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_scanqrcode)) || menuValue.equalsIgnoreCase(TAG_SCANQR)) {
                    svIntent = new Intent(svContext, ScannedBarcodeActivity.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_generateqrcode)) || menuValue.equalsIgnoreCase(TAG_GENERATEQR)) {
                    svIntent = new Intent(svContext, QRCodeGenerator.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }  else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_help)) || menuValue.equalsIgnoreCase(TAG_FAQ)) {
                    svIntent = new Intent(svContext, ActivityHelp.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_about)) || menuValue.equalsIgnoreCase(TAG_ABOUT)) {
                    svIntent = new Intent(svContext, ActivityAbout.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else if (menuValue.equalsIgnoreCase(getResources().getString(R.string.menu_logout)) || menuValue.equalsIgnoreCase(TAG_LOGOUT)) {
                    Logout();
                }

                hideKeyboard();
                drawer.closeDrawers();
                return true;
            }
        });
//        View child = getLayoutInflater().inflate(R.layout.navigation_footer, null);
//        nav_view.addView(child);
//        open drawer at start
//        drawer.openDrawer(GravityCompat.START);
    }

    private void Logout() {
        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, false);
//        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.PASSCODESET, false);

        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERID, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPHONE, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.YODDHAID, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINNAME, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINID, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINSTATE, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINGENDER, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.ISALERT, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPROFILEPIC, "");
        PreferenceConnector.writeString(svContext, PreferenceConnector.ADMINLOGINFCMID, "");

        Intent svIntent = new Intent(svContext, ActivityLogin.class);
        startActivity(svIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Sanchar Suchi application from " + " http://play.google.com/store/apps/details?id=" + this.getPackageName() + " ");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private void updateFCMId() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    //                    txtMessage.setText(message);
                }
            }
        };

        String refreshedToken = "er";
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (refreshedToken.equals("")) {
            GlobalData.showToast("No fcmid to update", svContext);
        } else {
            if (gd.isConnectingToInternet()) {
//                String fcmId = PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, "");

                String[] keys = {"phone", "fcm_id"};
                String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.OTPPHONENUMBER, ""), refreshedToken};

                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                for (int j = 0; j < keys.length; j++) {
                    System.out.println(keys[j] + "......." + value[j]);
                    hash.put(keys[j], value[j]);
                }

//                if (!fcmId.equals("")) {
                callWebService(GlobalVariables.LOGIN, hash, "");
//                }
            } else {
                ShowCustomView.showInternetAlert(svContext);
            }
        }
    }

//    public void customizeActionBar(String strText, boolean isBackVisible, boolean isHomeVisible) {
//        TextView titleTextView  = (TextView) findViewById(R.id.title_text);
//        ImageView imgBack       = (ImageView) findViewById(R.id.img_back);
//        ImageView imgHome       = (ImageView) findViewById(R.id.img_home);
//
//        titleTextView.setText(strText);
//
//        if (isBackVisible) {
//            imgBack.setVisibility(View.VISIBLE);
//        }else {
//            imgBack.setVisibility(View.INVISIBLE);
//        }
//
//        if (isHomeVisible) {
//            imgHome.setVisibility(View.VISIBLE);
//        }else {
//            imgHome.setVisibility(View.INVISIBLE);
//        }
//
//
//        imgBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customizeActionBar("DashBoard", false, false);
//                switchBack();
//            }
//        });
//
//        imgHome.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customizeActionBar("DashBoard", false, false);
//                switchContent(new FragDashboard());
//            }
//        });
//    }

    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public void showErrorMessage(Context aiContext, int errorId, String strValue) {
        layConnection.setVisibility(View.VISIBLE);

        textError.setVisibility(View.VISIBLE);
        progressbarInternet.setVisibility(View.INVISIBLE);
        if (errorId == GlobalVariables.CUSTOMIZEERROR) {
            textError.setText(strValue);
        } else {
            if (errorId == GlobalVariables.ERRORIDNOTGETTINGDATAFROMSERVER) {
                textError.setText(GlobalData.getStringRes(aiContext, R.string.errorgettingdatafromserver));
            } else if (errorId == GlobalVariables.ERRORIDINTERNETNOTAVAILABLE) {
                textError.setText(GlobalData.getStringRes(aiContext, R.string.errorsplashinternetnotavailable));
            }
        }

        if (errorId == 1) {
        }
    }

    public void hideNoConnectionError() {
        layConnection.setVisibility(View.GONE);
        textError.setVisibility(View.INVISIBLE);
        progressbarInternet.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    public void switchBack() {
        hideKeyboard();
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
//        FragmentManager fragmentManager = getFragmentManager();
        //		if(fragmentManager.getBackStackEntryCount()>1) {
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStack();
////            customizeActionBar("DashBoard", false, false);
//        } else {
//            finish();
//        }

        finish();
    }

    public void switchContent(Fragment fragment, String tag) {
        hideKeyboard();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void switchContent(Fragment fragment) {
        hideKeyboard();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        cancelNoti();
        //        this.registerReceiver(mMessageReceiver, new IntentFilter("com.skwebsoft.mainapp"));
        initToolbar();
        initNavigationMenu();
        updateFCMId();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
}
