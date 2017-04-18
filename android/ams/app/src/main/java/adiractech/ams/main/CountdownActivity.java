package adiractech.ams.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import adiractech.ams.R;
import adiractech.ams.tables.Employee;
import adiractech.ams.utils.Constants;
import adiractech.ams.utils.Helper;

public class CountdownActivity extends AppCompatActivity {

    private int employeeId;
    private Bundle bundle;
    private Context context;
    private Employee employee;
    private View progressView;
    private View contentView;

    private TextView employeeName;

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

        showProgress(true);

        new EmployeeFinderTask(employeeId).execute((Void)null);
    }


    private void showProgress(boolean show){
        Helper.showProgress(context,show,progressView,contentView);
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class EmployeeFinderTask extends AsyncTask<Void, Void, Boolean> {

        int employeeId;


        EmployeeFinderTask(int employeeId) {
            this.employeeId = employeeId;

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
            showProgress(false);
            if (success) {

            } else {
                Helper.displayNotification(context, "Invalid Card Presented..\n" +
                        "Please try again", true);
                Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}

