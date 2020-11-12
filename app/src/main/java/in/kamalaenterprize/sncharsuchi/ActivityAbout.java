package in.kamalaenterprize.sncharsuchi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.commonutility.WebServiceWithoutDialog;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ActivityAbout extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context context;
    private GlobalData gd;
    private TextView aboutText;

    public static final String TAG_ALERT_TEXT="alert_text";
    public static final String TAG_FOOTER="footer";
    public static final String TAG_TITLE="title";
    public static final String TAG_DESC="desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        context = this;
        gd = new GlobalData(context);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        aboutText = (TextView) findViewById(R.id.txt_aboutus);

        ImageView backArrow = (ImageView) findViewById(R.id.btn_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String strAbout = getResources().getString(R.string.about_txt_about);
        aboutText.setText(Html.fromHtml(strAbout));

        if (FragDashboard.slideList.size() == 0 || FragDashboard.slideList == null) {
            if (gd.isConnectingToInternet()) {
                String[] keys = {""};
                String[] value = {""};

                LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
                for (int j = 0; j < keys.length; j++) {
                    System.out.println(keys[j] + "......." + value[j]);
                    hash.put(keys[j], value[j]);
                }
                callWebService(GlobalVariables.LOADSLIDERADS, hash, "");
                callWebServiceWithoutDialog(GlobalVariables.GETABOUTCONTENT, hash, "");
            }else {
                ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), context, ShowCustomView.ToastyError);
            }
        } else {
            LoadSliderAds();
        }
        if (gd.isConnectingToInternet()) {
            String[] keys = {""};
            String[] value = {""};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebServiceWithoutDialog(GlobalVariables.GETABOUTCONTENT, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), context, ShowCustomView.ToastyError);
        }
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(context, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    private void callWebServiceWithoutDialog(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebServiceWithoutDialog webService = new WebServiceWithoutDialog(context, postUrl, hash, this, WebService.POST);
        webService.execute();
    }

    public static final String TAG_BANNERS = "banners";
    public static final String TAG_BANNER_ID = "banner_id";
    public static final String TAG_BANNER_URL = "banner_url";
    public static final String TAG_BANNER_IMAGE = "banner_image";
    public static final String TAG_NAME = "name";
    public static final String TAG_STATUS = "status";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);

        if (url.contains(GlobalVariables.LOADSLIDERADS)) {
            FragDashboard.slideList = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString(TAG_STATUS);
                JSONArray banners = json.getJSONArray(TAG_BANNERS);
                for (int banners_i = 0; banners_i < banners.length(); banners_i++) {
                    JSONObject banners_obj = banners.getJSONObject(banners_i);
                    String str_banner_id = banners_obj.getString(TAG_BANNER_ID);
                    String str_banner_url = banners_obj.getString(TAG_BANNER_URL);
                    String str_banner_image = banners_obj.getString(TAG_BANNER_IMAGE);

                    if (str_banner_image.contains("http://") || str_banner_image.contains("http://")) {
                        str_banner_image = str_banner_image;
                    } else {
                        str_banner_image = "http://" + str_banner_image;
                    }


                    FragDashboard.lstReferUrl.add(str_banner_url);
                    FragDashboard.slideList.add(new Slide(banners_i, str_banner_image, getResources().getDimensionPixelSize(R.dimen.no_corner)));
                }

                LoadSliderAds();
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), context);
                e.printStackTrace();
            }
        }else if (url.contains(GlobalVariables.GETABOUTCONTENT)) {
            try {
                JSONObject json = new JSONObject(result);

                JSONArray alert_text = json.getJSONArray(TAG_ALERT_TEXT);
                for(int alert_text_i = 0; alert_text_i < alert_text.length(); alert_text_i++){
                    JSONObject alert_text_obj=alert_text.getJSONObject(alert_text_i);
                    String str_footer = alert_text_obj.getString(TAG_FOOTER);
                    String str_title = alert_text_obj.getString(TAG_TITLE);
                    String str_desc = alert_text_obj.getString(TAG_DESC);

                    ((TextView)findViewById(R.id.title)).setText(str_title);
                    ((TextView)findViewById(R.id.txt_aboutus)).setText(str_desc);
                }
            }catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), context);
                e.printStackTrace();
            }
        }
    }

    private void LoadSliderAds() {
        Slider slider = findViewById(R.id.slider);
        //handle slider click listener
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceConnector.writeString(context, PreferenceConnector.WEBHEADING, "");
                PreferenceConnector.writeString(context, PreferenceConnector.WEBURL, FragDashboard.lstReferUrl.get(i));

                Intent svIntent = new Intent(context, WebViewActivity.class);
                startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        //add slides to slider
        slider.addSlides(FragDashboard.slideList);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        int response;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
