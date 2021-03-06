package in.kamalaenterprize.commonutility;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.util.LinkedHashMap;

import in.kamalaenterprize.sncharsuchi.R;

public class WebService extends AsyncTask<String, Void, String> {
    public static final int POST            = 1;
    public static final int GET             = 2;
    public static final int POSTWITHIMAGE   = 3;
    public static final int MSG             = 4;

    private String[] path;
    private Context context;
    private String url, message, showText;
    private int post;
    private LinkedHashMap<String, String> keyValue;
    private WebServiceListener listener;
    private CustomeProgressDialog customeProgressDialog;

    // For Get and Post
    public WebService(Context context, String url, LinkedHashMap<String, String> keyValue,
                      WebServiceListener listener, int post) {
        this.context = context;
        this.url = url;
        this.keyValue = keyValue;
        this.listener = listener;
        this.post = post;
        System.out.println(url + ".....................url,,,,,,,,..............");
    }

    public WebService(Context context, String url, LinkedHashMap<String, String> keyValue,
                      WebServiceListener listener, int post, String showText) {
        this.context = context;
        this.url = url;
        this.keyValue = keyValue;
        this.listener = listener;
        this.post = post;
        this.showText = showText;
        System.out.println(url + ".....................url,,,,,,,,..............");
    }

    public WebService(Context context, String message, String url, LinkedHashMap<String, String> keyValue,
                      WebServiceListener listener, int post, String[] path, String showText) {
        this.path = new String[path.length];
        this.context = context;
        this.url = url;
        this.keyValue = keyValue;
        this.listener = listener;
        this.message = message;
        this.post = post;
        this.showText = showText;
        this.path = path;
        System.out.println(url + ".....................url,,,,,,,,..............");
    }

    @Override
    protected void onPreExecute() {
        customeProgressDialog	=	new CustomeProgressDialog(context, R.layout.custom_progess_dialog);
        customeProgressDialog.setCancelable(false);
        customeProgressDialog.setTitle(showText);
        customeProgressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        if (url.equalsIgnoreCase(GlobalVariables.TEST_URL)) {
            return  result;
        }else {
            if (post == POST) {
                result = PostData.postData(context, GlobalVariables.PRE_URL + url, keyValue);
            } else if (post == GET) {
                result = PostData.getData(context, GlobalVariables.PRE_URL + url, keyValue);
            } else if (post == MSG) {
                result = PostData.getData(context, GlobalVariables.PRE_URL_MSG + url, keyValue);
            } else if (post == POSTWITHIMAGE) {
                result = PostData.postDataWithImage(context, GlobalVariables.PRE_URL + url, keyValue, path);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
        if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
            customeProgressDialog.dismiss();
        }
//        if (GlobalVariables.isTesting) {
//            try {
//                GlobalData.writeStringAsFile(context, result, url);
//            } catch (IOException e) {}
//        }
        listener.onWebServiceActionComplete(result, url);
    }
}
