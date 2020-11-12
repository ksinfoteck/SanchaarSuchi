package in.kamalaenterprize.sncharsuchi;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;

public class ActivityAlert extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private Typeface font;
    private GlobalData gd;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_alert);
        svContext = this;
        gd = new GlobalData(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }
        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISALERTSHOWING, true);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        if (gd.isConnectingToInternet()) {
            String[] keys = {""};
            String[] value = {""};

            LinkedHashMap<String, String> hash = new LinkedHashMap<String, String>();
            for (int j = 0; j < keys.length; j++) {
                System.out.println(keys[j] + "......." + value[j]);
                hash.put(keys[j], value[j]);
            }
            callWebService(GlobalVariables.GETALERTCONTENT, hash, "");
        } else {
            ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
        }

    }

    @Override
    public void onClick(View view) {
        int response;
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISALERTSHOWING, false);
        super.finish();
    }

    @Override
    public void finish() {
        PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISALERTSHOWING, false);
        super.finish();
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
    }

    public static final String TAG_ALERT_TEXT="alert_text";
    public static final String TAG_FOOTER="footer";
    public static final String TAG_TITLE="title";
    public static final String TAG_DESC="desc";
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.GETALERTCONTENT)) {
            try {
                JSONObject json = new JSONObject(result);

                JSONArray alert_text = json.getJSONArray(TAG_ALERT_TEXT);
                for(int alert_text_i = 0; alert_text_i < alert_text.length(); alert_text_i++){
                    JSONObject alert_text_obj=alert_text.getJSONObject(alert_text_i);
                    String str_footer = alert_text_obj.getString(TAG_FOOTER);
                    String str_title = alert_text_obj.getString(TAG_TITLE);
                    String str_desc = alert_text_obj.getString(TAG_DESC);

                    ((TextView)findViewById(R.id.title)).setText(str_title);
                    ((TextView)findViewById(R.id.desc)).setText(str_desc);
                    ((TextView)findViewById(R.id.footer)).setText(str_footer);
                }
            }catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }else {
            try {
                JSONObject json = new JSONObject(result);

//                populateState(db.getAllCategory(db.tablenames[0]));

            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
                e.printStackTrace();
            }
        }
    }
}
