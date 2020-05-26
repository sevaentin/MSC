package com.example.msc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.credenceid.biometrics.ApduCommand;
import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsActivity;
import com.credenceid.biometrics.BiometricsManager;
import com.credenceid.biometrics.CardCommandResponse;
import com.credenceid.biometrics.FingerprintSyncResponse;
import com.credenceid.biometrics.IrisQuality;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import androidx.appcompat.app.AppCompatActivity;

public class BiometricActivityBase extends AppCompatActivity {
    private static final String TAG = BiometricsActivity.class.getName();
    private BiometricsManager biometricsManager;
    private Biometrics.OnInitializedListener initializedListener = new Biometrics.OnInitializedListener() {
        public void onInitialized(Biometrics.ResultCode result, String sdk_version, String required_version) {
            Log.d(BiometricActivityBase.TAG, "onBiometricsInitialized");
            BiometricActivityBase.this.onBiometricsInitialized(result, sdk_version, required_version);
        }
    };

    public BiometricActivityBase() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (this.biometricsManager == null) {
            this.biometricsManager = new BiometricsManager(this);
        }

        this.initializeBiometrics(this.initializedListener);
    }

    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        this.initializeBiometrics(this.initializedListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.biometricsManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initializeBiometrics(Biometrics.OnInitializedListener listener) {
        this.biometricsManager.initializeBiometrics(listener);
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.finalizeBiometrics(false);
        super.onDestroy();
    }

    public void finalizeBiometrics(boolean stopService) {
        this.biometricsManager.finalizeBiometrics(stopService);
    }

    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    public void cancelCapture() {
        this.biometricsManager.cancelCapture();
    }

    public String getSDKVersion() {
        return this.biometricsManager.getSDKVersion();
    }

    public String getDeviceLibraryVersion() {
        return this.biometricsManager.getDeviceLibraryVersion();
    }

    public String getDeviceId() {
        return this.biometricsManager.getDeviceId();
    }

    public boolean hasCardReader() {
        return this.biometricsManager.hasCardReader();
    }

    public void enableFileAccessOverUsb(boolean enable) {
        this.biometricsManager.enableFileAccessOverUsb(enable);
    }

    public boolean hasUsbFileAccessEnabling() {
        return this.biometricsManager.hasUsbFileAccessEnabling();
    }

    public boolean hasNfcCard() {
        return this.biometricsManager.hasNfcCard();
    }

    public String getProductName() {
        return this.biometricsManager.getProductName();
    }

    public boolean hasFingerprintScanner() {
        return this.biometricsManager.hasFingerprintScanner();
    }

    public Biometrics.FingerprintScannerType getFingerprintScannerType() {
        return this.biometricsManager.getFingerprintScannerType();
    }

    public boolean hasIrisScanner() {
        return this.biometricsManager.hasIrisScanner();
    }

    public boolean hasMrzReader() {
        return this.biometricsManager.hasMrzReader();
    }

    public boolean hasUsbCamera() {
        return this.biometricsManager.hasUsbCamera();
    }

    public boolean cameraFlashControl(boolean flash_state) {
        return this.biometricsManager.cameraFlashControl(flash_state);
    }

    public void getPreferences(String key, Biometrics.PreferencesListener listener) {
        this.biometricsManager.getPreferences(key, listener);
    }

    public void cardCommand(ApduCommand apduCommand, boolean protocolT0T1, Biometrics.OnCardCommandListener listener) {
        this.biometricsManager.cardCommand(apduCommand, protocolT0T1, listener);
    }

    public void cardCommand(ApduCommand apduCommand, boolean protocolT0T1, Biometrics.OnCardCommandSyncResultListener listener) {
        this.biometricsManager.cardCommand(apduCommand, protocolT0T1, listener);
    }

    public void cardOpenCommand(Biometrics.CardReaderStatusListner listener) {
        this.biometricsManager.cardOpenCommand(listener);
    }

    public void cardCloseCommand() {
        this.biometricsManager.cardCloseCommand();
    }

    public boolean cardConnectSync(int timeout) {
        return this.biometricsManager.cardConnectSync(timeout);
    }

    public boolean cardDisconnectSync(int timeout) {
        return this.biometricsManager.cardDisconnectSync(timeout);
    }

    public void registerCardStatusListener(Biometrics.OnCardStatusListener listener) {
        Log.d(TAG, "BiometricsActivity:: registerCardStatusListner BiometricsActivity.java");
        this.biometricsManager.registerCardStatusListener(listener);
    }

    public void registerMrzDocumentStatusListener(Biometrics.OnMrzDocumentStatusListener listener) {
        Log.d(TAG, "BiometricsActivity:: registerMrzDocumentStatusListener BiometricsActivity.java");
        this.biometricsManager.registerMrzDocumentStatusListener(listener);
    }

    public void ektpCardReadCommand(int command, byte[] data, Biometrics.OnEktpCardReadListener listener) {
        this.biometricsManager.ektpCardReadCommand(command, data, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, Biometrics.OnFingerprintGrabbedListener listener) {
        this.biometricsManager.grabFingerprint(scanType, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, boolean saveToDisk, Biometrics.OnFingerprintGrabbedListener listener) {
        this.biometricsManager.grabFingerprint(scanType, saveToDisk, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, boolean saveToDisk, Biometrics.OnFingerprintGrabbedRawListener listener) {
        this.biometricsManager.grabFingerprint(scanType, saveToDisk, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, Biometrics.OnFingerprintGrabbedFullListener listener) {
        this.biometricsManager.grabFingerprint(scanType, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, boolean saveToDisk, Biometrics.OnFingerprintGrabbedFullListener listener) {
        this.biometricsManager.grabFingerprint(scanType, saveToDisk, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, Biometrics.OnFingerprintGrabbedWSQListener listener) {
        this.biometricsManager.grabFingerprint(scanType, listener);
    }

    public void grabFingerprint(Biometrics.ScanType scanType, boolean saveToDisk, Biometrics.OnFingerprintGrabbedWSQListener listener) {
        this.biometricsManager.grabFingerprint(scanType, saveToDisk, listener);
    }

    public void convertToWsq(String inputPathname, float bitrate, Biometrics.OnConvertToWsqListener listener) {
        this.biometricsManager.convertToWsq(inputPathname, bitrate, listener);
    }

    public void decompressWsq(String inputPathname, Biometrics.OnDecompressWsqListener listener) {
        this.biometricsManager.decompressWsq(inputPathname, listener);
    }

    public void getFingerQuality(Bitmap bitmap, Biometrics.OnGetFingerQualityListener listener) {
        this.biometricsManager.getFingerQuality(bitmap, listener);
    }

    public void getFingerQuality(String filePath, Biometrics.OnFingerQualityListener listener) {
        this.biometricsManager.getFingerQuality(filePath, listener);
    }

    public void openFingerprintReader(Biometrics.FingerprintReaderStatusListener listener) {
        this.biometricsManager.openFingerprintReader(listener);
    }

    public void closeFingerprintReader() {
        this.biometricsManager.closeFingerprintReader();
    }

    public void captureIrises(Biometrics.EyeSelection eyeSelection, Biometrics.OnIrisesCapturedListener listener) {
        this.biometricsManager.captureIrises(eyeSelection, listener);
    }

    public void closeIrisScanner() {
        this.biometricsManager.closeIrisScanner();
    }

    public boolean hasFmdMatcher() {
        return this.biometricsManager.hasFmdMatcher();
    }

    public void convertToFmd(Bitmap inputImage, Biometrics.FmdFormat format, Biometrics.OnConvertToFmdListener listener) {
        this.biometricsManager.convertToFmd(inputImage, format, listener);
    }

    public void convertToFmd(byte[] wsqArray, Biometrics.FmdFormat format, Biometrics.OnConvertToFmdListener listener) {
        this.biometricsManager.convertToFmd(wsqArray, format, listener);
    }

    public void compareFmd(byte[] fmd1, byte[] fmd2, Biometrics.FmdFormat format, Biometrics.OnCompareFmdListener listener) {
        this.biometricsManager.compareFmd(fmd1, fmd2, format, listener);
    }

    public void openMRZ(Biometrics.MRZStatusListener listener) {
        this.biometricsManager.openMRZ(listener);
    }

    public void closeMRZ() {
        this.biometricsManager.closeMRZ();
    }

    public void readMRZ(Biometrics.OnMrzReadListener listener) {
        Log.d(TAG, "Biometrics Activity : readMRZ()");
        this.biometricsManager.readMRZ(listener);
    }

    public void registerMrzReadListener(Biometrics.OnMrzReadListener mrzReadListener) {
        Log.d(TAG, "BiometricsActivity:: registerMrzReadListener");
        this.biometricsManager.registerMrzReadListener(mrzReadListener);
    }

    public void ePassportOpenCommand(Biometrics.EpassportReaderStatusListner ePassportReaderStatusListener) {
        this.biometricsManager.ePassportOpenCommand(ePassportReaderStatusListener);
    }

    public void ePassportCloseCommand() {
        this.biometricsManager.ePassportCloseCommand();
    }

    public void ePassportCommand(ApduCommand apduCommand, boolean protocolT0T1, Biometrics.OnEpassportCommandListener listener) {
        this.biometricsManager.ePassportCommand(apduCommand, protocolT0T1, listener);
    }

    public void registerEpassportCardStatusListener(Biometrics.OnEpassportCardStatusListener listener) {
        Log.d(TAG, "BiometricsActivity:: registerEpassportCardStatusListener BiometricsActivity.java");
        this.biometricsManager.registerEpassportCardStatusListener(listener);
    }

    public void setPreferences(String key, String value, Biometrics.PreferencesListener listener) {
        this.biometricsManager.setPreferences(key, value, listener);
    }

    public void convertFmdToCcf(byte[] fmdTemplate, Biometrics.OnFmdToCcfConversionListener listener) {
        this.biometricsManager.convertFmdToCcf(fmdTemplate, listener);
    }

    public void convertCcfToFmd(byte[] ccfTemplate, Biometrics.OnCcfToFmdConverionListener listener) {
        this.biometricsManager.convertCcfToFmd(ccfTemplate, listener);
    }

    public void convertCcfToFmd(byte[] ccfTemplate, short sizeX, short sizeY, short resolutionX, short resolutionY, Biometrics.OnCcfToFmdConverionListener listener) {
        this.biometricsManager.convertCcfToFmd(ccfTemplate, sizeX, sizeY, resolutionX, resolutionY, listener);
    }

    public boolean biometricsInitialized() {
        return this.biometricsManager.biometricsInitialized();
    }

    public void onBiometricsInitialized(Biometrics.ResultCode result, String sdkVersion, String requiredVersion) {
    }

    public void onPhotoTaken(Biometrics.ResultCode result, Bitmap bm, String photoPathname) {
    }

    public CardCommandResponse cardSyncCommand(ApduCommand apduCommand, boolean protocolT0T1) {
        return this.biometricsManager.cardCommandSync(apduCommand, protocolT0T1, 5000);
    }

    public void OnEktpCardRead(Biometrics.ResultCode result, String hint, byte[] response) {
    }

    public void eKtpCardReadCommand(int command, byte[] data) {
        this.biometricsManager.ektpCardReadCommand(command, data, new Biometrics.OnEktpCardReadListener() {
            public void OnEktpCardRead(Biometrics.ResultCode result, String hint, byte[] response) {
                BiometricActivityBase.this.OnEktpCardRead(result, hint, response);
            }
        });
    }

    public void grabFingerprint() {
        this.biometricsManager.grabFingerprint(Biometrics.ScanType.SINGLE_FINGER, new Biometrics.OnFingerprintGrabbedListener() {
            public void onFingerprintGrabbed(Biometrics.ResultCode result, Bitmap bm, byte[] iso, String filepath, String hint) {
                BiometricActivityBase.this.onFingerprintGrabbed(result, bm, iso, filepath, hint);
            }

            public void onCloseFingerprintReader(Biometrics.ResultCode result, Biometrics.CloseReasonCode reason) {
            }
        });
    }

    public void grabFingerprint(Biometrics.ScanType scanType) {
        this.biometricsManager.grabFingerprint(scanType, new Biometrics.OnFingerprintGrabbedListener() {
            public void onFingerprintGrabbed(Biometrics.ResultCode result, Bitmap bm, byte[] iso, String filepath, String hint) {
                BiometricActivityBase.this.onFingerprintGrabbed(result, bm, iso, filepath, hint);
            }

            public void onCloseFingerprintReader(Biometrics.ResultCode result, Biometrics.CloseReasonCode reason) {
            }
        });
    }

    public void onFingerprintGrabbed(Biometrics.ResultCode result, Bitmap bitmap, byte[] iso, String filepath, String status) {
    }

    public FingerprintSyncResponse grabFingerprintSync(int timeout) {
        return this.biometricsManager.grabFingerprintSync(timeout);
    }

    public void captureIrises() {
        this.biometricsManager.captureIrises(Biometrics.EyeSelection.BOTH_EYES, new Biometrics.OnIrisesCapturedListener() {
            public void onIrisesCaptured(Biometrics.ResultCode result, Bitmap bmLeft, Bitmap bmRight, String pathnameLeft, String pathnameRight, String status) {
                BiometricActivityBase.this.onIrisesCaptured(result, bmLeft, bmRight, pathnameLeft, pathnameRight, status);
            }

            public void onCloseIrisScanner(Biometrics.ResultCode result, Biometrics.CloseReasonCode reason) {
            }
        });
    }

    public void captureIrises(Biometrics.EyeSelection eyeSelection) {
        this.biometricsManager.captureIrises(eyeSelection, new Biometrics.OnIrisesCapturedListener() {
            public void onIrisesCaptured(Biometrics.ResultCode result, Bitmap bmLeft, Bitmap bmRight, String pathnameLeft, String pathnameRight, String status) {
                BiometricActivityBase.this.onIrisesCaptured(result, bmLeft, bmRight, pathnameLeft, pathnameRight, status);
            }

            public void onCloseIrisScanner(Biometrics.ResultCode result, Biometrics.CloseReasonCode reason) {
            }
        });
    }

    public void onIrisesCaptured(Biometrics.ResultCode result, Bitmap leftBitmap, Bitmap rightBitmap, String leftFilepath, String rightFilepath, String status) {
    }

    public void onMrzRead(Biometrics.ResultCode result, String hint, byte[] rawData, String rawString, String parsedString) {
    }

    public void readMRZ() {
        this.biometricsManager.readMRZ(new Biometrics.OnMrzReadListener() {
            public void onMrzRead(Biometrics.ResultCode result, String hint, byte[] rawData, String rawString, String parsedString) {
                BiometricActivityBase.this.onMrzRead(result, hint, rawData, rawString, parsedString);
            }
        });
    }

    public void updateNfcStatus(Biometrics.OnNfcUpdateListener listener) {
        this.biometricsManager.updateNfcStatus(listener);
    }

    public void onNfcUpdateStatus(Biometrics.ResultCode result, String str) {
    }

    public void updateNfcStatus() {
        this.biometricsManager.updateNfcStatus(new Biometrics.OnNfcUpdateListener() {
            public void onNfcUpdateStatus(Biometrics.ResultCode result, String str) {
                BiometricActivityBase.this.onNfcUpdateStatus(result, str);
            }
        });
    }

    public void convertToKind7(String leftInputPathname, String rightInputPathname) {
        this.biometricsManager.convertToKind7(leftInputPathname, rightInputPathname, new Biometrics.OnConvertToKind7Listener() {
            public void onConvertToKind7(Biometrics.ResultCode result, String pathnameLeft, IrisQuality iqLeft, String pathnameRight, IrisQuality iqRight) {
                BiometricActivityBase.this.onConvertToKind7(result, pathnameLeft, iqLeft, pathnameRight, iqRight);
            }
        });
    }

    public void onConvertToKind7(Biometrics.ResultCode result, String leftOutputPathname, IrisQuality leftIrisQuality, String rightOutputPathname, IrisQuality rightIrisQuality) {
    }

    public void convertToKind7(String leftInputPathname, String rightInputPathname, Biometrics.OnConvertToKind7Listener listener) {
        this.biometricsManager.convertToKind7(leftInputPathname, rightInputPathname, listener);
    }

    public void enrollBiometric(String name, String gps, String fpFileName, String leftIrisFileName, String rightIrisFileName, String faceFileName, Biometrics.OnEnrollListener listener) {
        this.biometricsManager.enrollBiometric(name, gps, fpFileName, leftIrisFileName, rightIrisFileName, faceFileName, listener);
    }

    public void enrollBiometric(String name, String gps, String fpFileName, String leftIrisFileName, String rightIrisFileName, String faceFileName) {
        this.biometricsManager.enrollBiometric(name, gps, fpFileName, leftIrisFileName, rightIrisFileName, faceFileName, new Biometrics.OnEnrollListener() {
            public void onEnroll(int result, String statusString) {
                BiometricActivityBase.this.onEnroll(result, statusString);
            }
        });
    }

    public void onEnroll(int result, String statusString) {
    }

    public void matchBiometric(String fingerprintFileName, String leftIrisFileName, String rightIrisFileName, String faceFileName, Biometrics.OnMatchListener listener) {
        this.biometricsManager.matchBiometric(fingerprintFileName, leftIrisFileName, rightIrisFileName, faceFileName, listener);
    }

    public void matchBiometric(String fingerprintFileName, String leftIrisFileName, String rightIrisFileName, String faceFileName) {
        this.biometricsManager.matchBiometric(fingerprintFileName, leftIrisFileName, rightIrisFileName, faceFileName, new Biometrics.OnMatchListener() {
            public void onMatch(int result, String name, long id, String gps, int fpMatch, int irisMatch, int faceMatch, String faceImage) {
                BiometricActivityBase.this.onMatch(result, name, id, gps, fpMatch, irisMatch, faceMatch, faceImage);
            }
        });
    }

    public void onMatch(int result, String name, long id, String gps, int fpMatch, int irisMatch, int faceMatch, String faceImage) {
    }

    public void getFaceQuality(String inputPathname, Biometrics.OnGetFaceQualityListener listener) {
        this.biometricsManager.getFaceQuality(inputPathname, listener);
    }

    public void checkAlgorithmLicenses() {
        this.biometricsManager.checkAlgorithmLicenses(new Biometrics.OnCheckAlgorithmLicensesListener() {
            public void onCheckAlgorithmLicenses(Biometrics.ResultCode result) {
                BiometricActivityBase.this.onCheckAlgorithmLicenses(result);
            }
        });
    }

    public void onCheckAlgorithmLicenses(Biometrics.ResultCode result) {
    }

    public void checkAlgorithmLicenses(Biometrics.OnCheckAlgorithmLicensesListener listener) {
        this.biometricsManager.checkAlgorithmLicenses(listener);
    }

    public void convertToJpeg(String input_pathname, int quality, Biometrics.OnConvertToJpegListener listener) {
        this.biometricsManager.convertToJpeg(input_pathname, quality, listener);
    }

    public void convertToJpeg(String inputPathname, int quality) {
        this.biometricsManager.convertToJpeg(inputPathname, quality, new Biometrics.OnConvertToJpegListener() {
            public void onConvertToJpeg(Biometrics.ResultCode result, String pathname) {
                BiometricActivityBase.this.onConvertToJpeg(result, pathname);
            }
        });
    }

    public void onConvertToJpeg(Biometrics.ResultCode result, String output_pathname) {
    }

    /** @deprecated */
    @Deprecated
    public String getDeviceTypeFromDisk() {
        String DEVICE_TYPE_FILE = Environment.getExternalStorageDirectory().getPath() + "/credenceid/device-type";
        String ALT_DEVICE_TYPE_FILE = "/etc/device-type";
        File f = new File(DEVICE_TYPE_FILE);
        String deviceTypeFile;
        if (!f.exists()) {
            f = new File(ALT_DEVICE_TYPE_FILE);
            if (!f.exists()) {
                return this.getDeviceModel();
            }

            deviceTypeFile = ALT_DEVICE_TYPE_FILE;
        } else {
            deviceTypeFile = DEVICE_TYPE_FILE;
        }

        String line = null;
        Log.d(TAG, " determineDeviceType - file: " + deviceTypeFile);

        try {
            InputStream fis = new FileInputStream(deviceTypeFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            if ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase("trident-1")) {
                    br.close();
                    return "trident-1";
                }

                if (line.equalsIgnoreCase("trident-2")) {
                    br.close();
                    return "trident-2";
                }

                if (line.equalsIgnoreCase("trident-3")) {
                    br.close();
                    return "trident-3";
                }

                if (line.equalsIgnoreCase("twizzler")) {
                    br.close();
                    return "twizzler";
                }

                if (line.equalsIgnoreCase("credenceone")) {
                    br.close();
                    return "credenceone";
                }

                if (line.equalsIgnoreCase("credenceone-v2")) {
                    br.close();
                    return "credenceone-v2";
                }

                if (line.equalsIgnoreCase("credence one")) {
                    br.close();
                    return "credenceone";
                }

                if (line.equalsIgnoreCase("credenceone-v3")) {
                    br.close();
                    return "credenceone-v3";
                }

                if (line.equalsIgnoreCase("starlight")) {
                    br.close();
                    return "starlight";
                }

                br.close();
                Log.d(TAG, "Unknown device identifier");
                return "unknown";
            }

            br.close();
        } catch (Exception var8) {
            Log.w(TAG, "determineDeviceType - " + var8.getMessage());
        }

        return this.getDeviceModel();
    }

    /** @deprecated */
    public String getDeviceModel() {
        String model = Build.MODEL;
        String credenceDeviceType = System.getProperty("ro.credenceid.product");
        if (credenceDeviceType != null) {
            return credenceDeviceType;
        } else if (model.equals("GT-I9300")) {
            return "trident-1";
        } else if (model.equals("Nexus 4")) {
            return "trident-2";
        } else if (model.equals("msm8960")) {
            return "trident-3";
        } else if (model.equals("YAWEI")) {
            return "credenceone";
        } else if (model.equals("twizzler")) {
            return "twizzler";
        } else {
            return model.equals("GT-I8190") ? "credence one v3" : "unknown";
        }
    }
}
