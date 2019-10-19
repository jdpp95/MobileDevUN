package dinnerqr.unal.edu.co.dinnerqr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ZXingScannerView.ResultHandler{

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btnScan){
            //Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            //startActivity(intent);
            mScannerView = new ZXingScannerView(this);
            setContentView(mScannerView);
            mScannerView.setResultHandler(this);
            setCameraPermissions();
        }
    }

    private void setCameraPermissions() {
        if(ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA));
                requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else { 
            mScannerView.startCamera();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("HandleResult", rawResult.getText());
        Toast.makeText(HomeActivity.this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        mScannerView.resumeCameraPreview(this);
        Intent intent = new Intent(this, MenuActivity.class);
        this.startActivity(intent);
    }
}
