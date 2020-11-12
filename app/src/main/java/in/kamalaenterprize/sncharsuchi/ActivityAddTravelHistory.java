package in.kamalaenterprize.sncharsuchi;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.commonutility.GPSTracker;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.SpinnerPopulateAdapter;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.database.DatabaseHandler;

public class ActivityAddTravelHistory extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private EditText edPersonName, edPersonAddress, edPrimaryContact, edPersonEmail, edNote;
    private TextView edDate, edTime;
    private Button btnsignUp;
    private Typeface font;
    private String strPersonName, strPersonAddress, strPrimaryContact, strPersonEmail, strNote;
    private GlobalData gd;
    private ImageView imgBack;
    private DatabaseHandler db;
    private GPSTracker gps;
    private Spinner dropDownViewTravelType, dropDownViewTravelMode;
    private String latitude="", longitude="";
    private String strSelectedDate, strSelectedTime = "";
    boolean isdateSelected = false, istimeSelected = false;
    private String selectedTravelTypeID = "", selectedTravelModeID = "";
    private AppCompatButton btnAddYoddhaId, btnAddFromCode;
    private EditText et_yoddhaid;
    private IntentIntegrator qrScan;

    public static final String TAG_RESULT = "result";
    public static final String TAG_MSG = "msg";
    public static final String TAG_STATES="states";
    public static final String TAG_CODE="code";
    public static final String TAG_NAME="name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_addtravelhistory);
        svContext = this;
        gps = new GPSTracker(this);
        gd = new GlobalData(svContext);
        db = new DatabaseHandler(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        edPersonName = (EditText) findViewById(R.id.et_schoolname);
        edPersonAddress = (EditText) findViewById(R.id.et_schooladdress);
        edPrimaryContact = (EditText) findViewById(R.id.et_contactone);
        edPersonEmail = (EditText) findViewById(R.id.et_email);
        edNote = (EditText) findViewById(R.id.et_note);
        edDate = (TextView) findViewById(R.id.et_date);
        edTime = (TextView) findViewById(R.id.et_time);

        et_yoddhaid = (EditText) findViewById(R.id.text_yoddhaid);

        btnAddYoddhaId = (AppCompatButton) findViewById(R.id.add_uid);
        btnAddFromCode = (AppCompatButton) findViewById(R.id.add_scan);

        dropDownViewTravelType = (Spinner) findViewById(R.id.spin_traveltype);
        dropDownViewTravelMode = (Spinner) findViewById(R.id.spin_travelmode);

        btnsignUp = (Button) findViewById(R.id.btn_addschool);
        imgBack = (ImageView) findViewById(R.id.img_back);

        qrScan = new IntentIntegrator(this);
        isdateSelected = true;
        istimeSelected = true;

        btnsignUp.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        edDate.setOnClickListener(this);
        edTime.setOnClickListener(this);
        btnAddYoddhaId.setOnClickListener(this);
        btnAddFromCode.setOnClickListener(this);

        edDate.setText(GlobalData.getcurrentDate());
        edTime.setText(GlobalData.getcurrentTime());

//        if (gps.canGetLocation()) {
//            getCurrentLocation();
//        } else {
//            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
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


        OpenQrCodeScanner();
    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void TopWithBack(String strHead) {
        ImageView imgback = (ImageView) findViewById(R.id.img_back);
        imgback.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText(strHead);
    }

    @Override
    public void onClick(View view) {
        int response;
        switch (view.getId()) {
            case R.id.btn_addschool:
                SubmitForm();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.et_date:
                showCustomDatePicker();
                break;
            case R.id.et_time:
                showCustomTimePicker();
                break;
            case R.id.add_uid:
                String enterYoddhaId = et_yoddhaid.getText().toString().trim();
                if (enterYoddhaId.length() == 0) {
                    et_yoddhaid.setError("Enter id");
                    ShowCustomView.showCustomToast("No id entered", svContext, ShowCustomView.ToastyError);
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
                    } else {
                        String[] offlineData = {"", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), enterYoddhaId, "", "", latitude, longitude,
                                "", ""};
                        AddOfflineData(offlineData);
                        ShowCustomView.showCustomToast("You are offline. Added to offline database", svContext, ShowCustomView.ToastyInfo);

                    }
                }
                break;
            case R.id.add_scan:
                OpenQrCodeScanner();
                break;
        }
    }

    private void OpenQrCodeScanner() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        qrScan.initiateScan();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            ShowCustomView.showCustomToast("Turn Camera Permission On", svContext, ShowCustomView.ToastyInfo);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void showCustomDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(this, myDateListener, mYear, mMonth, mDay);
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
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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

    @Override
    public void onBackPressed() {
        finish();
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

    public static final String TAG_TRAVELTYPE = "traveltype";
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

            finish();
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
//        Dexter.withActivity(this)
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
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
//                if (gps.canGetLocation()) {
//                    getCurrentLocation();
//                } else {
//                    ShowCustomView.showCustomToast("Not able to get current location", svContext, ShowCustomView.ToastyError);
//                }

                if (gd.isConnectingToInternet()) {
                    String[] keys = {"user_id", "name", "date", "yoddhaId", "travelType", "address", "locationLat", "locationLong", "modeOfTravel", "note"};
                    String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
                            "", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), result.getContents(), "", "", latitude, longitude,
                            "", ""};

                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                    for (int j = 0; j < keys.length; j++) {
                        System.out.println(keys[j] + "......." + value[j]);
                        hash.put(keys[j], value[j]);
                    }
                    callWebService(GlobalVariables.ADDTRAVELHISTORY, hash, "");
                } else {
                    String[] offlineData = {"", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), result.getContents(), "", "", latitude, longitude, "", ""};
                    AddOfflineData(offlineData);
                    ShowCustomView.showCustomToast("You are offline. Added to offline database", svContext, ShowCustomView.ToastyInfo);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void AddOfflineData(String[] offlineData) {
        db.addData(offlineData, 0);
    }
}
