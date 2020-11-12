package in.kamalaenterprize.sncharsuchi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.adapter.AdapterListExpand;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.LineItemDecoration;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.model.TravelHistoryModel;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ActivityShowTravelHistory extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private View parent_view;
    private RecyclerView recyclerView;
    private AdapterListExpand mAdapter;
    private ImageView imgBack;
    private TextView edDate;
    List<TravelHistoryModel> lstData = new ArrayList<>();
    private int animation_type = ItemAnimation.FADE_IN;
    private TextView txtNodata, txtShowAll;
    public static final String TAG_TRAVEL_HISTORY="travel_history";
    public static final String TAG_DATE="date";
    public static final String TAG_LOCATIONLAT="locationLat";
    public static final String TAG_NOTE="note";
    public static final String TAG_YODDHAID="yoddhaId";
    public static final String TAG_LOCATIONLONG="locationLong";
    public static final String TAG_MODEOFTRAVEL="modeOfTravel";
    public static final String TAG_USER_ID="user_id";
    public static final String TAG_NAME="name";
    public static final String TAG_STATUS="status";

    public static final String TAG_BANNERS="banners";
    public static final String TAG_BANNER_ID="banner_id";
    public static final String TAG_BANNER_URL="banner_url";
    public static final String TAG_BANNER_IMAGE="banner_image";
//    private List<String> lstReferUrl = new ArrayList<>();
//    private List<Slide> slideList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        parent_view = findViewById(android.R.id.content);
        svContext = this;
        gd = new GlobalData(svContext);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgBack = (ImageView) findViewById(R.id.img_back);
        edDate = (TextView) findViewById(R.id.et_date);
        txtNodata = (TextView) findViewById(R.id.txt_nodata);
        txtShowAll= (TextView) findViewById(R.id.et_showall);


        imgBack.setOnClickListener(this);
        edDate.setOnClickListener(this);
        txtShowAll.setOnClickListener(this);

        ShowAllHistory();

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
            } else {
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

    private void ShowAllHistory() {
        strSelectedDate = "";
        if (gd.isConnectingToInternet()) {
            String[] keys = {"date", "user_id"};
            String[] value = {"", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID,"")};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.GETTRAVELHISTORY, hash, "");
        } else {
            txtNodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            txtNodata.setText(getResources().getString(R.string.error_nodata_internet));
        }
    }

    private void initComponent() {
        if (lstData.size() == 0) {
            txtNodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            if (strSelectedDate.equalsIgnoreCase("")) {
                txtNodata.setText(getResources().getString(R.string.error_nodata));
            } else {
                txtNodata.setText(getResources().getString(R.string.error_nodata) + " for " + strSelectedDate);
            }
        } else {
            txtNodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
            recyclerView.setHasFixedSize(true);

            mAdapter = new AdapterListExpand(this, lstData, animation_type);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);

        if (url.contains(GlobalVariables.GETTRAVELHISTORY)) {
            lstData = new ArrayList<TravelHistoryModel>();
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString("status");
                if (str_result.equals("success")) {
                    JSONArray travel_history = json.getJSONArray(TAG_TRAVEL_HISTORY);
                    for(int travel_history_i = 0; travel_history_i < travel_history.length(); travel_history_i++){
                        JSONObject travel_history_obj=travel_history.getJSONObject(travel_history_i);
                        String str_date = travel_history_obj.getString(TAG_DATE);
                        String str_locationLat = travel_history_obj.getString(TAG_LOCATIONLAT);
                        String str_note = travel_history_obj.getString(TAG_NOTE);
                        String str_yoddhaId = travel_history_obj.getString(TAG_YODDHAID);
                        String str_locationLong = travel_history_obj.getString(TAG_LOCATIONLONG);
                        String str_modeOfTravel = travel_history_obj.getString(TAG_MODEOFTRAVEL);
                        String str_user_id = travel_history_obj.getString(TAG_USER_ID);
                        String str_name = travel_history_obj.getString(TAG_NAME);
                        String isverified = travel_history_obj.getString("isverified");

                        lstData.add(new TravelHistoryModel(str_name, str_date, str_yoddhaId, "str_travelType", "str_address", str_locationLat,
                                str_locationLong, str_modeOfTravel, str_note, str_user_id, isverified));
                    }
                }else {
                    String str_msg = json.getString("msg");
                    if (! str_msg.equalsIgnoreCase("No data found")) {
                        ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                    }
                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }

            initComponent();
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
        } else {
            try {
                JSONObject json = new JSONObject(result);

            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    @Override
    public void onClick(View view) {
        int response;
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.et_date:
                showCustomDatePicker();
                break;
            case R.id.et_showall:
                edDate.setText("Select Date");
                ShowAllHistory();
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
            strSelectedDate = GlobalData.getFormatedcurrentDate(dayOfMonth, monthOfYear + 1, year);

            edDate.setText(strSelectedDate);

            if (gd.isConnectingToInternet()) {
                String[] keys = {"date", "user_id"};
                String[] value = {strSelectedDate, PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID,"")};

                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                for (int j = 0; j < keys.length; j++) {
                    System.out.println(keys[j] + "......." + value[j]);
                    hash.put(keys[j], value[j]);
                }
                callWebService(GlobalVariables.GETTRAVELHISTORY, hash, "");
            } else {
                ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}
