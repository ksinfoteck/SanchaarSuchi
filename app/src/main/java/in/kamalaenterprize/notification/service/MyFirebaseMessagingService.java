package in.kamalaenterprize.notification.service;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.notification.app.Config;
import in.kamalaenterprize.notification.util.NotificationUtils;
import in.kamalaenterprize.sncharsuchi.ActivityMain;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
        Intent resultIntent = new Intent(getApplicationContext(), ActivityMain.class);
        resultIntent.putExtra("type", "notification");
        resultIntent.putExtra("message", message);

        showNotificationMessage(getApplicationContext(), "Test Notification", message, "07:48", resultIntent);
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        String result = json.toString();

        try {
            String str_title = json.getString("mesgTitle");
            String str_message = json.getString("alert");

            generateNotification(MyFirebaseMessagingService.this, str_title, str_message);
            updateMyActivity(MyFirebaseMessagingService.this);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public Uri playNotificationSound(Context mContext) {
        Uri alarmSound = null;
        try {
            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return alarmSound;
    }

    private void generateNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.putExtra("type", "notification");

        showNotificationMessage(getApplicationContext(), title, message, GlobalData.getcurrentTime(), intent);
    }

    private void updateMyActivity(Context context) {
        Intent intent = new Intent("com.kstaxi.roadmasterprovider");
        intent.putExtra("type", "notification");

        context.sendBroadcast(intent);
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        PreferenceConnector.writeBoolean(context, PreferenceConnector.ISNOTIFICATION, true);
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}
