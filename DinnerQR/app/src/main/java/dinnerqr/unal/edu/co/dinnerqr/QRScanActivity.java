package dinnerqr.unal.edu.co.dinnerqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScanActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private String token = "";
    private String previousToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        //cameraView = QRScanActivity.this.findViewById(R.id.camera_view);

        this.initQR();
    }

    private void initQR() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() > 0){

                    token = barcodes.valueAt(0).displayValue;

                    if(!token.equals(previousToken)) {
                        previousToken = token;
                        Log.i("Token", token);
                    }

                    //TODO: Do something.
                    Toast.makeText(QRScanActivity.this, token, Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(5000);
                                    previousToken = "";
                                }
                            } catch (InterruptedException e) {
                                Log.e("Error", "Waiting didn't work!");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                barcodeDetector.release();
            }
        });

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if(ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA));

                        requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie)
                    {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
    }
}
