package in.kamalaenterprize.commonutility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import in.kamalaenterprize.sncharsuchi.R;

public class ShowCustomView {
    private static Context _context;
    private int i = -1;

    public static final int ToastyError = 0;
    public static final int ToastySuccess = 1;
    public static final int ToastyInfo = 2;
    public static final int ToastyWarning = 3;
    public static final int ToastyNormal = 4;
    public static final int ToastyNormalWithIcon = 5;


    public ShowCustomView(Context context) {
        this._context = context;
    }

    public static void showCustomToast(final String toastString, final Context con, final int ToastType) {
//              Toasty.Config.getInstance()
//                .setErrorColor(Color.RED) // optional
//                .setInfoColor(Color.YELLOW) // optional
//                .setSuccessColor(Color.GREEN) // optional
//                .setWarningColor(Color.GREEN) // optional
//                .setTextColor(Color.WHITE) // optional
//                .tintIcon( boolean tintIcon) // optional (apply textColor also to the icon)
//                .setToastTypeface(Typeface.createFromAsset(_context.getAssets(), "PCap Terminal.otf")) // optional
//                .setTextSize( int sizeInSp) // optional
//                .apply(); // required

        ((Activity) con).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (ToastType) {
                    case ToastyError:
                        Toasty.error(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastySuccess:
                        Toasty.success(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyInfo:
                        Toasty.info(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyWarning:
                        Toasty.warning(con, toastString, Toast.LENGTH_LONG, true).show();
                        break;
                    case ToastyNormal:
                        Toasty.normal(con, toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyNormalWithIcon:
                        Drawable icon = con.getResources().getDrawable(R.drawable.ic_launcher_background);
                        Toasty.normal(con, toastString, icon).show();
                        break;
//                  case ToastySuccess:
//                        Toasty.Config.getInstance()
//                                .setTextColor(Color.GREEN)
//                                .setToastTypeface(Typeface.createFromAsset(getAssets(), "PCap Terminal.otf"))
//                                .apply();
//                        Toasty.custom(_context, "sudo kill -9 everyone", getResources().getDrawable(R.drawable.laptop512),
//                                Color.BLACK, Toast.LENGTH_SHORT, true, true).show();
//                        Toasty.Config.reset(); // Use this if you want to use the configuration above only once
//                        break;
                    default:
                        break;
                }
                System.out.println(toastString + "..........toastString__print......");
            }
        });
    }

    public static void showCustomAlert(final String alertTitle, final String alertContent, final Context con,
                                       final int AlertType, Drawable drawable) {
        switch (AlertType) {
//            case BASICALERTWITHOUTCONTENT:
//                // default title "Here's a message!"
//                SweetAlertDialog sd1 = new SweetAlertDialog(con);
//                sd1.setTitleText(alertTitle);
//                sd1.setCancelable(true);
//                sd1.setCanceledOnTouchOutside(true);
//                sd1.show();
//                break;
//            case BASICALERT:
//                SweetAlertDialog sd = new SweetAlertDialog(con);
//                sd.setTitleText(alertTitle);
//                sd.setContentText(alertContent);
//                sd.setCancelable(true);
//                sd.setCanceledOnTouchOutside(true);
//                sd.show();
//                break;
//            case ERRORALERT:
//                new SweetAlertDialog(con, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText(alertTitle)
//                        .setContentText(alertContent)
//                        .show();
//                break;
//            case SUCCESSALERT:
//                new SweetAlertDialog(con, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText(alertTitle)
//                        .setContentText(alertContent)
//                        .show();
//                break;



//            case R.id.warning_confirm_test:
//                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("Won't be able to recover this file!")
//                        .setConfirmText("Yes,delete it!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                // reuse previous dialog instance
//                                sDialog.setTitleText("Deleted!")
//                                        .setContentText("Your imaginary file has been deleted!")
//                                        .setConfirmText("OK")
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                            }
//                        })
//                        .show();
//                break;

//            case R.id.warning_cancel_test:
//                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("Won't be able to recover this file!")
//                        .setCancelText("No,cancel plx!")
//                        .setConfirmText("Yes,delete it!")
//                        .showCancelButton(true)
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                // reuse previous dialog instance, keep widget user state, reset them if you need
//                                sDialog.setTitleText("Cancelled!")
//                                        .setContentText("Your imaginary file is safe :)")
//                                        .setConfirmText("OK")
//                                        .showCancelButton(false)
//                                        .setCancelClickListener(null)
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
//
//                                // or you can new a SweetAlertDialog to show
//                               /* sDialog.dismiss();
//                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Cancelled!")
//                                        .setContentText("Your imaginary file is safe :)")
//                                        .setConfirmText("OK")
//                                        .show();*/
//                            }
//                        })
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.setTitleText("Deleted!")
//                                        .setContentText("Your imaginary file has been deleted!")
//                                        .setConfirmText("OK")
//                                        .showCancelButton(false)
//                                        .setCancelClickListener(null)
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                            }
//                        })
//                        .show();
//                break;
        }
    }

    public static void showImageAlert(final String alertTitle, final String alertContent, final Context con,
                                      final int AlertType, Drawable drawable) {
//        new SweetAlertDialog(con, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
//                .setTitleText(alertTitle)
//                .setContentText(alertContent)
//                .setCustomImage(drawable)
//                .show();
    }

    public static void showInternetAlert(final Context con) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(con);
        // Setting Dialog Title
        alertDialog.setTitle(con.getString(R.string.donthaveinternet));
        // Setting Dialog Message
        alertDialog.setMessage(con.getString(R.string.doactivateinternet));
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                con.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                dialog.dismiss();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
