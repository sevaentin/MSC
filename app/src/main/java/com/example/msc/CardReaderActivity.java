package com.example.msc;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.credenceid.biometrics.ApduCommand;
import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.CardCommandResponse;
import com.example.msc.services.SmartCardService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;


enum ApduState {
    APDU_INIT,
    APDU_PROCESS
}

public class CardReaderActivity extends BiometricActivityBase {

    TextView status_tv;
    TextView status_tv1;
    TextView status_tv2;
    TextView status_tv3;
    TextView status_tv4;
    TextView status_tv5;
    TextView status_tv6;
    TextView status_tv7;
    TextView status_tv8;

    SmartCardService smc;

    private static Hashtable cardTable = new Hashtable();
    public String APDU_lastdesc = "{unknown}";
    public String cardName = "{unknown}";
    public String[] APDU_table = com.example.msc.CardDataTables.noAPDU_table;
    /* Current index in Hashtable for which we are referring to. */
    int apduTableIndex = 0;
    /* Holds current APDU command state of CardReader. */
    ApduState apduState = ApduState.APDU_INIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);

        status_tv = (TextView) findViewById(R.id.status_tv);
        status_tv1 = (TextView) findViewById(R.id.status_tv1);
        status_tv2 = (TextView) findViewById(R.id.status_tv2);
        status_tv3 = (TextView) findViewById(R.id.status_tv3);
        status_tv4 = (TextView) findViewById(R.id.status_tv4);
        status_tv5 = (TextView) findViewById(R.id.status_tv5);
        status_tv6 = (TextView) findViewById(R.id.status_tv6);
        status_tv7 = (TextView) findViewById(R.id.status_tv7);
        status_tv8 = (TextView) findViewById(R.id.status_tv8);

        Biometrics.OnCardStatusListener onCardStatusListener = new Biometrics.OnCardStatusListener(){

            @Override
            public void onCardStatusChange(String ATR, int prevState, int currentState) {
                status_tv2.setText("\"Card status changed !\"");
                APDU_table = com.example.msc.CardDataTables.noAPDU_table;;
                apduState = ApduState.APDU_INIT;
                apduTableIndex = 0;
                status_tv3.setText("currentState: " + currentState);
                if (currentState == 1) {
                    status_tv3.setText("Card Removed.\nPrevious State:" + prevState + "\nCurrent State:" + currentState);
                }else if (currentState >= 2 && currentState <= 6) {
                    CardInfo ci = (CardInfo) cardTable.get(ATR);
                    String localCardName = (ci != null) ? ci.getCardName() : "{unknown}";
                    //APDU_table = (ci != null) ? ci.getApduTable() : APDU_table;
                    /* Set global cardName variable to match local's. */
                    cardName = localCardName;
                    status_tv3.setText("Card Inserted" +
                            "\nPrevious State:" + prevState +
                            "\nCurrent State:" + currentState +
                            "\nCard:" + localCardName +
                            "\nATR:\n" + ATR);
                    //beep();
                    //cardOpened(true);
                    //status_tv4.setText("APDU [" + APDU_table[apduTableIndex + 1] + "]");
                }else if (prevState == 0 && currentState == 0 && ATR.equals("{ATR-null}")) {
                }
            }
        };

        registerCardStatusListener(onCardStatusListener);

        status_tv.setText("opening card reader");
        cardOpenCommand(new Biometrics.CardReaderStatusListner() {
            @Override
            public void onCardReaderOpen(Biometrics.ResultCode resultCode) {
                status_tv1.setText("smart card opened. Status: " + resultCode);
                status_tv4.setText("");
                status_tv5.setText("");
                status_tv6.setText("");
                status_tv7.setText("");
                status_tv8.setText("");
            }

            @Override
            public void onCardReaderClosed(Biometrics.ResultCode resultCode, Biometrics.CloseReasonCode closeReasonCode) {
                status_tv4.setText("");
                status_tv5.setText("");
                status_tv6.setText("");
                status_tv7.setText("");
                status_tv8.setText("");
            }
        });
    }



    public void onOpen(View v) {
        status_tv4.setText("");
        status_tv5.setText("");
        status_tv6.setText("");
        status_tv7.setText("");
        status_tv8.setText("");
        cardOpened(true);
    }

    private void cardOpened(boolean readCard)  {
        status_tv4.setText("before ApduCommand");

        if(selectApplication()){
            if(getFileSettings()){
                Model m = getData();
                status_tv4.setText("after model");
                status_tv4.setText(m.getLastName());
                status_tv5.setText(m.getFirstName());
                status_tv6.setText(m.getId());
                status_tv7.setText(m.getBirthDate());
                status_tv8.setText(m.getStatus());
            }else{
                status_tv4.setText("getFileSettings Failed");
            }
       }else{
            status_tv4.setText("selectApplication Failed");
        }
    }
    public void onClose(View v) {

    }

    private String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    private byte[] getArray(List<byte[]> bytesList) {
        byte[] bytes = new byte[1000];
        for (int i = 0; i < bytesList.size(); i++) {
            for (int j = 0; j < bytesList.get(i).length; j++) {
                bytes[j] = bytesList.get(i)[j];
            }
        }

        return bytes;
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
            status_tv8.setText("selectApplication: "+ e.toString());
            return false;
        }
    }

    public boolean getFileSettings(){
        try {
            ApduCommand APDU = new ApduCommand("90F50000010100");
            CardCommandResponse cardCommandResponseData = cardSyncCommand(APDU, true);
            if (cardCommandResponseData.result == Biometrics.ResultCode.OK) {

                return String.format("%02x", (0x0ff) & cardCommandResponseData.sw2).equals("00");
            }
            return false;
        }catch (Exception e){
            status_tv8.setText("getFileSettings: "+ e.toString());
            return false;
        }
    }

    public Model getData(){
        try {
            Model m = new Model();
            ApduCommand APDU = new ApduCommand("90BD000007010000005B000000");
            CardCommandResponse cardCommandResponseData = cardSyncCommand(APDU, true);
            if (cardCommandResponseData.result == Biometrics.ResultCode.OK) {
                String status = "AF";
                int c = 0;
                String result = "";
                result = new String(cardCommandResponseData.data, "UTF-8");
                while (status.equals("AF") || c < 5) {
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

                String[] data = result.split(";");
                //status_tv8.setText("data length: "+ data.length);
                m.setLastName(data[0]);
                m.setFirstName(data[1]);
                m.setId(data[2]);
                m.setBirthDate(data[3]);
                m.setStatus(data[4]);
            }
            return m;
        }catch (Exception e){
            status_tv8.setText("getData: "+ e.toString());
            return null;
        }
    }

}
