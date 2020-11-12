package in.kamalaenterprize.sncharsuchi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import in.kamalaenterprize.adapter.AdapterListExpand;
import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.LineItemDecoration;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.commonutility.WebService;
import in.kamalaenterprize.commonutility.WebServiceListener;
import in.kamalaenterprize.database.DatabaseHandler;
import in.kamalaenterprize.model.TravelHistoryModel;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ActivityShowTravelHistoryOffline extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
    private View parent_view;
    private RecyclerView recyclerView;
    private AdapterListExpand mAdapter;
    private ImageView imgBack;
    List<TravelHistoryModel> lstData = new ArrayList<>();
    private int animation_type = ItemAnimation.FADE_IN;
    private DatabaseHandler db;
    private TextView txtNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        parent_view = findViewById(android.R.id.content);
        svContext = this;
        db = new DatabaseHandler(this);
        gd = new GlobalData(svContext);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtNodata = (TextView) findViewById(R.id.txt_nodata);

        findViewById(R.id.et_date).setVisibility(View.GONE);
        findViewById(R.id.et_showall).setVisibility(View.GONE);

        imgBack.setOnClickListener(this);

        List<String[]> lstOfflineData = db.getData(db.tablenames[0]);
        for (int j = 0; j < lstOfflineData.size(); j++) {
            lstData.add(new TravelHistoryModel(lstOfflineData.get(j)));
        }


        initComponent();

        if (FragDashboard.slideList.size() == 0 || FragDashboard.slideList == null){
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
                ShowCustomView.showCustomToast(getResources().getString(R.string.error_internet), svContext, ShowCustomView.ToastyError);
            }
        }else {
            LoadSliderAds();
        }
    }

    private void initComponent() {
        if (lstData.size() == 0) {
            txtNodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            txtNodata.setText("No offline history available.\nAll history is updated");
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
            recyclerView.setHasFixedSize(true);

            mAdapter = new AdapterListExpand(this, lstData, animation_type);
            recyclerView.setAdapter(mAdapter);
        }

    }

    public static final String TAG_BANNERS="banners";
    public static final String TAG_BANNER_ID="banner_id";
    public static final String TAG_BANNER_URL="banner_url";
    public static final String TAG_BANNER_IMAGE="banner_image";
    public static final String TAG_STATUS="status";
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(GlobalVariables.LOADSLIDERADS)) {
            FragDashboard.slideList = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(result);
                String str_status = json.getString(TAG_STATUS);
                JSONArray banners = json.getJSONArray(TAG_BANNERS);
                for(int banners_i = 0; banners_i < banners.length(); banners_i++){
                    JSONObject banners_obj=banners.getJSONObject(banners_i);
                    String str_banner_id = banners_obj.getString(TAG_BANNER_ID);
                    String str_banner_url = banners_obj.getString(TAG_BANNER_URL);
                    String str_banner_image = banners_obj.getString(TAG_BANNER_IMAGE);

                    if(str_banner_image.contains("http://") || str_banner_image.contains("http://")){
                        str_banner_image = str_banner_image;
                    }else {
                        str_banner_image = "http://"+str_banner_image;
                    }

                    FragDashboard.lstReferUrl.add(str_banner_url);
                    FragDashboard.slideList.add(new Slide(banners_i, str_banner_image, getResources().getDimensionPixelSize(R.dimen.no_corner)));
                }

                LoadSliderAds();
            } catch (JSONException e) {
                GlobalData.showToast(getResources().getString(R.string.errorgettingdatafromserver), svContext);
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
                PreferenceConnector.writeString(svContext, PreferenceConnector.WEBHEADING, "");
                PreferenceConnector.writeString(svContext, PreferenceConnector.WEBURL, FragDashboard.lstReferUrl.get(i));

                Intent svIntent = new Intent(svContext, WebViewActivity.class);
                startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        //add slides to slider
        slider.addSlides(FragDashboard.slideList);

    }

    private void callWebService(String postUrl, LinkedHashMap<String, String> hash, String showText) {
        WebService webService = new WebService(svContext, postUrl, hash, this, WebService.POST, showText);
        webService.execute();
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
        finish();
    }
}
