package in.kamalaenterprize.sncharsuchi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.commonutility.WebServiceWithoutDialog;

public class ActivityLogin extends Activity implements OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private EditText et_phone;
    private AppCompatButton btnLogin;
    private String strPhoneNumber;
    public static final String TAG_RESULT = "result";
    public static final String TAG_MSG = "msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        svContext = this;
        gd = new GlobalData(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        et_phone = (EditText) findViewById(R.id.et_phone);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_continue);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                strPhoneNumber = et_phone.getText().toString().trim();

                if (strPhoneNumber.length() == 0) {
                    et_phone.setError("Please enter mobile number");
                } else if (strPhoneNumber.length() != 10) {
                    et_phone.setError("Please enter correct mobile number");
                } else {
                    if (gd.isConnectingToInternet()) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.OTPPHONENUMBER, strPhoneNumber);
                        if (gd.isConnectingToInternet()) {
                            SendSms("Thanks for signup. otp for your number is " + getRandomNumberString());
//                            randomOtp = "1234";
                            PreferenceConnector.writeString(svContext, PreferenceConnector.RANDOMOTP, randomOtp);

                            String[] keys = {"phone"};
                            String[] value = {strPhoneNumber};

                            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                            for (int j = 0; j < keys.length; j++) {
                                System.out.println(keys[j] + "......." + value[j]);
                                hash.put(keys[j], value[j]);
                            }
                            callWebService(GlobalVariables.GETPHONEAVAILABILITY, hash, "");
                        } else {
                            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
                        }
                    } else {
                        GlobalData.showToast(getResources().getString(R.string.error_internet), svContext);
                    }
                }
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
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
//        https://api.msg91.com/api/v5/otp?authkey=Authentication Key&template_id=Template ID&extra_param={"Param1":"Value1", "Param2":"Value2", "Param3": "Value3"}&mobile=Mobile Number with Country Code&invisible=1&otp=OTP to send and verify. If not sent, OTP will be generated.&userip=IPV4 User IP&email=Email ID
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

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
//        {"result":"success","msg":"Record updated successfully","logindetail":{"id":"27","name":"sri","state":"RJ","pin":"","gender":"Male","phone":"8233388802","fcm_id":"fVMf48wzDEQ:APA91bFUHFHGX_bahULIsaFfULFmr_C0R7R7-teVMtyWgEWqzr7Aya_G3X6DV9QtBMOc3AuC5h8TUWKtkFC67yL_FvALDYf6zIokY8a4JqBLdHcLq7QjepIb_T-L4TCdRrK7FphvcbHA","yoddhaId":"RJN1398565925F","alert":"1","image":"CropImage294202010209.jpg"}}

        if (url.contains(GlobalVariables.GETPHONEAVAILABILITY)) {
            try {
                JSONObject json = new JSONObject(result);

                String str_result = json.getString(TAG_RESULT);
                String str_msg = json.getString(TAG_MSG);

                Intent svIntent = new Intent(svContext, VerificationOtp.class);
                startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();

                if (str_result.equals("success")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.OTPNEXT, "login");
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastySuccess);
                } else {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.OTPNEXT, "signup");
                    ShowCustomView.showCustomToast(str_msg, svContext, ShowCustomView.ToastySuccess);
                }
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }
    }

//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    private PhoneAuthProvider.ForceResendingToken resendToken;
//    private FirebaseAuth mAuth;
//    private void FirebaseOTP(String phoneNumber){
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);
//
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // ...
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // ...
//                }
//
//                // Show a message and update the UI
//                // ...
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                // ...
//            }
//        };
//    }


//    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private void FCMReg() {
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//                    //                    SaveFirebaseRegId();
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//                    String message = intent.getStringExtra("message");
//                    //                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//                    // txtMessage.setText(message);
//                }
//            }
//        };
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}