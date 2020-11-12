package in.kamalaenterprize.sncharsuchi;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.database.DatabaseHandler;

public class ScannedBarcodeActivity extends AppCompatActivity implements WebServiceListener {
    Context svContext;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    boolean isAdding = false;
    private String lastScan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);
        svContext = this;
        initViews();

        ImageView backArrow = (ImageView) findViewById(R.id.btn_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentData.length() > 0) {
//                    if (isEmail)
//                        startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
//                    else {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
//                    }
                }
            }
        });

        txtBarcodeValue.setVisibility(View.GONE);
        btnAction.setVisibility(View.GONE);

        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }
    }

    private void initialiseDetectorsAndSources() {
//        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature  
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                OpenQrCodeScanner();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    if (! lastScan.equalsIgnoreCase(barcodes.valueAt(0).displayValue)) {
                        txtBarcodeValue.post(new Runnable() {
                            @Override
                            public void run() {
                                intentData = barcodes.valueAt(0).displayValue;
                                GlobalData gd = new GlobalData(ScannedBarcodeActivity.this);
                                if (gd.isConnectingToInternet()) {
                                    String[] keys = {"name", "date", "yoddhaId", "travelType", "address", "locationLat", "locationLong", "modeOfTravel", "note"};
                                    String[] value = {"", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), intentData, "", "",
                                            "", "",
                                            "", ""};

                                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                                    for (int j = 0; j < keys.length; j++) {
                                        System.out.println(keys[j] + "......." + value[j]);
                                        hash.put(keys[j], value[j]);
                                    }

                                    if (!(PreferenceConnector.readString(svContext, PreferenceConnector.YODDHAID, "")).equalsIgnoreCase(intentData)) {
                                        if (!isAdding) {
                                            callWebService(GlobalVariables.ADDTRAVELHISTORY, hash, "");
                                        }
                                    } else {
                                        ShowCustomView.showCustomToast("You can't add yourself Sanchaar-Id", svContext, ShowCustomView.ToastyError);
                                    }
                                } else {
                                    String[] offlineData = {"", GlobalData.getcurrentDate() + " " + GlobalData.getcurrentTime(), intentData, "", "",
                                            "", "", "", ""};
                                    AddOfflineData(offlineData);
                                }

//                            if (barcodes.valueAt(0).email != null) {
//                                txtBarcodeValue.removeCallbacks(null);
//                                intentData = barcodes.valueAt(0).email.address;
//                                txtBarcodeValue.setText(intentData);
//                                isEmail = true;
//                                btnAction.setText("ADD CONTENT TO THE MAIL");
//                            } else {
//                                isEmail = false;
//                                btnAction.setText("LAUNCH URL");
//                                intentData = barcodes.valueAt(0).displayValue;
//                                txtBarcodeValue.setText(intentData);
//                            }
                            }
                        });
                    }
                }
            }
        });
    }

    private void OpenQrCodeScanner() {
        Dexter.withActivity(ScannedBarcodeActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        try {
                            cameraSource.start(surfaceView.getHolder());
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
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

    DatabaseHandler db;

    private void AddOfflineData(String[] offlineData) {
        db = new DatabaseHandler(svContext);
        db.addData(offlineData, 0);
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        isAdding = true;
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    public static final String TAG_TRAVELTYPE = "traveltype";
    public static final String TAG_TRAVELMODE = "travelmode";
    public static final String TAG_RESULT = "result";
    public static final String TAG_MSG = "msg";

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
            isAdding = false;
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


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}  