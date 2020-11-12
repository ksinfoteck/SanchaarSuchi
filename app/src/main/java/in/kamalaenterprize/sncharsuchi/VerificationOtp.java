package in.kamalaenterprize.sncharsuchi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.commonutility.WebServiceWithoutDialog;

public class VerificationOtp extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private EditText et_phone, et_otpOne, et_otptwo, et_otpthree, et_otpfour;
    private AppCompatButton btnContinue, btnResendOtp;
    private String strPhoneNumber;

    private TextView tv_coundown;
    private CountDownTimer countDownTimer;

    public static final String TAG_RESULT="result";
    public static final String TAG_MSG="msg";
    public static final String TAG_LOGINDETAIL="logindetail";
    public static final String TAG_YODDHAID="yoddhaId";
    public static final String TAG_PIN="pin";
    public static final String TAG_GENDER="gender";
    public static final String TAG_PHONE="phone";
    public static final String TAG_NAME="name";
    public static final String TAG_ID="id";
    public static final String TAG_STATE="state";
    public static final String TAG_FCM_ID="fcm_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        svContext = this;
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        tv_coundown = (TextView) findViewById(R.id.tv_coundown);
        countDownTimer();

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_otpOne = (EditText) findViewById(R.id.et_otpone);
        et_otptwo = (EditText) findViewById(R.id.et_otptwo);
        et_otpthree = (EditText) findViewById(R.id.et_otpthree);
        et_otpfour = (EditText) findViewById(R.id.et_otpfour);

        et_otpOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() == 1) {
                        et_otptwo.requestFocus();
                    } else {

                    }
                }
            }
        });

        et_otptwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() == 1) {
                        et_otpthree.requestFocus();
                    } else {
                        et_otpOne.requestFocus();
                    }
                }
            }
        });

        et_otpthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable != null) {
                    if (editable.length() == 1) {
                        et_otpfour.requestFocus();
                    } else {
                        et_otptwo.requestFocus();
                    }
                }

            }
        });

        et_otpfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable != null) {
                    if (editable.length() == 1) {

                    } else {
                        et_otpthree.requestFocus();
                    }
                }
            }
        });

        btnContinue = (AppCompatButton) findViewById(R.id.btn_continue);
        btnResendOtp = (AppCompatButton) findViewById(R.id.resendotp);

        btnContinue.setOnClickListener(this);
        btnResendOtp.setOnClickListener(this);


        et_phone.setText(PreferenceConnector.readString(svContext, PreferenceConnector.OTPPHONENUMBER, strPhoneNumber));
    }

    private void countDownTimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 2, 1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                tv_coundown.setText(text);
            }

            @Override
            public void onFinish() {
                tv_coundown.setText("00:00");
            }
        };
        countDownTimer.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                String inputOtp = et_otpOne.getText().toString().trim() + et_otptwo.getText().toString().trim() + et_otpthree.getText().toString().trim() +
                        et_otpfour.getText().toString().trim();


                String randomOtp = PreferenceConnector.readString(svContext, PreferenceConnector.RANDOMOTP, "");
                System.out.println(randomOtp + "......randomotp.");
                System.out.println(inputOtp + "......inputotp.");
                if (randomOtp.equals(inputOtp)) {
                    String nextScreen = PreferenceConnector.readString(svContext, PreferenceConnector.OTPNEXT, "signup");
                    System.out.println(nextScreen + "......nextScreen.");
                    if (nextScreen.equalsIgnoreCase("signup")) {
                        Intent svIntent = new Intent(svContext, ActivitySignUp.class);
                        startActivity(svIntent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    } else {

                        if (gd.isConnectingToInternet()) {
                            String[] keys = {"phone", "fcm_id"};
                            String[] value = {PreferenceConnector.readString(svContext, PreferenceConnector.OTPPHONENUMBER, ""), ""};

                            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                            for (int j = 0; j < keys.length; j++) {
                                System.out.println(keys[j] + "......." + value[j]);
                                hash.put(keys[j], value[j]);
                            }

                            callWebService(GlobalVariables.LOGIN, hash, "");
                        } else {
                            ShowCustomView.showInternetAlert(svContext);
                        }
                    }
                } else {
                    ShowCustomView.showCustomToast("Otp not matching", svContext, ShowCustomView.ToastyError);
                }

                break;
            case R.id.resendotp:
                SendSms("Thanks for signup. otp for your number is " + getRandomNumberString());
                break;
            default:
                break;
        }
    }

    String randomOtp = "";

    public String getRandomNumberString() {
        randomOtp = "";
        randomOtp = "" + ((int) (Math.random() * 9000) + 1000);

//        Random rnd = new Random();
//        int number = rnd.nextInt(9999);
//        randomOtp = String.format("%04d", number);
        PreferenceConnector.writeString(svContext, PreferenceConnector.RANDOMOTP, randomOtp);
        return randomOtp;
    }

    private void SendSms(String StrMessage) {
//        http://api.msg91.com/api/sendhttp.php?sender=CITYHE&route=4&mobiles=8233388802&authkey=216317AbrcSrdty5b019417&country=91&message=Hello! This is a test message
        if (gd.isConnectingToInternet()) {
            String[] keys = {"sender", "route", "mobiles", "authkey", "country", "message"};
            String[] value = {GlobalVariables.SENDERID, GlobalVariables.ROUTE, strPhoneNumber, GlobalVariables.AUTHKEY, GlobalVariables.COUNTRYCODE,
                    StrMessage};
            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }

            WebServiceWithoutDialog webService = new WebServiceWithoutDialog(svContext, GlobalVariables.MSG_URL, hash, this, WebService.MSG);
            webService.execute();
        } else {
            ShowCustomView.showInternetAlert(svContext);
        }
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.LOGIN)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString(TAG_RESULT);
                if (str_result.equals("success")) {
                    if (json.has(TAG_LOGINDETAIL)) {
                        JSONObject logindetail_obj = json.getJSONObject(TAG_LOGINDETAIL);
                        for (int logindetail_i = 0; logindetail_i < logindetail_obj.length(); logindetail_i++) {
                            String str_yoddhaId = logindetail_obj.getString(TAG_YODDHAID);
                            String str_pin = logindetail_obj.getString(TAG_PIN);
                            String str_gender = logindetail_obj.getString(TAG_GENDER);
                            String str_phone = logindetail_obj.getString(TAG_PHONE);
                            String str_name = logindetail_obj.getString(TAG_NAME);
                            String str_id = logindetail_obj.getString(TAG_ID);
                            String str_state = logindetail_obj.getString(TAG_STATE);

                            String str_userid = "";
                            if (logindetail_obj.has(TAG_ID)) {
                                str_userid = logindetail_obj.getString(TAG_ID);
                            }

                            String str_alert = "";
                            str_alert = logindetail_obj.getString("alert");

                            String str_Profilepic = "";
                            if (logindetail_obj.has("img_profile")) {
                                str_Profilepic = logindetail_obj.getString("img_profile");
                            }

                            String adminfcm_id = logindetail_obj.getString("adminfcm_id");

                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERID, str_userid);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPHONE, str_phone);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.YODDHAID, str_yoddhaId);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINNAME, str_name);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINID, str_id);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINSTATE, str_state);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINGENDER, str_gender);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.ISALERT, str_alert);
                            PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINPROFILEPIC, str_Profilepic);

                            PreferenceConnector.writeString(svContext, PreferenceConnector.ADMINLOGINFCMID, adminfcm_id);

                            PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);

                            Intent svIntent;
//                            if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.PASSCODESET, true)) {
//                                svIntent = new Intent(svContext, VerificationPasscode.class);
//                                startActivity(svIntent);
//                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                                finish();
//                            } else {
                                svIntent = new Intent(svContext, ActivityMain.class);
                                startActivity(svIntent);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                finish();
//                            }
                        }
                    }
                }else {
                    String str_msg = json.getString("msg");
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastyError);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent svIntent = new Intent(svContext, ActivityLogin.class);
        startActivity(svIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }
}
