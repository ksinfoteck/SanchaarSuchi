package in.kamalaenterprize.sncharsuchi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.commonutility.CustomeProgressDialog;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.SpinnerPopulateAdapter;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.model.LabDetailModel;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ActivityAddReport extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private Typeface font;
    private String strPersonName;
    private GlobalData gd;
    private ImageView imgBack;
    private AppCompatButton btnAddYoddhaId;
    private EditText et_fullname;
    private TextView et_yoddhaid;
    private Spinner dropDownState, dropDownLab;

    private EditText et_reportnumber, et_labname;
    private TextView edDate;

    public static final String TAG_RESOURCES="resources";
    public static final String TAG_RECORDID="recordid";
    public static final String TAG_CITY="city";
    public static final String TAG_DESCRIPTIONANDORSERVICEPROVIDED="descriptionandorserviceprovided";
    public static final String TAG_CONTACT="contact";
    public static final String TAG_PHONENUMBER="phonenumber";
    public static final String TAG_STATE="state";
    public static final String TAG_CATEGORY="category";
    public static final String TAG_NAMEOFTHEORGANISATION="nameoftheorganisation";

    public static final String TAG_STATES = "states";
    public static final String TAG_CODE = "code";
    public static final String TAG_NAME = "name";
    public static final String TAG_RESULT = "result";
    public static final String TAG_STATUS="status";
    public static final String TAG_BANNERS="banners";
    public static final String TAG_BANNER_ID="banner_id";
    public static final String TAG_BANNER_URL="banner_url";
    public static final String TAG_BANNER_IMAGE="banner_image";

    private List<LabDetailModel> lstLabDetail = new ArrayList<>();
    private List<String> lstSelectedLabDetail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_addreport);
        svContext = this;
        gd = new GlobalData(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        et_yoddhaid         = (TextView) findViewById(R.id.text_yoddhaid);
        et_fullname         = (EditText) findViewById(R.id.text_username);
        et_reportnumber     = (EditText) findViewById(R.id.text_testreportnumber);
        et_labname          = (EditText) findViewById(R.id.text_testlabname);
        edDate              = (TextView) findViewById(R.id.text_selectdate);

        dropDownState   = (Spinner) findViewById(R.id.state);
        dropDownLab     = (Spinner) findViewById(R.id.lab);

        et_yoddhaid.setText(DivideYoddhaId(PreferenceConnector.readString(svContext, PreferenceConnector.YODDHAID, "")));
        et_yoddhaid.setEnabled(false);
        et_fullname.setText(PreferenceConnector.readString(svContext, PreferenceConnector.LOGINNAME, ""));

        btnAddYoddhaId = (AppCompatButton) findViewById(R.id.submit_reort);

        imgBack = (ImageView) findViewById(R.id.img_back);
        edDate.setOnClickListener(this);

        imgBack.setOnClickListener(this);
        btnAddYoddhaId.setOnClickListener(this);

        if (gd.isConnectingToInternet()) {
            String[] keys = {""};
            String[] value = {""};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.GETSTATELIST, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
        }
        LoadLabDetail();

        if (FragDashboard.slideList.size() == 0 || FragDashboard.slideList == null){
            if (gd.isConnectingToInternet()) {
                String[] keys = {""};
                String[] value = {""};

                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                for (int j = 0; j < keys.length; j++) {
                    System.out.println(keys[j] + "......." + value[j]);
                    hash.put(keys[j], value[j]);
                }
                callWebService(GlobalVariables.LOADSLIDERADS, hash, "");
            }else {
                ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
            }
        }else {
            LoadSliderAds();
        }
    }

    private void LoadSliderAds() {
        Slider slider = findViewById(R.id.slider);
        //handle slider click listener
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, "");
                PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, FragDashboard.lstReferUrl.get(i));

                Intent svIntent = new Intent(svContext, WebViewActivity.class);
                startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        //add slides to slider
        slider.addSlides(FragDashboard.slideList);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_reort:
                int response = 0;
                response = GlobalData.emptyEditTextError(
                        new EditText[]{et_reportnumber, et_labname, et_fullname},
                        new String[]{"enter report number", "enter lab name", "enter full name"});

                if(strSelectedDate == "" || strSelectedDate.equalsIgnoreCase("Select Date")) {
                    response++;
                    ShowCustomView.showCustomToast("Please select date", svContext, ShowCustomView.ToastyError);
                }

                if (response == 0) {
                    ShowConfirmDialog();
                }
                break;
            case R.id.text_selectdate:
                showCustomDatePicker();
                break;
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }

    boolean isdateSelected = false;
    private String strSelectedDate = "";
    public void showCustomDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(this, myDateListener, mYear, mMonth, mDay);
        dpDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpDialog.show();
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            isdateSelected = true;
            strSelectedDate = getFormatedcurrentDate(dayOfMonth, monthOfYear + 1, year);

            edDate.setText(strSelectedDate);
        }
    };

    public static String getFormatedcurrentDate(int date, int month, int year) {
        String mon = "", dat = "";
        if (date < 10) {
            dat = "0" + date;
        }else {
            dat = "" + date;
        }

        if (month < 10) {
            mon = "0" + month;
        } else {
            mon = "" + month;
        }

        return (dat) + "/" + mon + "/" + year + "";
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        finish();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.ADDTPOSITIVEREPORT)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString("result");
                String str_msg = json.getString("msg");

                if (str_result.equals("true")) {
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastySuccess);

                    onBackPressed();
                }else {
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        } else if (url.contains(GlobalVariables.GETSTATELIST)) {
            try {
                JSONObject json = new JSONObject(result);
                JSONArray states = json.getJSONArray(TAG_STATES);
                for (int states_i = 0; states_i < states.length(); states_i++) {
                    JSONObject states_obj = states.getJSONObject(states_i);
                    String str_code = states_obj.getString(TAG_CODE);
                    String str_name = states_obj.getString(TAG_NAME);

                    lstStates.add(str_code + "#:#" + str_name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            populateStates();
        }else if (url.contains(GlobalVariables.LOADSLIDERADS)) {
            FragDashboard.slideList = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString(TAG_STATUS);
                JSONArray banners = json.getJSONArray(TAG_BANNERS);
                for(int banners_i = 0; banners_i < banners.length(); banners_i++){
                    JSONObject banners_obj=banners.getJSONObject(banners_i);
                    String str_banner_id = banners_obj.getString(TAG_BANNER_ID);
                    String str_banner_url = banners_obj.getString(TAG_BANNER_URL);
                    String str_banner_image = banners_obj.getString(TAG_BANNER_IMAGE);

                    if(str_banner_image.contains("http://") || str_banner_image.contains("http://")){
                        str_banner_image = str_banner_image;
                    }else {
                        str_banner_image = "http://"+str_banner_image;
                    }

                    FragDashboard.lstReferUrl.add(str_banner_url);
                    FragDashboard.slideList.add(new Slide(banners_i, str_banner_image, getResources().getDimensionPixelSize(R.dimen.no_corner)));
                }

                LoadSliderAds();
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        } else if(url.contains(GlobalVariables.GETLABDETAIL)){
            try {
                JSONObject json = new JSONObject(result);

                lstLabDetail = new ArrayList<>();
                JSONArray resources = json.getJSONArray(TAG_RESOURCES);
                for(int resources_i = 0; resources_i < resources.length(); resources_i++){
                    JSONObject resources_obj=resources.getJSONObject(resources_i);
                    String str_recordid = resources_obj.getString(TAG_RECORDID);
                    String str_city = resources_obj.getString(TAG_CITY);
                    String str_descriptionandorserviceprovided = resources_obj.getString(TAG_DESCRIPTIONANDORSERVICEPROVIDED);
                    String str_contact = resources_obj.getString(TAG_CONTACT);
                    String str_phonenumber = resources_obj.getString(TAG_PHONENUMBER);
                    String str_state = resources_obj.getString(TAG_STATE);
                    String str_category = resources_obj.getString(TAG_CATEGORY);
                    String str_nameoftheorganisation = resources_obj.getString(TAG_NAMEOFTHEORGANISATION);

                    if (str_category.equalsIgnoreCase("CoVID-19 Testing Lab")) {
                        lstLabDetail.add(new LabDetailModel(str_recordid, str_city, str_descriptionandorserviceprovided,
                                str_contact, str_phonenumber, str_nameoftheorganisation, str_state));
                    }

                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject json = new JSONObject(result);

//                populateState(db.getAllCategory(db.tablenames[0]));

            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }
    }

//    private void ShowConfirmDialog() {
//        new MaterialDialog.Builder(svContext)
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

    private void ShowConfirmDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(svContext);
        alertDialog.setTitle("Are you sure for your report");
        alertDialog.setPositiveButton("Yes Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SendAlert();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void SendAlert() {
        if (gd.isConnectingToInternet()) {
            String[] keys = {"user_id", "yoddhaId", "labname", "reportnumber", "fullname", "labnumber", "report_date"};
            String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
                    (et_yoddhaid.getText().toString().trim()).replaceAll(" ", ""), et_labname.getText().toString().trim(),
                    et_labname.getText().toString().trim(),
                    et_fullname.getText().toString().trim(), lstSelectedLabDetail.get(mLastSelectedPos).split("#:#")[0],
            strSelectedDate};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.ADDTPOSITIVEREPORT, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyInfo);
        }
    }

    private List<String> lstStates = new ArrayList<String>();
    private String selectedStateCode = "", selectedStateName = "";
    private void populateStates() {
        SpinnerAdapter spinnerAdapter = new SpinnerPopulateAdapter(svContext, lstStates, true);
        dropDownState.setAdapter(spinnerAdapter);

        dropDownState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View vi, int selectedPos, long arg3) {
                TextView txtView = ((TextView) vi.findViewById(R.id.txtitem));
                txtView.setTextColor(getResources().getColor(R.color.fontcolordark));
                txtView.setGravity(Gravity.CENTER_VERTICAL);

                selectedStateCode = lstStates.get(selectedPos).split("#:#")[0];
                selectedStateName = lstStates.get(selectedPos).split("#:#")[1];

                et_labname.setText("");
                populateLab(selectedStateCode, selectedStateName);
            }

            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    int mLastSelectedPos = 0;
    private void populateLab(String selectedStateCode, String selectedStateName) {
        lstSelectedLabDetail = new ArrayList<>();
        for (int i = 0; i < lstLabDetail.size(); i++) {
            if (lstLabDetail.get(i).getStr_state().equalsIgnoreCase(selectedStateName)){
                lstSelectedLabDetail.add(lstLabDetail.get(i).getStr_phonenumber()+"#:#"+lstLabDetail.get(i).getStr_nameoftheorganisation());
            }
        }
        lstSelectedLabDetail.add("-1"+"#:#"+"Other");

        SpinnerAdapter spinnerAdapter = new SpinnerPopulateAdapter(svContext, lstSelectedLabDetail, true);
        dropDownLab.setAdapter(spinnerAdapter);

        dropDownLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View vi, int selectedPos, long arg3) {
                TextView txtView = ((TextView) vi.findViewById(R.id.txtitem));
                txtView.setTextColor(getResources().getColor(R.color.fontcolordark));
                txtView.setGravity(Gravity.CENTER_VERTICAL);

                if ((lstSelectedLabDetail.get(selectedPos).split("#:#")[1]).equalsIgnoreCase("Other")) {
                    et_labname.setHint(getResources().getString(R.string.addreport_edt_labname));
                    et_labname.setText("");
                    et_labname.setEnabled(true);
                }else {
                    et_labname.setEnabled(false);
                    et_labname.setText(lstSelectedLabDetail.get(selectedPos).split("#:#")[1]);
                }

                mLastSelectedPos = selectedPos;
            }

            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    private void LoadLabDetail() {
        CustomeProgressDialog customeProgressDialog	=	new CustomeProgressDialog(svContext, R.layout.custom_progess_dialog);
        customeProgressDialog.setCancelable(false);
//        customeProgressDialog.setTitle(showText);
        customeProgressDialog.show();

        InputStream is = getResources().openRawResource(R.raw.resources);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

            is.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = writer.toString();

        try {
            JSONObject json = new JSONObject(jsonString);

            lstLabDetail = new ArrayList<>();
            JSONArray resources = json.getJSONArray(TAG_RESOURCES);
            for(int resources_i = 0; resources_i < resources.length(); resources_i++){
                JSONObject resources_obj=resources.getJSONObject(resources_i);
                String str_recordid = resources_obj.getString(TAG_RECORDID);
                String str_city = resources_obj.getString(TAG_CITY);
                String str_descriptionandorserviceprovided = resources_obj.getString(TAG_DESCRIPTIONANDORSERVICEPROVIDED);
                String str_contact = resources_obj.getString(TAG_CONTACT);
                String str_phonenumber = resources_obj.getString(TAG_PHONENUMBER);
                String str_state = resources_obj.getString(TAG_STATE);
                String str_category = resources_obj.getString(TAG_CATEGORY);
                String str_nameoftheorganisation = resources_obj.getString(TAG_NAMEOFTHEORGANISATION);

                if (str_category.equalsIgnoreCase("CoVID-19 Testing Lab")) {
                    lstLabDetail.add(new LabDetailModel(str_recordid, str_city, str_descriptionandorserviceprovided,
                            str_contact, str_phonenumber, str_nameoftheorganisation, str_state));
                }

            }
        } catch (JSONException e) {
            GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
            e.printStackTrace();
        }

        if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
            customeProgressDialog.dismiss();
        }

//        if (gd.isConnectingToInternet()) {
//            String[] keys = {""};
//            String[] value = {""};
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int j = 0; j < keys.length; j++) {
//                System.out.println(keys[j] + "......." + value[j]);
//                hash.put(keys[j], value[j]);
//            }
//
//            WebServiceWithoutPreUrl webService = new WebServiceWithoutPreUrl(svContext, GlobalVariables.GETLABDETAIL, hash, this, WebService.POST, "");
//            webService.execute();
//        }else {
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyInfo);
//        }
    }

    private String DivideYoddhaId(String str) {
        String[] equalStr = new String[5];

//        String str = "RJ123456789012";
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
}


