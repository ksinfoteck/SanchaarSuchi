package in.kamalaenterprize.sncharsuchi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.commonutility.PreferenceConnector;
import in.kamalaenterprize.commonutility.WebServiceListener;

public class QRCodeGenerator extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private Context svContext;
    private GlobalData gd;
//    private EditText edtValue;
    private ImageView qrImage;
//    private String inputValue;
//    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    //    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;
    private String qrcodetext = "";
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        svContext = this;
        gd = new GlobalData(svContext);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            ViewGroup root = (ViewGroup) findViewById(R.id.mylayout);
            GlobalData.setFont(root, font);
        }

        qrImage = findViewById(R.id.qr_image);
        Button btnGenerateCode = findViewById(R.id.generate_barcode);
        btnBack = (ImageView)findViewById(R.id.img_back);
        activity = this;

        qrcodetext = PreferenceConnector.readString(this, PreferenceConnector.YODDHAID, "");

        btnGenerateCode.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        btnGenerateCode.performClick();

        findViewById(R.id.save_barcode).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
//                        boolean save = new QRGSaver().save(savePath, edtValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
//                        String result = save ? "Image Saved" : "Image Not Saved";
//                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
//                        int imgWidth = 300;
//                        int imgHeight = 300;

                        SaveQrcode(qrcodetext);
//                        edtValue.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });
    }

    private void GenerateQrcode(String strValue) {
        Bitmap bitmap = QRCodeHelper
                .newInstance(this)
                .setContent(strValue)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();

        qrImage.setImageBitmap(bitmap);

    }

    private void SaveQrcode(String strValue) throws IOException {
        Bitmap bitmap = QRCodeHelper
                .newInstance(this)
                .setContent(strValue)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();

        // Assume block needs to be inside a Try/Catch block.
        OutputStream fOut = null;
        Integer counter = 0;
        File file = new File(GlobalVariables.defaultAppPath, "yoddha_"+counter+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.


        if (!file.exists()) {
            file.mkdirs();
        }

        fOut = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut.flush(); // Not really required
        fOut.close(); // do not forget to close the stream

        MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

    }

//    private void callWebServiceWithoutPreUrl(String postUrl, LinkedHashMap<String, String> hash, String showText) {
//        WebServiceWithoutPreUrl webService = new WebServiceWithoutPreUrl(svContext, postUrl, hash, this, WebService.GET, showText);
//        webService.execute();
//    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generate_barcode:
                GenerateQrcode(qrcodetext);
                break;
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
