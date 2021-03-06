package adiractech.ams.main;

import android.content.Context;

import com.google.android.gms.vision.Detector;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import adiractech.ams.R;
import adiractech.ams.intents.ServerUpdationIntentService;
import adiractech.ams.setup.FranchiseSelectionActivity;
import adiractech.ams.setup.LoginActivity;
import adiractech.ams.tables.Attendance;
import adiractech.ams.utils.Cache;
import adiractech.ams.utils.Constants;
import adiractech.ams.utils.Helper;

import static com.google.android.gms.vision.CameraSource.CAMERA_FACING_FRONT;

public class FaceActivity extends AppCompatActivity {

    private SurfaceView cameraView;
    private FaceDetector detector;
    private CameraSource source;
    private int action;
    private int employeeId;
    private long inTime;
    private String fileName;
    private Context context;
    private CameraSource.ShutterCallback shutterCallback;
    private CameraSource.PictureCallback pictureCallback;
    private boolean faceDetected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        Intent intent = getIntent();

        action = intent.getIntExtra(Constants.BUNDLE_ACTION,
                                         Constants.ATTENDANCE_ACTION_NA);
        employeeId = intent.getIntExtra(Constants.BUNDLE_EMPLOYEE_ID,
                                             Constants.EMPLOYEE_ID_INVALID);
        inTime = intent.getLongExtra(Constants.BUNDLE_IN_TIME,Constants.INVALID_TIME);
        fileName = intent.getStringExtra(Constants.BUNDLE_FILE_NAME);
        context = getApplicationContext();

        cameraView = (SurfaceView) findViewById(R.id.face_camera_view);

        detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        if (!detector.isOperational()) {
            Helper.displayNotification(context,"FaceDetection Library Missing.\n" +
                    "Kindly Connect to Internet and reinstall the app",true);
        }

        source = new CameraSource.Builder(this, detector)
                .setFacing(CAMERA_FACING_FRONT)
                .build();

        shutterCallback = new CameraSource.ShutterCallback(){
           @Override
           public void onShutter(){

           }
        };

        pictureCallback = new CameraSource.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] data){
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);

                    if(bitmap!=null){

                        String path = Constants.DEFAULT_SAVE_DIRECTORY + "/" +
                                         Integer.toString(employeeId) + "/" +
                                         Integer.toString(action);

                        File file=new File(path);
                        if(!file.isDirectory()){
                            file.mkdirs();
                        }



                        file=new File(path,fileName + ".jpg");


                        try
                        {
                            FileOutputStream fileOutputStream=new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);

                            fileOutputStream.flush();
                            fileOutputStream.close();

                            new ServerUpdateTask(path+"/"+fileName +".jpg").execute((Void)null);

                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                        catch(Exception exception)
                        {
                            exception.printStackTrace();
                        }

                    }
                }

            }
        };




        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    source.start(cameraView.getHolder());

                } catch (Exception e) {
                   Log.d("AMS",e.toString());
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

        detector.setProcessor(new Detector.Processor<Face>() {
            @Override
            public void release() {

            }



            @Override

            public void receiveDetections(Detector.Detections<Face> detections) {
                final SparseArray<Face> faces = detections.getDetectedItems();

                if(faces.size() != 0 && !faceDetected){
                   source.takePicture(shutterCallback,pictureCallback);
                    faceDetected = true;

                }/*else{
                    Helper.displayNotification(context,"Attendance Card could not be scanned.\n" +
                        "Please try again",true);
                }*/
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        //source.release();

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ServerUpdateTask extends AsyncTask<Void, Void, Boolean> {
        private String failureReason;
        private String imagePath;

        ServerUpdateTask(String path){
            failureReason = "";
            imagePath = path;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<Attendance> records = Attendance.find(Attendance.class,
                    "employee_id = ? and in_time = ?",Integer.toString(employeeId),
                    Long.toString(inTime));
            switch(action){
                case Constants.ATTENDANCE_ACTION_ENTER: {

                    if (records.isEmpty()) {
                        failureReason = "Entry recorded not captured. Please contact Administrator";
                        return false;
                    }

                    Attendance record = records.get(0);
                    record.setInTimeImagePath(imagePath);
                    record.save();
                    ServerUpdationIntentService.startActionLogEntry(getApplicationContext(),
                                                                    employeeId,inTime);
                }
                    break;
                case Constants.ATTENDANCE_ACTION_EXIT: {
                    Attendance record = records.get(0);
                    record.setOutTimeImagePath(imagePath);
                    record.save();
                    ServerUpdationIntentService.startActionLogExit(getApplicationContext(),
                            employeeId,inTime,record.getOutTime());
                }
                    break;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(FaceActivity.this, MainActivity.class);
                intent.putExtra(Constants.BUNDLE_NOTIFICATION,"Thank you. Your record has been captured");
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(FaceActivity.this, MainActivity.class);
                intent.putExtra(Constants.BUNDLE_NOTIFICATION,failureReason);
                startActivity(intent);
                finish();

            }
        }

        @Override
        protected void onCancelled() {

        }

    }

    }
