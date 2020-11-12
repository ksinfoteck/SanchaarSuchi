package in.kamalaenterprize.sncharsuchi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;

public class VerificationPasscode extends AppCompatActivity {
    private EditText et_otpOne, et_otptwo, et_otpthree, et_otpfour;
    private String passCode = "";
    private AppCompatButton btnContinue;
//    private boolean passCodeEnable;
    private TextView txtSkipPasscode;
    private  String tempPasscode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        et_otpOne   = (EditText) findViewById(R.id.et_otpone);
        et_otptwo   = (EditText) findViewById(R.id.et_otptwo);
        et_otpthree = (EditText) findViewById(R.id.et_otpthree);
        et_otpfour  = (EditText) findViewById(R.id.et_otpfour);

        txtSkipPasscode = (TextView)findViewById(R.id.skipPascode);
        btnContinue = (AppCompatButton) findViewById(R.id.btn_continue);
        txtSkipPasscode.setVisibility(View.INVISIBLE);

        passCode = PreferenceConnector.readString(this, PreferenceConnector.PASSCODE, "1234");
//        passCodeEnable = PreferenceConnector.readBoolean(this, PreferenceConnector.PASSCODESET, false);

        if (PreferenceConnector.readString(this, PreferenceConnector.PASSCODE, "").equals("")){
            btnContinue.setText("Next");
        }else {

        }


        tempPasscode = "";
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputOtp = et_otpOne.getText().toString().trim() + et_otptwo.getText().toString().trim() + et_otpthree.getText().toString().trim() +
                        et_otpfour.getText().toString().trim();

                if ((btnContinue.getText().toString().trim()).equalsIgnoreCase("Next")) {
                    if (inputOtp.length() == 4) {
                        tempPasscode = inputOtp;

                        et_otpOne.setText("");
                        et_otptwo.setText("");
                        et_otpthree.setText("");
                        et_otpfour.setText("");

                        et_otpOne.requestFocus();

                        btnContinue.setText("Set Passcode");
                    }else {
                        ShowCustomView.showCustomToast("Enter Passcode", VerificationPasscode.this, ShowCustomView.ToastyError);
                    }
                }else if ((btnContinue.getText().toString().trim()).equalsIgnoreCase("Set Passcode")) {
                    if (inputOtp.length() == 4) {
                        if (tempPasscode.equalsIgnoreCase(inputOtp)) {
                            PreferenceConnector.writeString(VerificationPasscode.this, PreferenceConnector.PASSCODE, inputOtp);
//                            PreferenceConnector.writeBoolean(VerificationPasscode.this, PreferenceConnector.PASSCODESET, true);
                            btnContinue.setText("Verify");

                            Intent svIntent = new Intent(VerificationPasscode.this, ActivityMain.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                        }else {
                            ShowCustomView.showCustomToast("Passcode not matching", VerificationPasscode.this, ShowCustomView.ToastyError);
                        }
                    }else {
                        ShowCustomView.showCustomToast("Enter Passcode", VerificationPasscode.this, ShowCustomView.ToastyError);
                    }
                }else if ((btnContinue.getText().toString().trim()).equalsIgnoreCase("Verify")) {
                    if (passCode.equals(inputOtp)) {
                        Intent svIntent = new Intent(VerificationPasscode.this, ActivityMain.class);
                        startActivity(svIntent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    }else {
                        ShowCustomView.showCustomToast("Enter Correct Passcode", VerificationPasscode.this, ShowCustomView.ToastyError);
                    }
                }
            }
        });

        txtSkipPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        et_otpOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

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
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
