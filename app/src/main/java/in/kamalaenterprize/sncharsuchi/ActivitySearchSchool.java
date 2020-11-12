//package in.ksinfoteck.learnerstreetemp;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import in.ksinfoteck.adapter.SeachSchoolAdapter;
//import in.ksinfoteck.commonutility.GPSTracker;
//import in.ksinfoteck.commonutility.GlobalData;
//import in.ksinfoteck.commonutility.GlobalVariables;
//import in.ksinfoteck.commonutility.ShowCustomView;
//import in.ksinfoteck.commonutility.WebService;
//import in.ksinfoteck.commonutility.WebServiceListener;
//import in.ksinfoteck.database.DatabaseHandler;
//import in.ksinfoteck.model.TravelHistoryModel;
//
//public class ActivitySearchSchool extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
//    private Context svContext;
//    private Button btnsignUp;
//    private Typeface font;
//    private GlobalData gd;
//    private ImageView imgBack;
//    private DatabaseHandler db;
//    private GPSTracker gps;
//    private Spinner dropDownViewCategory, dropDownViewSubCategory;
//    private RecyclerView rv_contact;
//    private List<TravelHistoryModel> lstData = new ArrayList<TravelHistoryModel>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_searchschool);
//        svContext = this;
//        gps = new GPSTracker(this);
//        gd = new GlobalData(svContext);
//        db = new DatabaseHandler(svContext);
//        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
//            font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
//            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
//            GlobalData.setFont(root, font);
//        }
//
//
////        btnsignUp = (Button) findViewById(R.id.btn_viewcontact);
//        imgBack = (ImageView) findViewById(R.id.img_back);
//        dropDownViewCategory = (Spinner) findViewById(R.id.spin_category);
////        dropDownViewSubCategory = (Spinner) findViewById(R.id.spin_subcategory);
//        rv_contact = (RecyclerView) findViewById(R.id.list);
//
//        TopWithBack("Add Contact");
//
//
////        btnsignUp.setOnClickListener(this);
//        imgBack.setOnClickListener(this);
//
//    }
//
//    private void TopWithBack(String strHead) {
//        ImageView imgback = (ImageView) findViewById(R.id.img_back);
//        imgback.setOnClickListener(this);
//
//        TextView txtHeading = (TextView) findViewById(R.id.heading);
//        txtHeading.setText(strHead);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int response;
//        switch (view.getId()) {
////            case R.id.btn_viewcontact:
////                ViewContacts();
////                break;
//            case R.id.img_back:
//                onBackPressed();
//                break;
//        }
//    }
//
//    private void ViewContacts() {
//        int response = 0;
//
//
//
//        if (response == 0) {
//            if (gd.isConnectingToInternet()) {
//                String[] keys = {"_id"};
//                String[] value = {""};
//
//                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//                for (int j = 0; j < keys.length; j++) {
//                    System.out.println(keys[j] + "......." + value[j]);
//                    hash.put(keys[j], value[j]);
//                }
//
//                callWebService(GlobalVariables.SEARCHSCHOOL, hash, "");
//            } else {
//               ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyInfo);
//            }
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//    }
//
//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
//        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
//        webService.execute();
//    }
//
//    @Override
//    public void onWebServiceActionComplete(String result, String url) {
//        System.out.println(result + ".........jsonresponse....." + url);
//
//        if (url.contains(GlobalVariables.SEARCHSCHOOL)) {
//            try {
//                lstData = new ArrayList<>();
//                JSONObject json = new JSONObject(result);
////                JSONObject data_obj = json.getJSONObject(TAG_DATA);
////                String str_msg = data_obj.getString(TAG_MSG);
////                String str_status = data_obj.getString(TAG_STATUS);
//
//
//            } catch (JSONException e) {
//                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
//                e.printStackTrace();
//            }
//            LoadData(lstData, false);
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
//
//    SeachSchoolAdapter mAdapter;
//    private void LoadData(List<TravelHistoryModel> lstData, boolean isOfflneLoad) {
//        if (lstData.size() == 0) {
//            rv_contact.setVisibility(View.INVISIBLE);
//            clearData(lstData, mAdapter);
//            ShowCustomView.showCustomToast("No data to show", svContext, ShowCustomView.ToastyInfo);
//        } else {
//            rv_contact.setVisibility(View.VISIBLE);
//            ActivitySearchSchool frag = this;
//            mAdapter = new SeachSchoolAdapter(lstData, svContext, frag, isOfflneLoad);
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(svContext, 1);
//            rv_contact.setLayoutManager(mLayoutManager);
//            rv_contact.setAdapter(mAdapter);
//        }
//    }
//
//    public void clearData(List<TravelHistoryModel> lstData, SeachSchoolAdapter mAdapter) {
//        lstData.clear(); //clear list
//        if (mAdapter != null)
//            mAdapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
//    }
//}
