package in.kamalaenterprize.sncharsuchi;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.adapter.AdapterApproveAlert;
import in.kamalaenterprize.commonutility.CustomeProgressDialog;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.model.ApproveAlertModel;
import in.kamalaenterprize.model.LabDetailModel;

public class ActivityApproveAlert extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private View parent_view;
    private RecyclerView recyclerView;
    private AdapterApproveAlert mAdapter;
    private ImageView imgBack;
    private List<ApproveAlertModel> lstData = new ArrayList<>();
    private int animation_type = ItemAnimation.FADE_IN;
    private TextView txtNodata;

    public static final String TAG_RESOURCES="resources";
    public static final String TAG_RECORDID="recordid";
    public static final String TAG_CITY="city";
    public static final String TAG_DESCRIPTIONANDORSERVICEPROVIDED="descriptionandorserviceprovided";
    public static final String TAG_CONTACT="contact";
    public static final String TAG_PHONENUMBER="phonenumber";
    public static final String TAG_STATE="state";
    public static final String TAG_CATEGORY="category";
    public static final String TAG_NAMEOFTHEORGANISATION="nameoftheorganisation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvealert);
        parent_view = findViewById(android.R.id.content);
        svContext = this;
        gd = new GlobalData(svContext);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtNodata = (TextView) findViewById(R.id.txt_nodata);

        imgBack.setOnClickListener(this);

        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISNOTIFICATION, false);

        ShowAllNoti();
        LoadLabDetail();
    }

    private void ShowAllNoti() {
        if (gd.isConnectingToInternet()) {
            String[] keys = {""};
            String[] value = {""};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.GETUNVERIFIEDALERT, hash, "");
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

            txtNodata.setText(getResources().getString(R.string.error_nodata));
        } else {
            txtNodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
            recyclerView.setHasFixedSize(true);

            ActivityApproveAlert act = this;
            mAdapter = new AdapterApproveAlert(this, lstData, animation_type, act);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.GETUNVERIFIEDALERT)) {
            lstData = new ArrayList<ApproveAlertModel>();
            try {
                JSONObject json = new JSONObject(result);
//                {"status":false,"msg":"No data found"}

                String str_result = json.getString("status");

                if (str_result.equalsIgnoreCase("success")) {
                    JSONArray notifications = json.getJSONArray("data");
                    for (int notifications_i = 0; notifications_i < notifications.length(); notifications_i++) {
                        JSONObject notifications_obj = notifications.getJSONObject(notifications_i);
                        String id = notifications_obj.getString("id");
                        String sender_id = notifications_obj.getString("sender_id");
                        String sender_yoddha = notifications_obj.getString("sender_yoddha");
                        String labname = notifications_obj.getString("labname");
                        String report_number = notifications_obj.getString("report_number");
                        String status = notifications_obj.getString("status");
                        String fullname = notifications_obj.getString("fullname");
                        String contactnumber = notifications_obj.getString("labnumber");

                        lstData.add(new ApproveAlertModel(id, sender_id, sender_yoddha, labname, report_number, status, fullname, contactnumber, false));
                    }
                } else {
                    String str_msg = json.getString("msg");
//                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                GlobalData.showToast("json error occured", svContext);
                e.printStackTrace();
            }

            initComponent();
        }else if (url.contains(GlobalVariables.ADDTPOSITIVEREPORT)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString("result");
                String str_msg = json.getString("msg");

                if (str_result.equals("success")) {
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastySuccess);
                } else {
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
            ShowAllNoti();
        }else if (url.contains(GlobalVariables.ACCEPTREJECT)) {
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
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST);
        webService.execute();
    }

    @Override
    public void onClick(View view) {
        int response;
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    public void RejectAccept(String acceptReject, ApproveAlertModel item, String notiRemark) {
        if (notiRemark.equalsIgnoreCase("")) {
            notiRemark = "Rejected by admin";
        }

        if (gd.isConnectingToInternet()) {
            String[] keys = {"id", "status", "remark", "user_id"};
            String[] value = {item.getId(), acceptReject, notiRemark, PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, "")};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.ADDTPOSITIVEREPORT, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
        }
    }

    private List<LabDetailModel> lstLabDetail = new ArrayList<>();
    private void LoadLabDetail() {
        CustomeProgressDialog customeProgressDialog	=	new CustomeProgressDialog(svContext, R.layout.custom_progess_dialog);
        customeProgressDialog.setCancelable(false);
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
