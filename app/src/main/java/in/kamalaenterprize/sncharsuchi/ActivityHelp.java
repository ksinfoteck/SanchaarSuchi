package in.kamalaenterprize.sncharsuchi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.adapter.AdapterListHelp;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.LineItemDecoration;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.model.HelpModel;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ActivityHelp extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context context;
    private GlobalData gd;
    private List<HelpModel> lstData = new ArrayList<>();
    private String[] strQues = {"What is the Sanchar Suchi ID?",
            "How can I record Sanchar Suchi ID of a person?",
            "Whose Sanchar Suchi ID should I register in my app?",
            "Does this app support recording of contacts by bluetooth?"};
    private String[] strAns = {"Sanchar Suchi ID is your unique identification number",
            "By entering the ID or scanning the QR quode in your app, you can record your communication with a person came in your close contact",
            "A person who came in close contact with you or is working with you in an office, shop etc or either of you are not wearing a mask/taking poper precautions in the pandemic",
            "No, this app is meant for recording of the contacts ( Sanchar Suchi IDs) manually, either by entering the ID or scanning the QR quode of the person you think came very close"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);
        context = this;
        gd = new GlobalData(context);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        ImageView backArrow = (ImageView) findViewById(R.id.btn_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
            } else {
                ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), context, ShowCustomView.ToastyError);
            }
        } else {
            LoadSliderAds();
        }

        initComponent();
    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(context, postUrl, hash, this, WebService.POST, showText);
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

    private RecyclerView recyclerView;
    private int animation_type = ItemAnimation.FADE_IN;
    private void initComponent() {
        lstData = new ArrayList<>();
        for (int j = 0; j < strQues.length; j++) {
            lstData.add(new HelpModel(strQues[j], strAns[j]));
        }

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        AdapterListHelp mAdapter = new AdapterListHelp(this, lstData, animation_type);
        recyclerView.setAdapter(mAdapter);
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
