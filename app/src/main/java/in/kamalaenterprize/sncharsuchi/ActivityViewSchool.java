//package in.ksinfoteck.yoddha;
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
//import android.widget.TextView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import in.ksinfoteck.adapter.ViewAdapter;
//import in.ksinfoteck.commonutility.GlobalData;
//import in.ksinfoteck.commonutility.GlobalVariables;
//import in.ksinfoteck.commonutility.PreferenceConnector;
//import in.ksinfoteck.commonutility.ShowCustomView;
//import in.ksinfoteck.commonutility.WebService;
//import in.ksinfoteck.commonutility.WebServiceListener;
//import in.ksinfoteck.commonutility.WebServiceWithoutPreUrl;
//import in.ksinfoteck.database.CSVFile;
//import in.ksinfoteck.database.DatabaseHandler;
//import in.ksinfoteck.model.TravelHistoryModel;
//
//public class ActivityViewSchool extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
//    private Context svContext;
//    private Typeface font;
//    private GlobalData gd;
//    private ImageView imgBack;
//    private RecyclerView rv_contact;
//    private List<TravelHistoryModel> lstData = new ArrayList<TravelHistoryModel>();
//    private ViewAdapter mAdapter;
//    private Button LoadData, uploadData, showData;
//
//    public static final String TAG_SCHOOL = "school";
//    public static final String TAG_LATITUDE = "latitude";
//    public static final String TAG_DESCRIPTION = "description";
//    public static final String TAG_CREATED_AT = "created_at";
//    public static final String TAG_FOLLOWUP_DATE = "followup_date";
//    public static final String TAG_SCHOOL_CONTACT1_NAME = "school_contact1_name";
//    public static final String TAG_CITY_NAME = "city_name";
//    public static final String TAG_PASSWORD = "password";
//    public static final String TAG_SCHOOL_ID = "school_id";
//    public static final String TAG_UPDATED_AT = "updated_at";
//    public static final String TAG_STATE_NAME = "state_name";
//    public static final String TAG_SCHOOL_ADDRESS = "school_address";
//    public static final String TAG_STATE_ID = "state_id";
//    public static final String TAG_EMAIL = "email";
//    public static final String TAG_LONGITUDE = "longitude";
//    public static final String TAG_MEETING_PERSON_NUMBER = "meeting_person_number";
//    public static final String TAG_IMAGE = "image";
//    public static final String TAG_ADDRESS = "address";
//    public static final String TAG_SCHOOL_STATUS = "school_status";
//    public static final String TAG_ID_PROOF = "id_proof";
//    public static final String TAG_MOBILE = "mobile";
//    public static final String TAG_SCHOOL_NAME = "school_name";
//    public static final String TAG_EMPLOYEE_NAME = "employee_name";
//    public static final String TAG_SCHOOL_CONTACT1 = "school_contact1";
//    public static final String TAG_SCHOOL_EMAIL = "school_email";
//    public static final String TAG_SCHOOL_CONTACT2 = "school_contact2";
//    public static final String TAG_MEETING_PERSON_NAME = "meeting_person_name";
//    public static final String TAG_EMPLOYEE_ID = "employee_id";
//    public static final String TAG_SCHOOL_CONTACT2_NAME = "school_contact2_name";
//    public static final String TAG_REFFRAL_NAME = "reffral_name";
//    public static final String TAG_CREATED_DATE = "created_date";
//    public static final String TAG_SCHOOL_PINCODE = "school_pincode";
//    public static final String TAG_MODIFY_DATE = "modify_date";
//    public static final String TAG_EMP_ID = "emp_id";
//    public static final String TAG_CITY_ID = "school_city_id";
//    public static final String TAG_STATUS = "status";
//    public static final String TAG_DATA = "data";
//    public static final String TAG_MSG = "msg";
//
//    public static final String TAG_REMARK_CREATED_AT = "remark_created_at";
//    public static final String TAG_REMARK_UPDATED_AT = "remark_updated_at";
//    public static final String TAG_REMARK_STATUS = "remark_status";
//    public static final String TAG_REMARK_ID = "remark_id";
//    public static final String TAG_REMARK_DES = "remark_des";
//
//    String SNoUniqueNo = "";
//    int tableNumber = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_viewschool);
//        svContext = this;
//        gd = new GlobalData(svContext);
//        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
//            font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
//            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
//            GlobalData.setFont(root, font);
//        }
//
//        LoadData = (Button) findViewById(R.id.load_data);
//        uploadData = (Button) findViewById(R.id.upload_data);
//        showData = (Button) findViewById(R.id.show_data);
//
//        imgBack = (ImageView) findViewById(R.id.img_back);
//        rv_contact = (RecyclerView) findViewById(R.id.list);
//
//        TopWithBack("View Schools added");
//
//        imgBack.setOnClickListener(this);
//        LoadData.setOnClickListener(this);
//        showData.setOnClickListener(this);
//        uploadData.setOnClickListener(this);
//
//    }
//
////    private class LoadDataFromExcel extends AsyncTask<String, String, String> {
////        ProgressDialog progressDialog;
////
////        @Override
////        protected void onPreExecute() {
////            progressDialog = new ProgressDialog(svContext);
////            progressDialog.setMessage(PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, ""));
////            progressDialog.setTitle("Loading");
////            progressDialog.show();
////            progressDialog.setCancelable(false);
////        }
////
////        @Override
////        protected String doInBackground(String... params) {
////            String strId = PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, "");
////            if (strId.equals(FragDashboard.strSlideMenuIds[0])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.nursery));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[1])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.lkg));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[2])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.ukg));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[3])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_one_gem));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[4])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_two_gem));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[5])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_two_pearl));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[6])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_three_gem));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[7])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_three_pearl));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[8])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_four_gem));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[9])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_four_pearl));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[10])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_six_gem));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[11])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_six_pearl));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[12])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_seven_gem));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[13])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_seven_pearl));
////            } else if (strId.equals(FragDashboard.strSlideMenuIds[14])) {
////                LoadDataToDB(getResources().openRawResource(R.raw.class_seven_ruby));
////            }
////            return "";
////        }
////
////        @Override
////        protected void onPostExecute(String result) {
////            if (progressDialog.isShowing()) {
////                progressDialog.dismiss();
////                GlobalData.showToast("Process Done", svContext);
////            }
////        }
////    }
//
//    private void SaveDBToSqlOne(String showText, String[] value) {
//        SNoUniqueNo = value[7];
//        tableNumber = 0;
//        DatabaseHandler db = new DatabaseHandler(svContext);
//        if (gd.isConnectingToInternet()) {
//            String[] keys = DatabaseHandler.fieldsNames[0];
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int j = 0; j < keys.length; j++) {
//                System.out.println(keys[j] + "......." + value[j]);
//                hash.put(keys[j], value[j]);
//            }
//            callWebService(GlobalVariables.PUTDATATABLEONE, hash, showText);
//        } else {
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//        }
//    }
//
//    private void SaveDBToSqlTwo(String showText, String[] value) {
//        SNoUniqueNo = value[7];
//        tableNumber = 1;
//        DatabaseHandler db = new DatabaseHandler(svContext);
//        if (gd.isConnectingToInternet()) {
//            String[] keys = DatabaseHandler.fieldsNames[1];
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int j = 0; j < keys.length; j++) {
//                System.out.println(keys[j] + "......." + value[j]);
//                hash.put(keys[j], value[j]);
//            }
//            callWebService(GlobalVariables.PUTDATATABLETWO, hash, showText);
//        } else {
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//        }
//    }
//
//    private void SaveDBToSqlThree(String showText, String[] value) {
//        SNoUniqueNo = value[7];
//        tableNumber = 2;
//        DatabaseHandler db = new DatabaseHandler(svContext);
//        if (gd.isConnectingToInternet()) {
//            String[] keys = DatabaseHandler.fieldsNames[2];
////            String[] value = (db.getData(db.tablenames[2])).get(pos);
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int j = 0; j < keys.length; j++) {
//                System.out.println(keys[j] + "......." + value[j]);
//                hash.put(keys[j], value[j]);
//            }
//            callWebService(GlobalVariables.PUTDATATABLETHREE, hash, showText);
//        } else {
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//        }
//    }
//
//    private void SaveDBToSqlFour(String showText, String[] value) {
//        SNoUniqueNo = value[7];
//        tableNumber = 3;
//        DatabaseHandler db = new DatabaseHandler(svContext);
//        if (gd.isConnectingToInternet()) {
//            String[] keys = DatabaseHandler.fieldsNames[3];
////            String[] value = (db.getData(db.tablenames[2])).get(pos);
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int j = 0; j < keys.length; j++) {
//                System.out.println(keys[j] + "......." + value[j]);
//                hash.put(keys[j], value[j]);
//            }
//            callWebService(GlobalVariables.PUTDATATABLEFOUR, hash, showText);
//        } else {
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//        }
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
//            case R.id.img_back:
//                onBackPressed();
//                break;
//            case R.id.load_data:
////                new LoadDataFromExcel().execute();
//                break;
//            case R.id.upload_data:
//                try {
//                    String strId = PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, "");
//                    if (strId.equals(FragDashboard.strSlideMenuIds[0]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[1]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[2])) {
//
//                        DatabaseHandler db = new DatabaseHandler(svContext);
//                        List<String[]> lstShowData = new ArrayList<>();
//                        lstShowData = (db.getData(db.tablenames[0]));
//                        for (int i = 0; i < lstShowData.size(); i++) {
//                            SaveDBToSqlOne("Loading Data " + i, lstShowData.get(i));
//                        }
//
//
//                    } else if (strId.equals(FragDashboard.strSlideMenuIds[3]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[4]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[5])) {
//
//                        DatabaseHandler db = new DatabaseHandler(svContext);
//                        List<String[]> lstShowData = new ArrayList<>();
//                        lstShowData = (db.getData(db.tablenames[1]));
//                        for (int i = 0; i < lstShowData.size(); i++) {
//                            SaveDBToSqlTwo("Loading Data " + i, lstShowData.get(i));
//                        }
//                    } else if (strId.equals(FragDashboard.strSlideMenuIds[6]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[7]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[8]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[9])) {
//
//                        DatabaseHandler db = new DatabaseHandler(svContext);
//                        List<String[]> lstShowData = new ArrayList<>();
//                        lstShowData = (db.getData(db.tablenames[2]));
//                        for (int i = 0; i < lstShowData.size(); i++) {
//                            SaveDBToSqlThree("Loading Data " + i, lstShowData.get(i));
//                        }
//                    } else if (strId.equals(FragDashboard.strSlideMenuIds[10]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[11]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[12]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[13]) ||
//                            strId.equals(FragDashboard.strSlideMenuIds[14])) {
//                        DatabaseHandler db = new DatabaseHandler(svContext);
//                        List<String[]> lstShowData = new ArrayList<>();
//                        lstShowData = (db.getData(db.tablenames[3]));
//                        for (int i = 0; i < lstShowData.size(); i++) {
//                            SaveDBToSqlFour("Loading Data " + i, lstShowData.get(i));
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.show_data:
//                try {
//                    ShowDbData();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//
//    private void ShowDbData() {
//        List<String[]> lstShowData = new ArrayList<>();
//        DatabaseHandler db = new DatabaseHandler(svContext);
//        String strId = PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, "");
//        if (strId.equals(FragDashboard.strSlideMenuIds[0])) {
//            lstShowData = db.getData(db.tablenames[0]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[1])) {
//            lstShowData = db.getData(db.tablenames[0]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[2])) {
//            lstShowData = db.getData(db.tablenames[0]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[3])) {
//            lstShowData = db.getData(db.tablenames[1]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[4])) {
//            lstShowData = db.getData(db.tablenames[1]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[5])) {
//            lstShowData = db.getData(db.tablenames[1]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[6])) {
//            lstShowData = db.getData(db.tablenames[2]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[7])) {
//            lstShowData = db.getData(db.tablenames[2]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[8])) {
//            lstShowData = db.getData(db.tablenames[2]);
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[9])) {
//            lstShowData = db.getData(db.tablenames[2]);
//        } else {
//            lstShowData = db.getData(db.tablenames[3]);
//        }
//
//        LoadDataFromDB(lstShowData);
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
//        WebServiceWithoutPreUrl webService = new WebServiceWithoutPreUrl(svContext, postUrl, hash, this, WebService.POST, showText);
//        webService.execute();
//    }
//
//    public static final String TAG_RESULT = "result";
//    public static final String TAG_SUCCESS = "success";
//
//    @Override
//    public void onWebServiceActionComplete(String result, String url) {
//        System.out.println(result + ".........jsonresponse....." + url);
//
//        try {
//            lstData = new ArrayList<>();
//            JSONObject json = new JSONObject(result);
//
//            String str_result = json.getString(TAG_RESULT);
//            String str_success = json.getString(TAG_SUCCESS);
//
//            DatabaseHandler db = new DatabaseHandler(this);
//            if (str_result.equals("true")) {
//                db.deleteTableWithId(db.tablenames[tableNumber], db.fieldsNames[tableNumber][7], SNoUniqueNo);
//                GlobalData.showToast(str_success, svContext);
//                showData.performClick();
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<String[]> LoadDataToDB(InputStream inputFS) {
//        List<String[]> lstShowData = new ArrayList<>();
//        try {
//            CSVFile csvFile = new CSVFile(inputFS, svContext);
//            List<String[]> lstData = csvFile.read();
//
//            System.out.println(lstData.get(0)[0] + lstData.get(0)[1] + "etc.......sriva........" + lstData.size()
//                    + "etc.......sriva........" + lstData.get(0).length);
//
//            DatabaseHandler db = new DatabaseHandler(svContext);
//
//            String strId = PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, "");
//            if (strId.equals(FragDashboard.strSlideMenuIds[0])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 0);
//                }
//                lstShowData = db.getData(db.tablenames[0]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[1])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 0);
//                }
//                lstShowData = db.getData(db.tablenames[0]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[2])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 0);
//                }
//                lstShowData = db.getData(db.tablenames[0]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[3])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 1);
//                }
//                lstShowData = db.getData(db.tablenames[1]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[4])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 1);
//                }
//                lstShowData = db.getData(db.tablenames[1]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[5])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 1);
//                }
//                lstShowData = db.getData(db.tablenames[1]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[6])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 2);
//                }
//                lstShowData = db.getData(db.tablenames[2]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[7])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 2);
//                }
//                lstShowData = db.getData(db.tablenames[2]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[8])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 2);
//                }
//                lstShowData = db.getData(db.tablenames[2]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[9])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 2);
//                }
//                lstShowData = db.getData(db.tablenames[2]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[10])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 3);
//                }
//                lstShowData = db.getData(db.tablenames[3]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[11])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 3);
//                }
//                lstShowData = db.getData(db.tablenames[3]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[12])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 3);
//                }
//                lstShowData = db.getData(db.tablenames[3]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[13])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 3);
//                }
//                lstShowData = db.getData(db.tablenames[3]);
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[14])) {
//                for (int i = 0; i < lstData.size(); i++) {
//                    db.addData(lstData.get(i), 3);
//                }
//                lstShowData = db.getData(db.tablenames[3]);
//            }
//
////            LoadDataFromDB(lstShowData);
//            System.out.println("etc.......sriva........" + lstShowData.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return lstShowData;
//    }
//
//    private void LoadDataFromDB(List<String[]> lstData) {
//        if (lstData.size() == 0) {
//            rv_contact.setVisibility(View.INVISIBLE);
////            clearData(lstData, mAdapter);
//            ShowCustomView.showCustomToast(getResources().getString(R.string.error_nodata, svContext, ShowCustomView.ToastyInfo);
//        } else {
//            rv_contact.setVisibility(View.VISIBLE);
//            ActivityViewSchool frag = this;
//            mAdapter = new ViewAdapter(lstData, svContext, frag);
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(svContext, 1);
//            rv_contact.setLayoutManager(mLayoutManager);
//            rv_contact.setAdapter(mAdapter);
//        }
//    }
//
//    public void clearData(List<TravelHistoryModel> lstData, ViewAdapter mAdapter) {
//        lstData.clear(); //clear list
//        if (mAdapter != null)
//            mAdapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
//    }
//
//    public void ViewRemark(String str_school_id) {
//        if (gd.isConnectingToInternet()) {
//            String[] keys = {"school_id"};
//            String[] value = {str_school_id};
//
//            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//            for (int i = 0; i < keys.length; i++) {
//                System.out.println(keys[i] + "......." + value[i]);
//                hash.put(keys[i], value[i]);
//            }
//            callWebService(GlobalVariables.REMARKLIST, hash, "");
//        } else {
//            ShowCustomView.showInternetAlert(svContext);
//        }
//    }
//
//    public void DeleteData(String[] value) {
//        SNoUniqueNo = value[7];
//
//        String strId = PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, "");
//        if (strId.equals(FragDashboard.strSlideMenuIds[0]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[1]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[2])) {
//
//            tableNumber = 0;
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[3]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[4]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[5])) {
//
//            tableNumber = 1;
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[6]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[7]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[8]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[9])) {
//
//            tableNumber = 2;
//        } else if (strId.equals(FragDashboard.strSlideMenuIds[10]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[11]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[12]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[13]) ||
//                strId.equals(FragDashboard.strSlideMenuIds[14])) {
//            tableNumber = 3;
//        }
//        try {
//            DatabaseHandler db = new DatabaseHandler(this);
//            db.deleteTableWithId(db.tablenames[tableNumber], db.fieldsNames[tableNumber][7], SNoUniqueNo);
//        showData.performClick();
//
//            ShowCustomView.showCustomToast("Delete Successfully", svContext, ShowCustomView.ToastySuccess);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public void UploadData(String[] value) {
//        try {
//            SNoUniqueNo = value[7];
//            String strId = PreferenceConnector.readString(svContext, PreferenceConnector.ClASSUPLOADID, "");
//            if (strId.equals(FragDashboard.strSlideMenuIds[0]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[1]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[2])) {
//
//                tableNumber = 0;
//
//                if (gd.isConnectingToInternet()) {
//                    String[] keys = DatabaseHandler.fieldsNames[0];
//
//                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//                    for (int j = 0; j < keys.length; j++) {
//                        System.out.println(keys[j] + "......." + value[j]);
//                        hash.put(keys[j], value[j]);
//                    }
//                    callWebService(GlobalVariables.PUTDATATABLEONE, hash, "Uploading");
//                } else {
//                    ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//                }
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[3]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[4]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[5])) {
//
//                tableNumber = 1;
//
//                if (gd.isConnectingToInternet()) {
//                    String[] keys = DatabaseHandler.fieldsNames[1];
//
//                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//                    for (int j = 0; j < keys.length; j++) {
//                        System.out.println(keys[j] + "......." + value[j]);
//                        hash.put(keys[j], value[j]);
//                    }
//                    callWebService(GlobalVariables.PUTDATATABLETWO, hash, "Uploading");
//                } else {
//                    ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//                }
//
//
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[6]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[7]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[8]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[9])) {
//
//                tableNumber = 2;
//
//                if (gd.isConnectingToInternet()) {
//                    String[] keys = DatabaseHandler.fieldsNames[2];
//
//                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//                    for (int j = 0; j < keys.length; j++) {
//                        System.out.println(keys[j] + "......." + value[j]);
//                        hash.put(keys[j], value[j]);
//                    }
//                    callWebService(GlobalVariables.PUTDATATABLETHREE, hash, "Uploading");
//                } else {
//                    ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//                }
//            } else if (strId.equals(FragDashboard.strSlideMenuIds[10]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[11]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[12]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[13]) ||
//                    strId.equals(FragDashboard.strSlideMenuIds[14])) {
//                tableNumber = 3;
//
//                if (gd.isConnectingToInternet()) {
//                    String[] keys = DatabaseHandler.fieldsNames[3];
//
//                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
//                    for (int j = 0; j < keys.length; j++) {
//                        System.out.println(keys[j] + "......." + value[j]);
//                        hash.put(keys[j], value[j]);
//                    }
//                    callWebService(GlobalVariables.PUTDATATABLEFOUR, hash, "Uploading");
//                } else {
//                    ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//}
