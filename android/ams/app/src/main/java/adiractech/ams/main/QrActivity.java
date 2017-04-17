package adiractech.ams.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import adiractech.ams.R;
import adiractech.ams.tables.Employee;
import adiractech.ams.utils.Helper;
import okhttp3.OkHttpClient;

import static com.google.android.gms.vision.CameraSource.CAMERA_FACING_FRONT;

public class QrActivity extends AppCompatActivity {

    private SurfaceView cameraView;
    private BarcodeDetector detector;
    private CameraSource source;
    private Context context;
    private EmployeeFinderTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        context = getApplicationContext();

        cameraView = (SurfaceView) findViewById(R.id.qr_camera_view);

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        source = new CameraSource.Builder(this, detector)
                .setFacing(CAMERA_FACING_FRONT)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    source.start(cameraView.getHolder());

                } catch (IOException ie) {

                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                source.stop();
            }

        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() != 0){
                    String secret = barcodes.valueAt(0).displayValue;
                    task = new EmployeeFinderTask(secret);
                    task.execute((Void)null);

                }else{
                    Helper.displayNotification(context,"Attendance Card could not be scanned.\n" +
                                                       "Please try again",true);
                    Intent intent = new Intent(QrActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });



    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class EmployeeFinderTask extends AsyncTask<Void, Void, Boolean> {

        String secret;

        EmployeeFinderTask(String secret) {
            this.secret = secret;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<Employee> employees = Employee.find(Employee.class,
                    "secret = ?", secret);

            if (employees.isEmpty()) {
                return false;
            }
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(QrActivity.this, CountdownActivity.class);
                startActivity(intent);
                finish();

            } else {
                Helper.displayNotification(context, "Invalid Card Presented..\n" +
                        "Please try again", true);
                Intent intent = new Intent(QrActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }





}
