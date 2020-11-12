package in.kamalaenterprize.commonutility;

import android.os.Environment;
import android.widget.TextView;

import java.io.File;

public class GlobalVariables {
    public static boolean isTesting = true;
	public static final String defaultAppPath 				=  Environment.getExternalStorageDirectory().getAbsolutePath() +
			File.separator + "Sanchar Suchi/";

    public static String google_api_key 					= "AIzaSyDuLfus-RMjhIkkGheOr3jF9P7USDkX-nI";
    public static String spreadsheet_id 					= "1p9s_YvSFVK9QIK4vkWlP9e_FgJBr0CCVuRXxvgu_AJU";

	public static final String PRE_URL_MSG 					= "http://api.msg91.com/api/";
	public static final String MSG_URL 						= "sendhttp.php";

	// MSG91 Gateway Detail
	public static final String SENDERID						= "SNCHAR";
	public static final String ROUTE 						= "4";
	public static final String AUTHKEY	 					= "258377A7uVRSPmst35c4b691c";
	public static final String COUNTRYCODE 					= "91";
	public static final String OTPTEMPLATE	 					= "5f210ec8d6fc0530013056e9";

	public static final String  CUSTOMFONTNAME 				= "sanchaar_font.ttf";

	public static final String  TEST_URL 					= "";
	public static final String  PRE_URL	 					= "http://sanchaarsuchi.ksprojects.online/myapi/";
	public static final String  PRE_URL_IMG					= "http://sanchaarsuchi.ksprojects.online/myapi/uploads/";
	public static final String  GETPHONEAVAILABILITY 		= "check_phone.php";
	public static final String  LOGIN						= "update_fcm.php";
	public static final String  SIGNUP						= "register.php";
	public static final String  GETTRAVELTYPES				= "getTravelType.txt";
	public static final String  ADDTRAVELHISTORY 			= "addtravelhistory.php";
	public static final String  GETSTATELIST	 			= "indiastates.json";
	public static final String  GETTRAVELHISTORY			= "getTravelhistory.php";
	public static final String  ADDTPOSITIVEREPORT 			= "addpostivereport.php";
	public static final String  GETNOTIFICATION 			= "getnotification.php";
	public static final String  GETUNVERIFIEDALERT 			= "getunverifiedalert.php";
	public static final String  LOADSLIDERADS 				= "get_banner.php";
	public static final String  GETALERTCONTENT 			= "alert.json";
	public static final String  GETABOUTCONTENT 			= "about.json";

	public static final String  ACCEPTREJECT 				= "acceptreject.php";
	public static final String  GETLABDETAIL 				= "http://sanchaarsuchi.ksprojects.online/myapi/resources.json";

	public static final String  CHECKZONESSTATUS			= "https://api.covid19india.org/zones.json";
	public static final String  STATEDISTRICWISE			= "https://api.covid19india.org/v2/state_district_wise.json";

//	https://api.covid19india.org/state_district_wise.json
//	public static final String  GETQRCODE 					= "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=";

	public static final String Facebook 					= "http://www.fb.com/sanchaarsuchi";
	public static final String Instagram 					= "sanchaarsuchi";
	public static final String ContactNumber 				= "+918233388802";
	public static final String CompanyWebsite 				= "http://www.sanchaarsuchi.com";
	public static final String CompanyEmail 				= "sanchaarsuchi@ksinfoteck.com";

	public static final int CUSTOMIZEERROR 					= 0;
	public static final int ERRORIDNOTGETTINGDATAFROMSERVER = 1;
	public static final int ERRORIDINTERNETNOTAVAILABLE 	= 2;

	public static final String CURRENCYSYMBOL 				= "₹ ";
	public static final boolean ISTESTING 					= true;
	public static String 		SELECTDATE              	= "";
	public static TextView TextViewDate 					= null;
}