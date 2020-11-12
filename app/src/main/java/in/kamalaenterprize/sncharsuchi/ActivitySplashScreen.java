package in.kamalaenterprize.sncharsuchi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;

public class ActivitySplashScreen extends Activity {
//    private static final String CUSTOMFONTBOTTOM = "font/magneto.ttf";
//    private static final String CUSTOMFONTTOP = "font/swisscbo.ttf";
    private CountDownTimer ct;
    private Context svContext;
    private GlobalData gd;
    private static int SPLASH_TIME_OUT = 3000;
    private Intent svIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        svContext = this;
        gd = new GlobalData(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        GlobalData.loadLocalImages(R.drawable.splash_logo, (ImageView) findViewById(R.id.imageView2));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.AUTOLOGIN, false)) {
                } else {
                    svIntent = new Intent(svContext, ActivityLogin.class);
                    startActivity(svIntent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

//        Typeface fontTop = Typeface.createFromAsset(getAssets(), CUSTOMFONTTOP);
//        Typeface fontBottom = Typeface.createFromAsset(getAssets(), CUSTOMFONTBOTTOM);

//        ((TextView) findViewById(R.id.text_splashname)).setTypeface(fontTop);
//        ((TextView) findViewById(R.id.splash_bottom)).setTypeface(fontBottom);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ct != null) {
            ct.cancel();
        }
    }
}