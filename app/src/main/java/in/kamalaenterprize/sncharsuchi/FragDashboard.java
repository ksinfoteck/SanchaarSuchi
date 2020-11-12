package in.kamalaenterprize.sncharsuchi;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.adapter.TravelHistoryAdapter;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.SpinnerPopulateAdapter;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.database.DatabaseHandler;
import in.kamalaenterprize.model.TravelHistoryModel;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class FragDashboard extends Fragment implements OnClickListener, WebServiceListener {
    private static final int COUNTSHOWTRAVELHOME = 5;
    private Context svContext;
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private GlobalData gd;
    private TextView txtYoddaId, txtZone, txtConfirmed, txtConfirmedToday, txtActive, txtRecovered, getTxtRecoveredToday, txtDeceased, txtDeceasedToday;
    private LinearLayout txtSeeMore, txtGenerateQrCode;
    private ImageView imgAddHistory;
    private RecyclerView rvTravelHistory;
    private List<TravelHistoryModel> lstData = new ArrayList<TravelHistoryModel>();
    private TravelHistoryAdapter mAdapter;
    private TextView txtNoData;
    private LinearLayout cardMemberid, layAdmin;
    private ImageView imgProfile;
    private Slider slider;
    public static List<Slide> slideList = new ArrayList<>();
    private int previousLength;
    private boolean backSpace;
    private List<Slide> slideListLocal = new ArrayList<>();
    public static final String TAG_TRAVEL_HISTORY = "travel_history";
    public static final String TAG_DATE = "date";
    public static final String TAG_LOCATIONLAT = "locationLat";
    public static final String TAG_TRAVELTYPE = "travelType";
    public static final String TAG_BANNERS = "banners";
    public static final String TAG_BANNER_ID = "banner_id";
    public static final String TAG_BANNER_URL = "banner_url";
    public static final String TAG_BANNER_IMAGE = "banner_image";
    public static final String TAG_STATUS = "status";
    public static final String TAG_NAME = "name";

    private EditText edPersonName, edPersonAddress, edPrimaryContact, edPersonEmail, edNote;
    private TextView edDate, edTime;
    private Button btnsignUp;
    private Typeface font;
    private String strPersonName, strPersonAddress, strPrimaryContact, strPersonEmail, strNote;
    private DatabaseHandler db;
    //    private GPSTracker gps;
    private Spinner dropDownViewTravelType, dropDownViewTravelMode;
    private String latitude = "", longitude = "";
    public static List<String> lstReferUrl = new ArrayList<>();
    private String strSelectedDate, strSelectedTime = "";
    boolean isdateSelected = false, istimeSelected = false;
    private String selectedTravelTypeID = "", selectedTravelModeID = "";
    private AppCompatButton btnAddYoddhaId, btnAddFromCode;
    private EditText et_yoddhaid;
//    private IntentIntegrator qrScan;
    public static final String TAG_RESULT = "result";
    public static final String TAG_MSG = "msg";
    public static final String TAG_STATES = "states";
    public static final String TAG_CODE = "code";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_dashboard, container, false);
        }
        return aiView;
    }

    public FragDashboard() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        svContext = getActivity();
//        if (savedInstanceState == null && !mAlreadyLoaded)
        {
            mAlreadyLoaded = true;

            aiView = getView();
            gd = new GlobalData(svContext);
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
                ViewGroup root = (ViewGroup) aiView.findViewById(R.id.mylayout);
                GlobalData.setFont(root, font);
            }

//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            slider = aiView.findViewById(R.id.slider);
            txtYoddaId = (TextView) aiView.findViewById(R.id.txt_yoddhaid);
            txtZone = (TextView) aiView.findViewById(R.id.txt_zone);
            txtConfirmed = (TextView) aiView.findViewById(R.id.txt_confirmed);
            txtConfirmedToday = (TextView) aiView.findViewById(R.id.txt_confirmedtoday);
            txtActive = (TextView) aiView.findViewById(R.id.txt_active);
            txtRecovered = (TextView) aiView.findViewById(R.id.txt_recovered);
            getTxtRecoveredToday = (TextView) aiView.findViewById(R.id.txt_recoveredtoday);
            txtDeceased = (TextView) aiView.findViewById(R.id.txt_deceased);
            txtDeceasedToday = (TextView) aiView.findViewById(R.id.txt_deceasedtoday);
            txtSeeMore = (LinearLayout) aiView.findViewById(R.id.txt_seemore);
            txtGenerateQrCode = (LinearLayout) aiView.findViewById(R.id.txt_generatecode);
            txtNoData = (TextView) aiView.findViewById(R.id.txt_nodata);

            cardMemberid = (LinearLayout) aiView.findViewById(R.id.lay_yoddhaid);
            layAdmin = (LinearLayout) aiView.findViewById(R.id.lay_admin);

            imgAddHistory = (ImageView) aiView.findViewById(R.id.img_addhistory);
            rvTravelHistory = (RecyclerView) aiView.findViewById(R.id.rv_travelhistory);

            et_yoddhaid = (EditText) aiView.findViewById(R.id.text_yoddhaid);

//            et_reportnumber = (TextInputEditText) aiView.findViewById(R.id.text_testreportnumber);
//            et_labname = (TextInputEditText) aiView.findViewById(R.id.text_testlabname);

            btnAddYoddhaId = (AppCompatButton) aiView.findViewById(R.id.add_uid);
            btnAddYoddhaId.setOnClickListener(this);

            imgAddHistory.setOnClickListener(this);
            txtSeeMore.setOnClickListener(this);
            txtGenerateQrCode.setOnClickListener(this);
            layAdmin.setOnClickListener(this);

            String yoddhaId = PreferenceConnector.readString(svContext, PreferenceConnector.YODDHAID, "");
            if (!yoddhaId.equals("")) {
                txtYoddaId.setText(DivideYoddhaId(yoddhaId));
            }

//            cardMemberid.setBackgroundColor(getResources().getColor(R.color.green));
            txtYoddaId.setBackgroundResource(R.drawable.addalertgreen);

//            InputMethodManager im = (InputMethodManager)svContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//            im.showSoftInput(et_yoddhaid, 0);

//            if (gd.isConnectingToInternet()) {
//                String[] keys = {"date", "user_id"};
//                String[] value = {"", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID,"")};
//
//                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//                for (int j = 0; j < keys.length; j++) {
//                    System.out.println(keys[j] + "......." + value[j]);
//                    hash.put(keys[j], value[j]);
//                }
//                callWebService(GlobalVariables.GETTRAVELHISTORY, hash, "");
//            } else {
//                ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//            }
        }

        String strAlertStatus = PreferenceConnector.readString(svContext, PreferenceConnector.ISALERT, "0");
        if (strAlertStatus.equalsIgnoreCase("1")) {
//            cardMemberid.setBackgroundColor(getResources().getColor(R.color.red));
            txtYoddaId.setBackgroundResource(R.drawable.addalertred);
            txtYoddaId.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.flash_leave_now));
            if(! PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISALERTSHOWING, false)) {
                Intent svIntent = new Intent(svContext, ActivityAlert.class);
                startActivity(svIntent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }else {
//            cardMemberid.setBackgroundColor(getResources().getColor(R.color.green));
            txtYoddaId.setBackgroundResource(R.drawable.addalertgreen);
        }

//        String strAlertStatus = PreferenceConnector.readString(svContext, PreferenceConnector.ISALERT, "0");
//        if (strAlertStatus.equalsIgnoreCase("1")) {
//            if (! strAlertShow.equalsIgnoreCase("2")) {
//
//            }
//        }
        openTravelHistory();

        String adminFcmId = PreferenceConnector.readString(svContext, PreferenceConnector.ADMINLOGINFCMID, "");
        if (adminFcmId.equalsIgnoreCase(PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, ""))) {
            layAdmin.setVisibility(View.GONE);
        } else {
            layAdmin.setVisibility(View.GONE);
        }

        if (gd.isConnectingToInternet()) {
            String[] keys = {""};
            String[] value = {""};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.LOADSLIDERADS, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
        }
    }

    private void LoadSliderAds() {
        //handle slider click listener
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, "");
                PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, lstReferUrl.get(i));

                Intent svIntent = new Intent(svContext, WebViewActivity.class);
                startActivity(svIntent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        //add slides to slider
        slider.addSlides(slideListLocal);
    }

    private String DivideYoddhaId(String str) {
        String[] equalStr = new String[5];

        //   String str = "RJ123456789012";
        equalStr[0] = str.substring(0, 2);
        equalStr[1] = str.substring(2, 5);
        equalStr[2] = str.substring(5, 8);
        equalStr[3] = str.substring(8, 11);
        equalStr[4] = str.substring(11, str.length());


        String finalYoddhaId = "";
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                finalYoddhaId = finalYoddhaId + equalStr[i];
            } else if (1 == 1) {
                finalYoddhaId = finalYoddhaId + " " + equalStr[i];
            } else {
                finalYoddhaId = finalYoddhaId + " " + equalStr[i];
            }
        }
        return finalYoddhaId;
    }

    private void openTravelHistory() {
//        gps = new GPSTracker(getActivity());
        gd = new GlobalData(svContext);
        db = new DatabaseHandler(svContext);

        edPersonName = (EditText) aiView.findViewById(R.id.et_schoolname);
        edPersonAddress = (EditText) aiView.findViewById(R.id.et_schooladdress);
        edPrimaryContact = (EditText) aiView.findViewById(R.id.et_contactone);
        edPersonEmail = (EditText) aiView.findViewById(R.id.et_email);
        edNote = (EditText) aiView.findViewById(R.id.et_note);
        edDate = (TextView) aiView.findViewById(R.id.et_date);
        edTime = (TextView) aiView.findViewById(R.id.et_time);
        imgProfile = (ImageView) aiView.findViewById(R.id.img_profile);

        et_yoddhaid = (EditText) aiView.findViewById(R.id.text_yoddhaid);

        btnAddYoddhaId = (AppCompatButton) aiView.findViewById(R.id.add_uid);
        btnAddFromCode = (AppCompatButton) aiView.findViewById(R.id.add_scan);

        dropDownViewTravelType = (Spinner) aiView.findViewById(R.id.spin_traveltype);
        dropDownViewTravelMode = (Spinner) aiView.findViewById(R.id.spin_travelmode);

        btnsignUp = (Button) aiView.findViewById(R.id.btn_addschool);

        GlobalData.loadImages(GlobalVariables.PRE_URL_IMG +
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINPROFILEPIC, ""), imgProfile, R.drawable.profile_pic);

//        qrScan = new IntentIntegrator(getActivity());
        isdateSelected = true;
        istimeSelected = true;

        btnsignUp.setOnClickListener(this);
        edDate.setOnClickListener(this);
        edTime.setOnClickListener(this);
        btnAddYoddhaId.setOnClickListener(this);
        btnAddFromCode.setOnClickListener(this);

        edDate.setText(GlobalData.getcurrentDate());
        edTime.setText(GlobalData.getcurrentTime());

//        if (gps.canGetLocation()) {
//            getCurrentLocation();
//        } else {
//            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(svContext)
//                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                    .setTitle("GPS Disabled")
//                    .setMessage("You must have activated gps to get exact shop location.")
//                    .addButton("Activate Location", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
//                            CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
//                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(intent);
//                                dialog.dismiss();
//                            })
//                    .addButton("Cancel", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
//                            CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
//                                dialog.dismiss();
//                            });
//            // Show the alert
//            builder.show();
//        }

        et_yoddhaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                backSpace = previousLength > editable.length();

                if (!backSpace) {
                    if (editable != null) {
                        if (editable.length() == 2 || editable.length() == 6 || editable.length() == 10 || editable.length() == 14) {
                            et_yoddhaid.setText(editable.toString() + "-");
                            et_yoddhaid.setSelection(editable.length() + 1);
                        } else {

                        }
                    }
                }
            }
        });

        if (gd.isConnectingToInternet()) {
            String[] keys = {""};
            String[] value = {""};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
//            callWebService(GlobalVariables.GETTRAVELTYPES, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
        }
    }


    @Override
    public void onClick(View v) {
        Intent svIntent;
        switch (v.getId()) {
            case R.id.img_logout:
//                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, false);

//                svIntent = new Intent(svContext, ActivityLogin.class);
//                startActivity(svIntent);
//                getActivity().finish();
                break;
            case R.id.img_addhistory:
                svIntent = new Intent(svContext, ActivityAddTravelHistory.class);
                startActivity(svIntent);
//                getActivity().finish();
                break;
            case R.id.txt_seemore:
                svIntent = new Intent(svContext, ActivityShowTravelHistory.class);
                startActivity(svIntent);
//                getActivity().finish();
                break;
            case R.id.txt_generatecode:
                svIntent = new Intent(svContext, QRCodeGenerator.class);
                startActivity(svIntent);
//                getActivity().finish();
                break;
            case R.id.lay_admin:
                svIntent = new Intent(svContext, ActivityApproveAlert.class);
                startActivity(svIntent);
//                getActivity().finish();
                break;
            case R.id.add_uid:
                String enterYoddhaId = (et_yoddhaid.getText().toString().trim()).replaceAll(" ", "");
                String currentYoddhaId = PreferenceConnector.readString(svContext, PreferenceConnector.YODDHAID, "");
                if (enterYoddhaId.length() == 0) {
                    et_yoddhaid.setError("Enter id");
                    ShowCustomView.showCustomToast("No id entered", svContext, ShowCustomView.ToastyError);
                } else if (enterYoddhaId.equalsIgnoreCase(currentYoddhaId)) {
                    ShowCustomView.showCustomToast("You can't enter yourself Sanchaar Id", svContext, ShowCustomView.ToastyError);
                } else {
//                    strSelectedDate = GlobalData.getcurrentDate();
//                    strSelectedTime = GlobalData.getcurrentTime();

//                    if (gps.canGetLocation()) {
//                        getCurrentLocation();
//                    } else {
//                        ShowCustomView.showCustomToast("Not able to get current location", svContext, ShowCustomView.ToastyError);
//                    }

                    if (gd.isConnectingToInternet()) {
                        String[] keys = {"user_id", "name", "date", "yoddhaId", "travelType", "address", "locationLat", "locationLong", "modeOfTravel", "note"};
                        String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
                                "", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), enterYoddhaId, "", "", latitude, longitude,
                                "", ""};

                        LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                        for (int j = 0; j < keys.length; j++) {
                            System.out.println(keys[j] + "......." + value[j]);
                            hash.put(keys[j], value[j]);
                        }
                        callWebService(GlobalVariables.ADDTRAVELHISTORY, hash, "");
                    }else {
                        String[] offlineData = {"", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), enterYoddhaId, "", "", latitude, longitude,
                                "", ""};
                        AddOfflineData(offlineData);
                        ShowCustomView.showCustomToast("You are offline. Added to offline database", svContext, ShowCustomView.ToastyInfo);
                    }
                }
                break;
            case R.id.add_scan:
                svIntent = new Intent(svContext, ScannedBarcodeActivity.class);
                startActivity(svIntent);
                break;
            default:
                break;
        }
    }

//    private void SendAlert() {
//        if (gd.isConnectingToInternet()) {
//            String[] keys = {"user_id", "yoddhaId", "labname", "reportnumber"};
//            String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
//                    et_yoddhaid.getText().toString().trim(), et_labname.getText().toString().trim(), et_labname.getText().toString().trim()};
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int j = 0; j < keys.length; j++) {
//                System.out.println(keys[j] + "......." + value[j]);
//                hash.put(keys[j], value[j]);
//            }
//            callWebService(GlobalVariables.ADDTPOSITIVEREPORT, hash, "");
//        } else {
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyInfo);
//        }
//    }

    private void LoadTravelHistory() {
        if (lstData.size() == 0) {
            rvTravelHistory.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            rvTravelHistory.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
            FragDashboard frag = this;
            mAdapter = new TravelHistoryAdapter(lstData, svContext, frag);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(svContext, 1);
            rvTravelHistory.setNestedScrollingEnabled(false);
            rvTravelHistory.setLayoutManager(mLayoutManager);
            rvTravelHistory.setAdapter(mAdapter);
        }
    }

//    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
//        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
//        webService.execute();
//    }

//    @Override
//    public void onWebServiceActionComplete(String result, String url) {
//        System.out.println(result + ".........jsonresponse....." + url);
//
//        if (url.contains(GlobalVariables.GETTRAVELHISTORY)) {
//            lstData = new ArrayList<TravelHistoryModel>();
//            try {
//                JSONObject json = new JSONObject(result);
//
////                        lstData.add(new TravelHistoryModel(str_name, str_date, str_yoddhaId, str_travelType, str_address, str_locationLat,
////                                str_locationLong, str_modeOfTravel, str_note));
//
//
//            } catch (JSONException e) {
//                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
//                e.printStackTrace();
//            }
//
////            LoadTravelHistory();
//        } else if (url.contains(GlobalVariables.ADDTPOSITIVEREPORT)) {
//            try {
//                JSONObject json = new JSONObject(result);
//
//                String str_result = json.getString("result");
//                String str_msg = json.getString("msg");
//
//                if (str_result.equals("success")) {
//                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastySuccess);
//                } else {
//
//                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
//                }
//            } catch (JSONException e) {
//                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                JSONObject json = new JSONObject(result);
//
//            } catch (JSONException e) {
//                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
//                e.printStackTrace();
//            }
//        }
//    }

//    private void ShowConfirmDialog() {
//        String enterYoddhaId = et_yoddhaid.getText().toString().trim();
//        new MaterialDialog.Builder(svContext)
////                .input(strRemark, strRemark, false, new MaterialDialog.InputCallback() {
////                    @Override
////                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
////                        strRemark = input.toString();
////                    }
////                })
//                .title("Are you sure for your report")
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .positiveText("Yes Confirm")
//                .positiveColor(getResources().getColor(R.color.green))
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        SendAlert();
//                    }
//                })
//                .negativeText("Cancel")
//                .negativeColor(getResources().getColor(R.color.red))
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(svContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            svContext.sendBroadcast(poke);
        }
    }

    private void turnGPSOff() {
        String provider = Settings.Secure.getString(svContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            svContext.sendBroadcast(poke);
        }
    }

//    private void OpenQrCodeScanner() {
//        Dexter.withActivity(getActivity())
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        qrScan.initiateScan();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        // check for permanent denial of permission
//                        if (response.isPermanentlyDenied()) {
//                            ShowCustomView.showCustomToast("Turn Camera Permission On", svContext, ShowCustomView.ToastyInfo);
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//    }

    public void showCustomDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(svContext, myDateListener, mYear, mMonth, mDay);
        dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dpDialog.show();

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            isdateSelected = true;
            strSelectedDate = GlobalData.getFormatedcurrentDate(dayOfMonth, monthOfYear + 1, year);

            edDate.setText(strSelectedDate);
        }
    };


    private void showCustomTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(svContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                edFollowUpTime.setText(selectedHour + ":" + selectedMinute);
                strSelectedTime = selectedHour + ":" + selectedMinute;
                istimeSelected = true;

                edTime.setText(strSelectedTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void SubmitForm() {
        int response = 0;
        response = GlobalData.emptyEditTextError(
                new EditText[]{edPersonName},
                new String[]{"enter Person name"});

        strPersonName = edPersonName.getText().toString().trim();
        strPersonEmail = edPersonEmail.getText().toString().trim();
        strPersonAddress = edPersonAddress.getText().toString().trim();
        strPrimaryContact = edPrimaryContact.getText().toString().trim();
        strNote = edNote.getText().toString().trim();

//        if (!isdateSelected) {
//            response++;
//            ShowCustomView.showCustomToast("Please select date", svContext, ShowCustomView.ToastyError);
//        }
//
//        if (!istimeSelected) {
//            response++;
//            ShowCustomView.showCustomToast("Please select time", svContext, ShowCustomView.ToastyError);
//        }

        if (strPersonEmail.length() != 0) {
            if (!GlobalData.isEmailValid(strPersonEmail)) {
                response++;
                edPersonEmail.setError("Invalid Email Address");
            }
        }

        if (strPrimaryContact.length() != 0) {
            if (strPrimaryContact.length() != 10) {
                if (!strPrimaryContact.startsWith("0145")) {
                    response++;
                    edPrimaryContact.setError("Invalid phone number");
                }
            }
        }

//        if (gps.canGetLocation()) {
//            getCurrentLocation();
//        } else {
//            response++;
//            ShowCustomView.showCustomToast("Not able to get current location", svContext, ShowCustomView.ToastyError);
//        }
        if (response == 0) {
            if (gd.isConnectingToInternet()) {
                String[] keys = {"user_id", "name", "date", "yoddhaId", "travelType", "address", "locationLat", "locationLong", "modeOfTravel", "note"};
                String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
                        strPersonName, GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), "", selectedTravelTypeID, strPersonAddress, latitude, longitude,
                        selectedTravelModeID, strNote};

                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                for (int j = 0; j < keys.length; j++) {
                    System.out.println(keys[j] + "......." + value[j]);
                    hash.put(keys[j], value[j]);
                }
                callWebService(GlobalVariables.ADDTRAVELHISTORY, hash, "");
            } else {
                String[] offlineData = {strPersonName, strSelectedDate + " " + strSelectedTime, "", selectedTravelTypeID, strPersonAddress,
                        latitude, longitude, selectedTravelModeID, strNote};
                AddOfflineData(offlineData);
                ShowCustomView.showCustomToast("You are offline. Added to offline database", svContext, ShowCustomView.ToastyInfo);
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    public static final String TAG_TRAVELMODE = "travelmode";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.ADDTRAVELHISTORY)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString(TAG_RESULT);
                String str_msg = json.getString(TAG_MSG);

                if (str_result.equals("success")) {
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastySuccess);
                } else {
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        } else if (url.contains(GlobalVariables.LOADSLIDERADS)) {
            try {
                lstReferUrl = new ArrayList<>();
                slideList=new ArrayList<>();
                slideListLocal = new ArrayList<>();
                JSONObject json = new JSONObject(result);
                String str_status = json.getString(TAG_STATUS);
                JSONArray banners = json.getJSONArray(TAG_BANNERS);
                for (int banners_i = 0; banners_i < banners.length(); banners_i++) {
                    JSONObject banners_obj = banners.getJSONObject(banners_i);
                    String str_banner_id = banners_obj.getString(TAG_BANNER_ID);
                    String str_banner_url = banners_obj.getString(TAG_BANNER_URL);
                    String str_banner_image = banners_obj.getString(TAG_BANNER_IMAGE);

                    if (str_banner_image.contains("http://") || str_banner_image.contains("http://")) {
                        str_banner_image = str_banner_image;
                    } else {
                        str_banner_image = "http://" + str_banner_image;
                    }


                    lstReferUrl.add(str_banner_url);

                    try {
                        slideList.add(new Slide(banners_i, str_banner_image, getResources().getDimensionPixelSize(R.dimen.no_corner)));
                        slideListLocal.add(new Slide(banners_i, str_banner_image, getResources().getDimensionPixelSize(R.dimen.no_corner)));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                LoadSliderAds();
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }

            (aiView.findViewById(R.id.mylayout)).setVisibility(View.VISIBLE);
        } else if (url.contains(GlobalVariables.GETTRAVELTYPES)) {
            try {
                JSONObject json = new JSONObject(result);
                JSONArray traveltype = json.getJSONArray(TAG_TRAVELTYPE);
                for (int traveltype_i = 0; traveltype_i < traveltype.length(); traveltype_i++) {
                    String str_traveltype = traveltype.getString(traveltype_i);
                    travelType.add(str_traveltype);
                }
                JSONArray travelmode = json.getJSONArray(TAG_TRAVELMODE);
                for (int travelmode_i = 0; travelmode_i < travelmode.length(); travelmode_i++) {
                    String str_travelmode = travelmode.getString(travelmode_i);
                    travelMode.add(str_travelmode);
                }

            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }

            populateTravelType();
            populateTravelMode();
        } else if (url.contains(GlobalVariables.GETSTATELIST)) {
            try {
                JSONObject json = new JSONObject(result);
                JSONArray states = json.getJSONArray(TAG_STATES);
                for (int states_i = 0; states_i < states.length(); states_i++) {
                    JSONObject states_obj = states.getJSONObject(states_i);
                    String str_code = states_obj.getString(TAG_CODE);

                    String str_name = states_obj.getString(TAG_NAME);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject json = new JSONObject(result);

//                populateState(db.getAllCategory(db.tablenames[0]));

            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }
    }

    List<String> travelType = new ArrayList<String>();
    List<String> travelMode = new ArrayList<String>();

    private void populateTravelType() {
        SpinnerAdapter spinnerAdapter = new SpinnerPopulateAdapter(svContext, travelType, false);
        dropDownViewTravelType.setAdapter(spinnerAdapter);

        dropDownViewTravelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View vi, int selectedPos, long arg3) {
                TextView txtView = ((TextView) vi.findViewById(R.id.txtitem));
                txtView.setTextColor(getResources().getColor(R.color.fontcolordark));
                txtView.setGravity(Gravity.CENTER_VERTICAL);

                selectedTravelTypeID = travelType.get(selectedPos);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void populateTravelMode() {
        SpinnerAdapter spinnerAdapter = new SpinnerPopulateAdapter(svContext, travelMode, false);
        dropDownViewTravelMode.setAdapter(spinnerAdapter);

        dropDownViewTravelMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View vi, int selectedPos, long arg3) {
                TextView txtView = ((TextView) vi.findViewById(R.id.txtitem));
                txtView.setTextColor(getResources().getColor(R.color.fontcolordark));
                txtView.setGravity(Gravity.CENTER_VERTICAL);

                selectedTravelModeID = travelMode.get(selectedPos);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

//    private void getCurrentLocation() {
//        Dexter.withActivity(getActivity())
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        turnGPSOn();
//                        if (gps.canGetLocation()) {
//                            // permission is granted
//                            longitude = gps.getLongitude() + "";
//                            latitude = gps.getLatitude() + "";
//                        }
//                        turnGPSOff();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        // check for permanent denial of permission
//                        if (response.isPermanentlyDenied()) {
////                            ShowCustomView.showCustomToast("Turn Gps on for current location", svContext, ShowCustomView.ToastyInfo);
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//

    private void AddOfflineData(String[] offlineData) {
        db.addData(offlineData, 0);
    }
}