package in.kamalaenterprize.sncharsuchi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asksira.webviewsuite.WebViewSuite;

import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.ShowCustomView;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{
    private WebViewSuite webViewSuite;
    private ImageView imgBack;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webViewSuite    = findViewById(R.id.webViewSuite);
        imgBack         = (ImageView) findViewById(R.id.btn_back);
        txtTitle        = (TextView) findViewById(R.id.heading);
        txtTitle.setText(PreferenceConnector.readString(this, PreferenceConnector.WEBHEADING, ""));

        imgBack.setOnClickListener(this);

        ProgressBar pDialog = new ProgressBar(this);
//        pDialog.set("Loading...");
//        pDialog.setCanceledOnTouchOutside(false);
//        pDialog.setCancelable(false);

        webViewSuite.startLoading(PreferenceConnector.readString(this, PreferenceConnector.WEBURL, ""));
        webViewSuite.setCustomProgressBar(pDialog);


        webViewSuite.setOpenPDFCallback(new WebViewSuite.WebViewOpenPDFCallback() {
            @Override
            public void onOpenPDF() {
                finish();
            }
        });


        webViewSuite.customizeClient(new WebViewSuite.WebViewSuiteCallback() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Do your own stuffs. These will be executed after default onPageStarted().
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Do your own stuffs. These will be executed after default onPageFinished().
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Override those URLs you need and return true.
                //Return false if you don't need to override that URL.
                if (url.contains("wa.me")) {
                    openWhatsApp();
                    onBackPressed();
                }else {
                    view.loadUrl(url);
                }
                return false;
            }
        });
    }

    private void openWhatsApp() {
        String smsNumber = "919116669453"; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I want to know about your services.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        if (sendIntent.resolveActivity(getPackageManager()) == null) {
            ShowCustomView.showCustomToast("Whatsapp have not been installed.", WebViewActivity.this,
                    ShowCustomView.ToastyInfo);
            return;
        }
        startActivity(sendIntent);
    }

    @Override
    public void onClick(View view) {
        int response;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void onRefreshWebView() {
        webViewSuite.refresh();
    }

    private void LoadStaticData() {
//        webViewSuite.startLoadData(data, mimeType, encoding);
    }

    @Override
    public void onBackPressed() {
        if (!webViewSuite.goBackIfPossible()) super.onBackPressed();
    }
}
