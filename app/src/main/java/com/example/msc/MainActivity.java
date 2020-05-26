package com.example.msc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.credenceid.biometrics.Biometrics;

import java.util.Hashtable;

public class MainActivity extends BiometricActivityBase {

    private static Hashtable cardTable = new Hashtable();
    Button btn_readCard;
    TextView status_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        btn_readCard = (Button) findViewById(R.id.btn_readCard);

    }

    public void onLogIn(View v){
        Intent intent = new Intent(this, ReadCardActivity.class);
        startActivity(intent);
    }
}
