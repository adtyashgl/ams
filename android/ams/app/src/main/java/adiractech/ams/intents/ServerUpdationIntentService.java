package adiractech.ams.intents;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import adiractech.ams.tables.Attendance;
import adiractech.ams.tables.Employee;
import adiractech.ams.utils.Constants;
import adiractech.ams.utils.Helper;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static adiractech.ams.utils.Constants.ATTENDANCE_STATUS_IN;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServerUpdationIntentService extends IntentService {


    public ServerUpdationIntentService() {
        super("ServerUpdationIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.SERVER_ACTION_ENTER.equals(action)) {
                final int employeeId = intent.getIntExtra(Constants.BUNDLE_EMPLOYEE_ID,
                        Constants.EMPLOYEE_ID_INVALID);
                final long inTime = intent.getLongExtra(Constants.BUNDLE_IN_TIME,
                                                       Constants.INVALID_TIME);


                handleActionLogEntry(employeeId,inTime);

            } else if (Constants.SERVER_ACTION_EXIT.equals(action)) {
                final int employeeId = intent.getIntExtra(Constants.BUNDLE_EMPLOYEE_ID,
                        Constants.EMPLOYEE_ID_INVALID);
                final long inTime = intent.getLongExtra(Constants.BUNDLE_IN_TIME,
                        Constants.INVALID_TIME);
                final long outTime = intent.getLongExtra(Constants.BUNDLE_OUT_TIME,
                        Constants.INVALID_TIME);
                handleActionLogExit(employeeId,inTime,outTime);
            }  else if (Constants.SERVER_ACTION_PUSH_TIMER_EXPIRY.equals(action)) {
                handleActionPushTimerExpired();
            }
        }
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionLogEntry(Context context,int employeeId,long inTime) {
        Intent intent = new Intent(context, ServerUpdationIntentService.class);
        intent.setAction(Constants.SERVER_ACTION_ENTER);
        intent.putExtra(Constants.BUNDLE_EMPLOYEE_ID, employeeId);
        intent.putExtra(Constants.BUNDLE_IN_TIME, inTime);

        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionLogExit(Context context,int employeeId,long inTime,long outTime) {
        Intent intent = new Intent(context, ServerUpdationIntentService.class);
        intent.setAction(Constants.SERVER_ACTION_EXIT);
        intent.putExtra(Constants.BUNDLE_EMPLOYEE_ID, employeeId);
        intent.putExtra(Constants.BUNDLE_IN_TIME, inTime);
        intent.putExtra(Constants.BUNDLE_OUT_TIME, outTime);
        context.startService(intent);
    }

    public static void startActionPushTimerExpiry(Context context){
        Intent intent = new Intent(context, ServerUpdationIntentService.class);
        intent.setAction(Constants.SERVER_ACTION_PUSH_TIMER_EXPIRY);
        context.startService(intent);


    }

    /**********************************************************************************************
     *
     *                       Handler Functions
     *
     *********************************************************************************************/

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionLogEntry(int employeeId,long inTime) {
        if (!Helper.isConnected(getApplicationContext())) {
            //set the state of the entry object to NOT in sync
            setAttendanceStateOutOfSync(employeeId,inTime);
             sendResult(Constants.TRIGGER_ACTION_ENTRY, Constants.RET_NOK,
                    Constants.ERROR_NO_CONNECTION);

            return;
        }


        try {
            OkHttpClient client = new OkHttpClient();
            Request request = buildLogEntryRequest(employeeId,inTime);

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JSONObject json = new JSONObject(responseData);
                int status = json.optInt("status", Constants.RET_NOK);

                if(status == Constants.RET_OK){
                    setAttendanceStateInSync(employeeId,inTime);

                    sendResult(Constants.TRIGGER_ACTION_ENTRY,Constants.RET_OK);

                }else{
                    String reason = json.optString("reason");
                    sendResult(Constants.TRIGGER_ACTION_ENTRY, Constants.RET_NOK,
                            reason);
                    response.body().close();
                    return;

                }
            } else {
                String reason = response.message();
                sendResult(Constants.TRIGGER_ACTION_ENTRY, Constants.RET_NOK,
                        reason);
                response.body().close();
                return;

            }
        }catch(JSONException ioe) {
            setAttendanceStateOutOfSync(employeeId,inTime);
            sendResult(Constants.TRIGGER_ACTION_ENTRY, Constants.RET_NOK,
                    Constants.ERROR_INVALID_RESPONSE);

        } catch (java.io.IOException ioe) {
            setAttendanceStateOutOfSync(employeeId,inTime);
            sendResult(Constants.TRIGGER_ACTION_ENTRY, Constants.RET_NOK,
                    Constants.ERROR_NO_CONNECTION);

        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionLogExit(int employeeId,long inTime, long outTime) {

        if (!Helper.isConnected(getApplicationContext())) {
            //set the state of the entry object to NOT in sync
            setAttendanceStateOutOfSync(employeeId,inTime);
            sendResult(Constants.TRIGGER_ACTION_EXIT, Constants.RET_NOK,
                    Constants.ERROR_NO_CONNECTION);

            return;
        }


        try {
            OkHttpClient client = new OkHttpClient();
            Request request = buildLogExitRequest(employeeId,inTime,outTime);

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JSONObject json = new JSONObject(responseData);
                int status = json.optInt("status", Constants.RET_NOK);

                if(status == Constants.RET_OK){
                    setAttendanceStateInSync(employeeId,inTime);
                    sendResult(Constants.TRIGGER_ACTION_EXIT,Constants.RET_OK);

                }else{
                    String reason = json.optString("reason");
                    sendResult(Constants.TRIGGER_ACTION_EXIT, Constants.RET_NOK,
                            reason);
                    response.body().close();
                    return;

                }
            } else {
                String reason = response.message();
                setAttendanceStateOutOfSync(employeeId,inTime);
                sendResult(Constants.TRIGGER_ACTION_EXIT, Constants.RET_NOK,
                        reason);
                response.body().close();
                return;

            }
        }catch(JSONException ioe) {
            setAttendanceStateOutOfSync(employeeId,inTime);
            sendResult(Constants.TRIGGER_ACTION_EXIT, Constants.RET_NOK,
                    Constants.ERROR_INVALID_RESPONSE);

        } catch (java.io.IOException ioe) {
            setAttendanceStateOutOfSync(employeeId,inTime);
            sendResult(Constants.TRIGGER_ACTION_EXIT, Constants.RET_NOK,
                    Constants.ERROR_NO_CONNECTION);

        }catch (Exception e){
            setAttendanceStateOutOfSync(employeeId,inTime);
            sendResult(Constants.TRIGGER_ACTION_ENTRY, Constants.RET_NOK,
                    Constants.ERROR_NO_CONNECTION);

        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPushTimerExpired() {

        if (!Helper.isConnected(getApplicationContext())) {
            //set the state of the entry object to NOT in sync
            startPushTimer(Constants.PUSH_TIMER_EXPIRY);
            return;
        }

        List<Attendance> attendances = Attendance.find(Attendance.class,"is_synced_with_server = ?",
                                                                                               "0");

        if(attendances.isEmpty()){
            return;
        }

        for(Iterator<Attendance> attendanceIterator = attendances.listIterator() ;
                 attendanceIterator.hasNext();){
            Attendance record = attendanceIterator.next();

            switch(record.getStatus()){
                case Constants.ATTENDANCE_STATUS_IN:
                    handleActionLogEntry(record.getEmployeeId(),record.getInTime());
                    break;
                case Constants.ATTENDANCE_STATUS_OUT:
                    handleActionLogExit(record.getEmployeeId(),record.getInTime(),record.getOutTime());
            }
        }
    }


    /**********************************************************************************************
     *
     *                       Helper Functions
     *
     *********************************************************************************************/

    private void setAttendanceStateInSync(int employeeId,long entryTime){
        List<Attendance> records = Attendance.find(Attendance.class, "employee_id = ? and in_time = ?"
                                                  ,Integer.toString(employeeId), Long.toString(entryTime));

       if(records.isEmpty()){
           return;
       }

       Attendance record = records.get(0);

       record.setIsSyncedWithServer(true);
       record.save();
    }

    private void setAttendanceStateOutOfSync(int employeeId,long entryTime){
        List<Attendance> records = Attendance.find(Attendance.class, "employee_id = ? and in_time = ?",
                Integer.toString(employeeId), Long.toString(entryTime));

        if(records.isEmpty()){
            return;
        }

        Attendance record = records.get(0);

        record.setIsSyncedWithServer(false);
        record.save();
    }

    public void startPushTimer(long milliseconds){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
               ServerUpdationIntentService.startActionPushTimerExpiry(getApplicationContext());
            }
        }, milliseconds);

    }

    /**********************************************************************************************
     *
     *                       Builder Functions
     *
     *********************************************************************************************/

      private Request buildLogEntryRequest(int employeeId,long inTime){


          List<Attendance> records = Attendance.find(Attendance.class, "employee_id = ? and in_time = ?",
                                              Integer.toString(employeeId),Long.toString(inTime));

          if(records.isEmpty()){
              return null;
          }

          Attendance record = records.get(0);

          String url = Constants.POST_ENTRY_URL + "/";
          url += employeeId + "/" + inTime;

          RequestBody requestBody = new MultipartBody.Builder()
                  .setType(MultipartBody.FORM)
                  .addFormDataPart("image","image",
                          RequestBody.create(MultipartBody.FORM, new File(record.getInTimeImagePath())))
                  .build();

          return (new Request.Builder()
                  .url(url)
                  .post(requestBody)
                  .build());

      }

    private Request buildLogExitRequest(int employeeId,long inTime,long outTime){


        List<Attendance> records = Attendance.find(Attendance.class, "employee_id = ? && status = ? and in_time = ?",
                         Integer.toString(employeeId),
                         Integer.toString(Constants.ATTENDANCE_STATUS_IN),
                         Long.toString(inTime));

        if(records.isEmpty()){
            return null;
        }

        Attendance record = records.get(0);

        String url = Constants.POST_EXIT_URL + "/";
        url += employeeId + "/" + inTime + "/" + outTime;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("inImage","inImage",
                        RequestBody.create(MultipartBody.FORM, new File(record.getInTimeImagePath())))
                .addFormDataPart("outImage","outImage",
                        RequestBody.create(MultipartBody.FORM, new File(record.getOutTimeImagePath())))
                .build();

        return (new Request.Builder()
                .url(url)
                .post(requestBody)
                .build());

    }

    /**********************************************************************************************
     *
     *                       Result Functions
     *
     *********************************************************************************************/

    private Intent prepareResult(int triggerActionName, int result) {
            /*
          * Creates a new Intent containing a Uri object
          * BROADCAST_ACTION is a custom Intent action
          */
        Intent localIntent =
                new Intent(Constants.SERVER_UPDATION_BROADCAST_RESULTS_ACTION);

        localIntent.putExtra(Constants.SERVER_UPDATION_BROADCAST_RESULTS_STATUS, result);

        localIntent.putExtra(Constants.TRIGGER_ACTION_NAME, triggerActionName);


        return localIntent;
    }


    private void sendResult(Intent intent) {
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    private void sendResult(int triggerActionName, int result) {
        sendResult(triggerActionName, result, "");

    }


    private void sendResult(int triggerActionName, int result, String error) {
            /*
          * Creates a new Intent containing a Uri object
          * BROADCAST_ACTION is a custom Intent action
          */
        Intent localIntent =
                new Intent(Constants.SERVER_UPDATION_BROADCAST_RESULTS_ACTION);

        localIntent.putExtra(Constants.SERVER_UPDATION_BROADCAST_RESULTS_STATUS, result);

        localIntent.putExtra(Constants.TRIGGER_ACTION_NAME, triggerActionName);


        localIntent.putExtra(Constants.ERROR, error);


        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }



}
