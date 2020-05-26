package com.example.msc;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.credenceid.biometrics.ApduCommand;
import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.CardCommandResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class ReadCardActivity extends BiometricActivityBase {

    TextView txt_id_number;
    TextView txt_last_name;
    TextView txt_first_name;
    TextView txt_immuned;
    TextView txt_error;
    Button btn_checkFinger;
    Model m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_read_card);

        txt_id_number = (TextView) findViewById(R.id.txt_id_number);
        txt_last_name = (TextView) findViewById(R.id.txt_last_name);
        txt_first_name = (TextView) findViewById(R.id.txt_first_name);
        txt_immuned = (TextView)findViewById(R.id.txt_immuned);
        txt_error = (TextView)findViewById(R.id.txt_error);
        btn_checkFinger =  (Button)findViewById(R.id.btn_checkFinger);

        Biometrics.OnCardStatusListener onCardStatusListener = new Biometrics.OnCardStatusListener(){

            @Override
            public void onCardStatusChange(String ATR, int prevState, int currentState) {
                btn_checkFinger.setEnabled(false);
                //status_tv.setText("Status: " + currentState);
                if (currentState == 1) {
                }else if (currentState >= 2 && currentState <= 6) {
                    beep();
                    try {
                        if(selectApplication()){
                            int number = getFileSettings(1);
                            if(number > 0){
                                m = getData(number,1);
                                txt_last_name.setText(m.getLastName());
                                txt_first_name.setText(m.getFirstName());
                                txt_id_number.setText(m.getId());
                                //status_tv7.setText(m.getBirthDate());
                                txt_immuned.setText(m.getStatus());

                                number =  getFileSettings(2);
                                if(number > 0){
                                    m = getData(number,2);
                                    btn_checkFinger.setEnabled(true);
                                }

                            }else{
                                // txt_error.setText("getFileSettings Failed");
                            }
                        }else{
                            //txt_error.setText("selectApplication Failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (prevState == 0 && currentState == 0 && ATR.equals("{ATR-null}")) {
                }
            }
        };

        registerCardStatusListener(onCardStatusListener);

        cardOpenCommand(new Biometrics.CardReaderStatusListner() {
            @Override
            public void onCardReaderOpen(Biometrics.ResultCode resultCode) {
                //status_tv.setText("smart card opened. Status: " + resultCode);
            }

            @Override
            public void onCardReaderClosed(Biometrics.ResultCode resultCode, Biometrics.CloseReasonCode closeReasonCode) {
                // status_tv.setText("smart card closed. Status: " + resultCode);
            }
        });


    }

    public void onCheckFingerprint(View v){
        Intent intent = new Intent(this, FingerprintActivity.class);
        intent.putExtra("EXTRA_FINGER", m.getFinger());
        startActivity(intent);
    }
    private void readXmlFile() throws Exception {
        new Reader(this,null).execute();
    }

    public void callBackData(Model result) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        beep();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        txt_id_number.setText(result.getId());
        txt_last_name.setText(result.getLastName());
        txt_first_name.setText(result.getFirstName());
        txt_immuned.setText(result.getStatus());
    }

    private void beep(){
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,150);
    }


    public boolean selectApplication(){
        try {
            ApduCommand APDU = new ApduCommand("905A00000301000000");
            CardCommandResponse cardCommandResponseData = cardSyncCommand(APDU, true);
            return cardCommandResponseData.result == Biometrics.ResultCode.OK;
        }catch (Exception e){
            txt_error.setText("selectApplication: "+ e.toString());
            return false;
        }
    }

    public int getFileSettings(int file){
        try {
            String hexaFile = padLeftZeros(Integer.toHexString(file),2);
            String firstPart = "90F5000001";
            String secondPart = "00";
            //txt_error.setText(firstPart+hexaFile+secondPart);
            ApduCommand APDU = new ApduCommand(firstPart+hexaFile+secondPart);
            CardCommandResponse cardCommandResponseData = cardSyncCommand(APDU, true);
            if (cardCommandResponseData.result == Biometrics.ResultCode.OK) {
                if(String.format("%02x", (0x0ff) & cardCommandResponseData.sw2).equals("00")){

                    String res = "";
                    for (int i = 0; i < cardCommandResponseData.data.length; i ++) {
                        res += cardCommandResponseData.data[i] & 0xff;;
                    }
                    //txt_error.append("size: " + res);
                    res = res.substring(res.length() - 4);
                    res = res.substring(0,2);
                    return Integer.parseInt(res);
                }
            }
            return 0;
        }catch (Exception e){
            txt_error.setText("getFileSettings: "+ e.toString());
            return 0;
        }
    }

    public Model getData(int number, int flag){
        String s = Integer.toHexString(0);
        //txt_error.append("n: " + number + " id: " + s);
        try {
            Model m = new Model();
            String firstpart = "90BD000007";
            String hexaId = padLeftZeros(Integer.toHexString(flag),2);
            String idPart = hexaId + "0000";
            String hexaNumber = padLeftZeros(s,4);
            String secondpart = "000000";
            //txt_error.append(firstpart +"."+ idPart +"."+ hexaNumber +"."+ secondpart);
            ApduCommand APDU = new ApduCommand(firstpart + idPart + hexaNumber + secondpart);
            CardCommandResponse cardCommandResponseData = cardSyncCommand(APDU, true);
            if (cardCommandResponseData.result == Biometrics.ResultCode.OK) {
                String status = "AF";
                int c = 0;
                String result = "";
                result = new String(cardCommandResponseData.data, "UTF-8");
                while (status.equals("AF") || c < 20) {
                    APDU = new ApduCommand("90AF000000");
                    cardCommandResponseData = cardSyncCommand(APDU, true);
                    status = String.format("%02x", (0x0ff) & cardCommandResponseData.sw2);
                    try {
                        result += new String(cardCommandResponseData.data, "UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    c++;
                }
                //txt_error.append("11:\n" + result);
                if(flag == 2){
                    m.setFinger(result);
                }else {
                    String[] data = result.split(";");
                    m.setLastName(data[0]);
                    m.setFirstName(data[1]);
                    m.setId(data[2]);
                    m.setBirthDate(data[3]);
                    m.setStatus(data[4]);
                }

            }
            return m;
        }catch (Exception e){
            txt_error.setText("getData: "+ e.toString());
            return null;
        }
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
