package adiractech.ams.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import adiractech.ams.R;
import adiractech.ams.tables.Attendance;
import adiractech.ams.tables.Employee;
import adiractech.ams.utils.Constants;
import adiractech.ams.utils.Helper;
import hirondelle.date4j.DateTime;

public class CountdownActivity extends AppCompatActivity {

    private int employeeId;
    private Bundle bundle;
    private Context context;
    private Employee employee;
    private View progressView;
    private View contentView;
    private int action;
    private long currentDateTime;
    private long inDateTime;




    private TextView employeeNameView;
    private TextView entryTimeView;
    private TextView exitTimeView;
    private TextView countDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        context = getApplicationContext();


        progressView = findViewById(R.id.acd_progress_id);
        contentView  = findViewById(R.id.acd_content_view_id);

        //read bundle params from printer test activity
        bundle = getIntent().getExtras();

        employeeId = bundle.getInt(Constants.BUNDLE_EMPLOYEE_ID);
        action = bundle.getInt(Constants.BUNDLE_ACTION);

        employeeNameView = (TextView)findViewById(R.id.acd_employee_name_id);
        entryTimeView = (TextView)findViewById(R.id.acd_entry_time_id);
        exitTimeView  = (TextView)findViewById(R.id.acd_exit_time_id);
        countDownView = (TextView)findViewById(R.id.acd_countdown_id);


        showProgress(true);

        new EmployeeFinderTask().execute((Void)null);
    }


    private void showProgress(boolean show){
        Helper.showProgress(context,show,progressView,contentView);
    }

    private void startCountDown(int value){
        new CountDownTimer(value, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownView.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                countDownView.setText("0");
                Intent intent = new Intent(CountdownActivity.this, FaceActivity.class);
                intent.putExtra(Constants.BUNDLE_ACTION,action);
                intent.putExtra(Constants.BUNDLE_EMPLOYEE_ID,employeeId);
                intent.putExtra(Constants.BUNDLE_IN_TIME,inDateTime);
                intent.putExtra(Constants.BUNDLE_FILE_NAME,Long.toString(currentDateTime));
                startActivity(intent);
                finish();

            }
        }.start();

    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class EmployeeFinderTask extends AsyncTask<Void, Void, Boolean> {


        EmployeeFinderTask() {


        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<Employee> employees = Employee.find(Employee.class,
                    "employee_id = ?", Integer.toString(employeeId));

            if (employees.isEmpty()) {
                return false;
            }
            employee = employees.get(0);
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                String name = employee.getFirstName() + " ";

                if(!employee.getLastName().isEmpty()){
                    name += employee.getLastName();
                }

                employeeNameView.setText(name);
                DateTime now = DateTime.now(Constants.TIME_ZONE);
                currentDateTime = now.getMilliseconds(Constants.TIME_ZONE);

                String currentDateTimeStr = now.format("YYYY-MM-DD hh:mm");

                Attendance record = employee.getAttendanceRecord();

                switch(action){
                    case Constants.ATTENDANCE_ACTION_ENTER:{
                        if(record.getStatus() == Constants.ATTENDANCE_ACTION_ENTER){
                            String lastEntryStr = DateTime.forInstant(record.getInTime(),Constants.TIME_ZONE).format("YYYY-MM-DD hh:mm");
                            String displayString = "An entry already exists for " + name +
                                                   ". Last Entry at " + lastEntryStr +
                                                   ". Please exit first or contact Store Manager";
                            Helper.displayNotification(context, displayString, true);
                            Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        String entryString = getString(R.string.entry_string) + " " + currentDateTimeStr;
                        entryTimeView.setText(entryString);
                        exitTimeView.setVisibility(View.GONE);
                        inDateTime = currentDateTime;
                        record.setEmployeeId(employeeId);
                        record.setInTime(currentDateTime);
                        record.setStatus(Constants.ATTENDANCE_STATUS_IN);
                        record.save();

                    }
                    break;

                    case Constants.ATTENDANCE_ACTION_EXIT:{

                        if(record.getInTime() == Constants.INVALID_TIME){
                            Helper.displayNotification(context, "Invalid Entry Time set\n" +
                                    "Please contact the Store Manager", true);
                            Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }

                        if(record.getStatus() == Constants.ATTENDANCE_ACTION_EXIT){
                            Helper.displayNotification(context,
                              "No entry record exist. Please go back and record your entry first",
                                    true);
                            Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                        record.setOutTime(currentDateTime);
                        String exitString = getString(R.string.exit_string) + " " + currentDateTimeStr;
                        exitTimeView.setText(exitString);
                        String entryTimeStr = DateTime.forInstant(record.getInTime(),Constants.TIME_ZONE).
                                                format("YYYY-MM-DD hh:mm");
                        String entryString = getString(R.string.entry_string) + " " + entryTimeStr;
                        entryTimeView.setText(entryString);
                        record.setStatus(Constants.ATTENDANCE_STATUS_OUT);
                        record.save();
                        inDateTime = record.getInTime();

                    }
                    break;

                }

                showProgress(false);
                startCountDown(Constants.COUNTDOWN_TIMER_VALUE_IN_MS);

            } else {
                showProgress(false);
                Helper.displayNotification(context, "Invalid Card Presented..\n" +
                        "Please try again", true);
                Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}

