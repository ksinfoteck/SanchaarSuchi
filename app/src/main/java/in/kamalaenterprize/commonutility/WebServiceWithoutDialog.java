package in.kamalaenterprize.commonutility;

import android.content.Context;
import android.os.AsyncTask;

import java.util.LinkedHashMap;

public class WebServiceWithoutDialog extends AsyncTask<String, Void, String> {
    public static final int POST = 1;
    public static final int GET = 2;
    public static final int POSTWITHIMAGE = 3;
    public static final int MSG = 4;

    private String[] path;
    private Context context;
    private String url, message;
    private int post;
    private LinkedHashMap<String, String> keyValue;
    private WebServiceListener listener;

    // For Get and Post
    public WebServiceWithoutDialog(Context context, String url, LinkedHashMap<String, String> keyValue,
                                   WebServiceListener listener, int post) {
        this.context = context;
        this.url = url;
        this.keyValue = keyValue;
        this.listener = listener;
        this.post = post;
        System.out.println(url + ".....................url,,,,,,,,..............");
    }

    public WebServiceWithoutDialog(Context context, String url, LinkedHashMap<String, String> keyValue,
                                   WebServiceListener listener, int post, String path[]) {
        this.context = context;
        this.url = url;
        this.keyValue = keyValue;
        this.listener = listener;
        this.post = post;
        this.path = path;
        System.out.println(url + ".....................url,,,,,,,,..............");
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        if (post == POST) {
            result = PostData.postData(context, GlobalVariables.PRE_URL + url, keyValue);
        } else if (post == GET) {
            result = PostData.getData(context, GlobalVariables.PRE_URL + url, keyValue);
        } else if (post == MSG) {
            result = PostData.getData(context, GlobalVariables.PRE_URL_MSG + url, keyValue);
        } else if (post == POSTWITHIMAGE) {
            result = PostData.postDataWithImage(context, GlobalVariables.PRE_URL + url, keyValue, path);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onWebServiceActionComplete(result, url);
    }
}
