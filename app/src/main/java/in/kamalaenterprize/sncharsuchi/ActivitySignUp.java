package in.kamalaenterprize.sncharsuchi;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.SpinnerPopulateAdapter;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener, WebServiceListener, BSImagePicker.OnSingleImageSelectedListener,
        UCropFragmentCallback {
    private Context svContext;
    private Typeface font;
    private GlobalData gd;
    private EditText edPersonName;
    private Button btnsignUp, btnRemovePic;
    private Spinner dropDownView;
    private RadioGroup radioGender;
    private ImageView imgProfilePic;
    public static final String TAG_STATES = "states";
    public static final String TAG_CODE = "code";
    public static final String TAG_NAME = "name";
    public static final String TAG_RESULT = "result";
    public static final String TAG_LOGINDETAIL = "logindetail";
    public static final String TAG_YODDHAID = "yoddhaid";
    public static final String TAG_PIN = "pin";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_ID = "id";
    public static final String TAG_STATE = "state";
    public static final String TAG_GENDER = "Gender";
    public static final String TAG_EMAIL = "email";
    public Uri imageUri = null;
    private String imgPath;
    private Button dialogCancel;
    private RelativeLayout openGallery, openCamera;
    private RelativeLayout rlayImageSelect;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    protected static final int REQUEST_CAMERA_ACCESS_PERMISSION = 103;
    private static final int TAKE_PICTURE = 2;
    private static final int BROWSE_PICTURE = 1;
    private UCropFragment fragment;
    private boolean mShowLoader;
    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;
    private AlertDialog mAlertDialog;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "CropImage";
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        svContext = this;
        gd = new GlobalData(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        edPersonName = (EditText) findViewById(R.id.et_name);
        dropDownView = (Spinner) findViewById(R.id.state);
        btnsignUp = (Button) findViewById(R.id.email_sign_in_button);
        radioGender = (RadioGroup) findViewById(R.id.gender);
        imgProfilePic = (ImageView) findViewById(R.id.imgae_dp);
        btnRemovePic = (Button) findViewById(R.id.btn_removepic);

        btnsignUp.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
        btnRemovePic.setOnClickListener(this);


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

        rlayImageSelect = (RelativeLayout) findViewById(R.id.uploadimage);
        openGallery = (RelativeLayout) findViewById(R.id.dialog_fromgallery);
        openCamera = (RelativeLayout) findViewById(R.id.dialog_fromcamera);
        dialogCancel = (Button) findViewById(R.id.dialog_cancel);

        openGallery.setOnClickListener(this);
        openCamera.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

    }


    List<String> lstStates = new ArrayList<String>();
    String selectedStateCode = "";

    private void populateStates() {
        SpinnerAdapter spinnerAdapter = new SpinnerPopulateAdapter(svContext, lstStates, true);
        dropDownView.setAdapter(spinnerAdapter);

        dropDownView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View vi, int selectedPos, long arg3) {
                TextView txtView = ((TextView) vi.findViewById(R.id.txtitem));
                txtView.setTextColor(getResources().getColor(R.color.fontcolordark));
                txtView.setGravity(Gravity.CENTER_VERTICAL);

                selectedStateCode = lstStates.get(selectedPos).split("#:#")[0];

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

//    public static Uri imageUri = null;
//    public void SetProfilePic(Context context) {
//        isUpdatedProfilePic = true;
//        imageUri = ActivityBrowseProfileImage.imageUri;
//        ContentResolver cr = svContext.getContentResolver();
//        try {
//            imgProfilePic.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri));
//        } catch (Exception e) {
//            ShowCustomView.showCustomToast("Failed to load", context, ShowCustomView.ToastyError);
//            Log.e("Camera", e.toString());
//        }
//    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    private void updateFCMId(String fcmId) {
        if (gd.isConnectingToInternet()) {
//                String fcmId = PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, "");

            String[] keys = {"phone", "fcm_id"};
            String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.OTPPHONENUMBER, ""), fcmId};

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

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.SIGNUP)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString(TAG_RESULT);
                if (str_result.equals("true")) {
                    String fcmId = PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, "");

                    updateFCMId(fcmId);

                    Intent svIntent = new Intent(svContext, ActivityMain.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                } else {
                    ShowCustomView.showCustomToast(json.getString("msg"), svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        } else if (url.contains(GlobalVariables.LOGIN)) {
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
                    }
                } else {
                    Logout();
                }
            } catch (JSONException e) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                SubmitForm();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.imgae_dp:
//                Intent aiIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
//                startActivity(aiIntent);
                ShowUploadDialog();
                break;
            case R.id.dialog_fromgallery:
                browseImageFromGallery();
                break;
            case R.id.dialog_fromcamera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_write_storage_rationale), REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
                } else {
                    browseImageFromCamera();
                }

                break;
            case R.id.btn_removepic:
                if (imageUri == null) {
                    ShowCustomView.showCustomToast("No image to remove", svContext, ShowCustomView.ToastyError);
                } else {
                    imageUri = null;
                    imgProfilePic.setImageDrawable(null);
                    imgProfilePic.setBackgroundResource(R.drawable.dprofile);
                }
                break;
            case R.id.dialog_cancel:
                HideUploadDialog();
//                finish();
                break;
        }
    }

    private void SubmitForm() {
        int response = 0;
        response = GlobalData.emptyEditTextError(
                new EditText[]{edPersonName},
                new String[]{"enter Person name"});

        String strPersonName = edPersonName.getText().toString().trim();

        int selectedId = radioGender.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);

        if (strPersonName.length() == 0) {
            response++;
            edPersonName.setError("Enter name");
        }

        if (imageUri == null) {
            response++;
            ShowCustomView.showCustomToast(getResources().getString(R.string.signup_selfie_error), svContext, ShowCustomView.ToastyError);
        }

        if (response == 0) {
            ShowConfirmDialog(strPersonName, radioButton);
        }
    }

    private void ShowConfirmDialog(String strPersonName, RadioButton radioButton) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(svContext);
        alertDialog.setTitle(getResources().getString(R.string.signup_selfietext));
        alertDialog.setPositiveButton("Yes Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (gd.isConnectingToInternet()) {
                    String[] keys = {"name", "state", "gender", "phone", "fcm_id", "pin", "image"};
                    String[] value = {strPersonName, selectedStateCode, radioButton.getText().toString(),
                            PreferenceConnector.readString(svContext, PreferenceConnector.OTPPHONENUMBER, ""), "", "",
                            imgPath};

                    LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                    for (int j = 0; j < keys.length; j++) {
                        System.out.println(keys[j] + "......." + value[j]);
                        hash.put(keys[j], value[j]);
                    }

                    String[] uploadDocsath = {imageUri.getPath()};
                    callWebService(GlobalVariables.SIGNUP, hash, "Uploading post", uploadDocsath);


//                            WebService webService = new WebService(svContext, GlobalVariables.SIGNUP, hash, ActivitySignUp.this, WebService.POSTWITHIMAGE,
//                                    imageUri.getPath(), "");
//                            webService.execute();
                } else {
                    ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
                }
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

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String strtext, String[] imgPath) {
        WebService webService = new WebService(svContext, "", postUrl, hash, this, WebService.POSTWITHIMAGE, imgPath, strtext);
        webService.execute();
    }

    public String getImagePath() {
        return imgPath;
    }

    private void browseImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
//                    .setType("image/*")
//                    .addCategory(Intent.CATEGORY_OPENABLE);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                String[] mimeTypes = {"image/jpeg", "image/png"};
//                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//            }
//            startActivityForResult(Intent.createChooser(intent, "Select Image"), BROWSE_PICTURE);

            BSImagePicker pickerDialog = new BSImagePicker.Builder("in.kamalaenterprize.sncharsuchi.fileprovider")
                    .setMaximumDisplayingImages(24) //Default: Integer.MAX_VALUE. Don't worry about performance :)
                    .setSpanCount(3) //Default: 3. This is the number of columns
                    .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                    .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                    .hideCameraTile() //Default: show. Set this if you don't want user to take photo.
//                    .hideGalleryTile() //Default: show. Set this if you don't want to further let user select from a gallery app. In such case, I suggest you to set maximum     displaying    images to Integer.MAX_VALUE.
                    .build();
            pickerDialog.show(getSupportFragmentManager(), "picker");
        }
    }

    private void browseImageFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.CAMERA,
                    "Camera permission is needed to click images.",
                    REQUEST_CAMERA_ACCESS_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File createDir = new File(Environment.getExternalStorageDirectory(), "/Sanchar Suchi" + "/");
                File photoFile = new File(Environment.getExternalStorageDirectory(), "/Sanchar Suchi" + "/photo_" + timeStamp + ".jpg");
                if (!createDir.exists()) {
                    createDir.mkdirs();
                }
//              imageUri = Uri.fromFile(photoFile);
                imgPath = photoFile.getAbsolutePath();

                imageUri = FileProvider.getUriForFile(svContext,
                        getString(R.string.file_provider_authority),
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }
    }


    private void ShowUploadDialog() {
        rlayImageSelect.setVisibility(View.VISIBLE);
    }

    private void HideUploadDialog() {
        rlayImageSelect.setVisibility(View.GONE);
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + GlobalData.getUniqueString();
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

//        if (BROWSE_PICTURE == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
//            setupFragment(uCrop);
//        } else {                                                        // else start uCrop Activity
//
//        }

        uCrop.start(this);

    }

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {
//        uCrop = uCrop.useSourceImageAspectRatio();
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = uCrop.withMaxResultSize(600, 600);

        return uCrop;
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

//        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100); //From 1 to 100
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

//        options.setMaxScaleMultiplier(5);
//        options.setImageToCropBoundsAnimDuration(666);
//        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);

        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */
       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */

        return uCrop.withOptions(options);
    }

    //    private CropImage listener;
    private void handleCropResult(@NonNull Intent result) {
        imageUri = UCrop.getOutput(result);
        if (imageUri != null) {
//            ResultActivity.startWithUri(this, imageUri);

            ContentResolver cr = svContext.getContentResolver();
            try {
                imgProfilePic.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri));
            } catch (Exception e) {
                ShowCustomView.showCustomToast("Failed to load", svContext, ShowCustomView.ToastyError);
                Log.e("Camera", e.toString());
            }
            dialogCancel.performClick();
//            ActivitySignUp fragment = new ActivitySignUp();
//            (fragment).SetProfilePic(svContext);
//            finish();
//                ivAddImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri));

//            Glide.with(this).load(imageUri).into(ivAddImage);
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.toast_cannot_retrieve_cropped_image), svContext, ShowCustomView.ToastyInfo);
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            ShowCustomView.showCustomToast(cropError.getMessage(), svContext, ShowCustomView.ToastyError);

        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.toast_unexpected_error), svContext, ShowCustomView.ToastyInfo);
        }
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
        }
        removeFragmentFromScreen();
    }

    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        toolbar.setVisibility(View.GONE);
//        settingsView.setVisibility(View.VISIBLE);
    }

//    public void setupFragment(UCrop uCrop) {
//        fragment = uCrop.getFragment(uCrop.getIntent(this).getExtras());
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container, fragment, UCropFragment.TAG)
//                .commitAllowingStateLoss();
//
//        setupViews(uCrop.getIntent(this).getExtras());
//    }

    public void setupViews(Bundle args) {
//        settingsView.setVisibility(View.GONE);
        mStatusBarColor = args.getInt(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_statusbar));
        mToolbarColor = args.getInt(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar));
        mToolbarCancelDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, R.drawable.ucrop_ic_cross);
        mToolbarCropDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, R.drawable.ucrop_ic_done);
        mToolbarWidgetColor = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget));
        mToolbarTitle = args.getString(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitle = mToolbarTitle != null ? mToolbarTitle : getResources().getString(R.string.ucrop_label_edit_photo);

        setupAppBar();
    }

    private String strPostText = "";

    @Override
    public void onSingleImageSelected(Uri uri) {
        startCrop(uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == TAKE_PICTURE) {
                String selectedImagePath = getImagePath();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startCrop(FileProvider.getUriForFile(svContext, getString(R.string.file_provider_authority), new File(selectedImagePath)));
                } else {
                    startCrop(Uri.fromFile(new File(selectedImagePath)));
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            } else {
                final Uri selectedUri = imageUri;
                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    ShowCustomView.showCustomToast(getResources().getString(R.string.toast_cannot_retrieve_selected_image), svContext, ShowCustomView.ToastyError);
                }
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivitySignUp.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            browseImageFromGallery();
                        }
                    });
                }
                break;
            case REQUEST_CAMERA_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    browseImageFromCamera();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    browseImageFromCamera();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Configures and styles both status bar and toolbar.
     */
    private void setupAppBar() {
        setStatusBarColor(mStatusBarColor);
        toolbar = findViewById(R.id.toolbar);

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(mToolbarColor);
        toolbar.setTitleTextColor(mToolbarWidgetColor);
        toolbar.setVisibility(View.VISIBLE);
//        final TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setTextColor(mToolbarWidgetColor);
//        toolbarTitle.setText(mToolbarTitle);

        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(getBaseContext(), mToolbarCancelDrawable);
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate();
            stateButtonDrawable.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(stateButtonDrawable);
        }

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Sets status-bar color for L devices.
     *
     * @param color - status-bar color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);

        // Change crop & loader menu icons color to match the rest of the UI colors

        MenuItem menuItemLoader = menu.findItem(R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Log.i(this.getClass().getName(), String.format("%s - %s", e.getMessage(), getString(R.string.ucrop_mutate_exception_hint)));
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }

        MenuItem menuItemCrop = menu.findItem(R.id.menu_crop);
        Drawable menuItemCropIcon = ContextCompat.getDrawable(this, mToolbarCropDrawable);
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop) {
            if (fragment.isAdded())
                fragment.cropAndSaveImage();
        } else if (item.getItemId() == android.R.id.home) {
            removeFragmentFromScreen();
        }
        return super.onOptionsItemSelected(item);
    }
}
