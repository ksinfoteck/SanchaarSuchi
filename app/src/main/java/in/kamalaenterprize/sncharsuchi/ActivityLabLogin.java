//package in.ksinfoteck.yoddha;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import EditText;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Html;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import in.ksinfoteck.commonutility.GlobalData;
//import in.ksinfoteck.commonutility.GlobalVariables;
//
//public class ActivityLabLogin extends AppCompatActivity implements View.OnClickListener {
//    private Context context;
//    private GlobalData gd;
//    private TextView aboutText;
//    private LinearLayout layEnterOtp;
//    private TextInputEditText txtMobileNumber, txtPassword;
//    private Button btnOtpSignIn;
//    private String strPhoneNumber, strOtp;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.act_login);
//        context = this;
//        gd = new GlobalData(context);
//        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
//            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
//            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
//            GlobalData.setFont(root, font);
//        }
//
//        aboutText = (TextView) findViewById(R.id.txt_aboutus);
//
//        txtMobileNumber = (TextInputEditText) findViewById(R.id.txt_mobilenumber);
//        txtPassword     = (TextInputEditText) findViewById(R.id.txt_password);
//        btnOtpSignIn    = (Button) findViewById(R.id.txt_otpsinin);
//
//        btnOtpSignIn.setOnClickListener(this);
//
//        ImageView backArrow = (ImageView) findViewById(R.id.btn_back);
//        backArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//
//        String strAbout = getResources().getString(R.string.about_txt_about);
//        aboutText.setText(Html.fromHtml(strAbout));
//    }
//
//    @Override
//    public void onClick(View view) {
//        Intent intent;
//        int response;
//        switch (view.getId()) {
//            case R.id.btn_back:
//                finish();
//                break;
//            case R.id.btn_continue:
////                if (btnOtpSignIn.)
//                strPhoneNumber = txtMobileNumber.getText().toString().trim();
//
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        finish();
//    }
//}
