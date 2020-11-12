package in.kamalaenterprize.sncharsuchi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import in.kamalaenterprize.adapter.AdapterNotification;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.model.NotificationModel;

public class ActivityNotification extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private View parent_view;
    private RecyclerView recyclerView;
    private AdapterNotification mAdapter;
    private ImageView imgBack;
    private List<NotificationModel> lstData = new ArrayList<>();
    private int animation_type = ItemAnimation.FADE_IN;
    private TextView txtNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        parent_view = findViewById(android.R.id.content);
        svContext = this;
        gd = new GlobalData(svContext);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtNodata = (TextView) findViewById(R.id.txt_nodata);

        imgBack.setOnClickListener(this);

        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISNOTIFICATION, false);

        ShowAllNoti();
    }

    private void ShowAllNoti() {
        strSelectedDate = "";
        if (gd.isConnectingToInternet()) {
            String[] keys = {"user_id"};
            String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, "")};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.GETNOTIFICATION, hash, "");
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
//            recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
            recyclerView.setHasFixedSize(true);

            ActivityNotification act = this;
            mAdapter = new AdapterNotification(this, lstData, animation_type, act);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public static final String TAG_NOTIFICATIONS = "notifications";
    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_NOTIFICATION_TITLE = "notification_title";
    public static final String TAG_ID = "id";
    public static final String TAG_TYPE = "type";
    public static final String TAG_HISTORY_ID = "history_id";
    public static final String TAG_SENDER_USER_ID = "sender_user_id";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.GETNOTIFICATION)) {
            lstData = new ArrayList<NotificationModel>();
            try {
                JSONObject json = new JSONObject(result);
//                {"status":false,"msg":"No data found"}

                String str_result = json.getString("status");

                if (str_result.equalsIgnoreCase("success")) {
                    JSONArray notifications = json.getJSONArray(TAG_NOTIFICATIONS);
                    for (int notifications_i = 0; notifications_i < notifications.length(); notifications_i++) {
                        JSONObject notifications_obj = notifications.getJSONObject(notifications_i);
                        String str_user_id = notifications_obj.getString(TAG_USER_ID);
                        String str_notification_title = notifications_obj.getString(TAG_NOTIFICATION_TITLE);
                        String str_id = notifications_obj.getString(TAG_ID);
                        String str_type = notifications_obj.getString(TAG_TYPE);
                        String str_history_id = notifications_obj.getString(TAG_HISTORY_ID);
                        String str_sender_user_id = notifications_obj.getString(TAG_SENDER_USER_ID);

                        String str_status = notifications_obj.getString("status");
                        String str_meeterremark = notifications_obj.getString("meeter_remark");

                        lstData.add(new NotificationModel(str_id, str_user_id, str_sender_user_id, str_notification_title, str_type, str_history_id,
                                false, str_status, str_meeterremark));
                    }
                } else {
                    String str_msg = json.getString("msg");
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                GlobalData.showToast("json error occured", svContext);
                e.printStackTrace();
            }

            initComponent();
        } else if (url.contains(GlobalVariables.ACCEPTREJECT)) {
            try {
                JSONObject json = new JSONObject(result);

            } catch (JSONException e) {
                GlobalData.showToast("json error occured", svContext);
                e.printStackTrace();
            }

            ShowAllNoti();
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
        dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dpDialog.show();

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            isdateSelected = true;
            strSelectedDate = GlobalData.getFormatedcurrentDate(dayOfMonth, monthOfYear + 1, year);

            if (gd.isConnectingToInternet()) {
                String[] keys = {"date", "user_id"};
                String[] value = {strSelectedDate, PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, "")};

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

    public void RejectAccept(String acceptReject, NotificationModel item, String notiRemark) {
        if (notiRemark.equalsIgnoreCase("")) {
            notiRemark = "Rejected by " + PreferenceConnector.readString(svContext, PreferenceConnector.YODDHAID, "");
        }

        if (gd.isConnectingToInternet()) {
            String[] keys = {"user_id", "notification_id", "status", "notificationremark"};
            String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
                    item.getId(), acceptReject, notiRemark};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.ACCEPTREJECT, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
        }


    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
