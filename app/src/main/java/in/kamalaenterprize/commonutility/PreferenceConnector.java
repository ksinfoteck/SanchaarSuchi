package in.kamalaenterprize.commonutility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector{
	public static final String 	PREF_NAME 				= "app_prefrences";
	public static final int 	MODE 					= Context.MODE_PRIVATE;

	public static final String  AUTOLOGIN 				= "autologin";


	public static final String WEBHEADING 				= "isaboutedit";
	public static final String WEBURL 					= "isrequest";
    public static final String OTPPHONENUMBER 			= "otpphonenumber";
	public static final String RANDOMOTP 				= "randomotp";
    public static final String PASSCODE 				= "passcode";

	public static final String YODDHAID 				= "yoddhaid";
    public static final String LOGINPHONE				= "loginphone";
	public static final String LOGINNAME				= "loginname";
	public static final String LOGINID 					= "loginid";
	public static final String LOGINSTATE				= "loginstate";
	public static final String LOGINGENDER				= "logingender";
//	public static final String PASSCODESET 				= "passcodeset";
	public static final String ISFIRSTPASSCODE			= "isfirstpasscode";
    public static final String LOGINUSERID 				= "user_id";
    public static final String FCMID 					= "fcm_id";
    public static final String OTPNEXT 					= "otpnext";
    public static final String ISALERT 					= "isalert";
    public static final String ISALERTSHOWING 			= "isalertshowing";
    public static final String ISNOTIFICATION 			= "isnotification";
    public static final String LOGINPROFILEPIC 			= "loginprofilepic";
    public static final String WEBSITE 					= "website";
	public static final String FACEBOOK 				= "facebook";
	public static final String PLAYSTORE				= "playstore";
    public static final String LANGUAGE 				= "language";
    public static final String ADMINLOGINFCMID 			= "adminfcmid";

    public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void cleanPrefrences(Context context){
		getPreferences(context).edit().clear().commit();
	}
}
