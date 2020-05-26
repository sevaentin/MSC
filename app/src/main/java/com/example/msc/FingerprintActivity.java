package com.example.msc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.credenceid.biometrics.Biometrics;


public class FingerprintActivity extends BiometricActivityBase {

    ImageView imageView;
    TextView text_status;
    String finger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        imageView = (ImageView) findViewById(R.id.imageView);
        text_status = (TextView) findViewById(R.id.text_status);
        text_status.setText("Put finger on the sensor");
        imageView.setImageDrawable(null);
        finger = getIntent().getStringExtra("EXTRA_FINGER");
        grabFingerprint();
    }


    public void onFingerprintGrabbed(Biometrics.ResultCode result, Bitmap bitmap, byte[] iso, String filepath, String status)
    {
        if (status != null)
            text_status.setText(status);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            verify(finger, iso);
        }

    }

    private void verify(String minutia, byte[] candidateImage){
        byte[] decodedBytes = new byte[0];

        decodedBytes = Base64.decode(minutia, Base64.DEFAULT);





    }

}
