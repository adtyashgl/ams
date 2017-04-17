package adiractech.ams.setup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import adiractech.ams.R;
import adiractech.ams.adapters.CustomFranchiseListAdapter;
import adiractech.ams.listelements.FranchiseElement;
import adiractech.ams.main.MainActivity;
import adiractech.ams.tables.Employee;
import adiractech.ams.utils.Cache;
import adiractech.ams.utils.Constants;
import adiractech.ams.utils.Helper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FranchiseSelectionActivity extends AppCompatActivity {

    private int     userId;
    private Context context;
    private View    loadingView;
    private ListView franchiseDetailView;
    private GetFranchisesTask getFranchisesTask;
    private ArrayList<FranchiseElement> elements;
    private CustomFranchiseListAdapter customFranchiseListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise_selection);
        context = getApplicationContext();
        userId = (Integer) Cache.getFromCache(context, Constants.CACHE_PREFIX_USER_ID,
                                              Constants.TYPE_INT);

        loadingView = findViewById(R.id.afs_franchise_details_loading);
        franchiseDetailView = (ListView)findViewById(R.id.afs_franchise_details);

        context = getApplicationContext();
        elements = new ArrayList<FranchiseElement>();

        franchiseDetailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int franchiseId = (int)view.getTag();
                getEmployees(franchiseId);

            }
        });

        showProgress(true);
        getFranchisesTask = new GetFranchisesTask(userId);
        getFranchisesTask.execute();
    }


    private void showProgress(boolean show){
        Helper.showProgress(context,show,loadingView,franchiseDetailView);
    }

    private void getEmployees(int franchiseId){
        showProgress(true);
        new GetEmployeesTask(franchiseId).execute();
    }


    private void inflateFranchiseList(JSONArray franchises,int count){

        try {

            for(int i = 0; i < count; i++){
                JSONObject franchise = franchises.getJSONObject(i).getJSONObject(Constants.FRANCHISE);
                elements.add(new FranchiseElement(franchise.getString(Constants.FRANCHISE_NAME),
                                                  franchise.getString(Constants.FRANCHISE_ADDRESS),
                                                  franchise.getInt(Constants.FRANCHISE_ID)));
            }
            customFranchiseListAdapter = new CustomFranchiseListAdapter(this,elements);
            franchiseDetailView.setAdapter(customFranchiseListAdapter);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    /**
     * Represents an asynchronous GetSpaces Task used for choosing the space
     * this app will be setup for.
     */
    public class GetFranchisesTask extends AsyncTask<Void, Void, Boolean> {

        private int userId;
        private String failureReason;
        private OkHttpClient client;
        private JSONArray franchises;
        private int numFranchises = 0;

        GetFranchisesTask(int userId) {
            this.userId = userId;
            client = new OkHttpClient();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Request request = buildGetFranchiseRequest(userId);

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("RESPONSE", "SPACE DETAILS FOUND");

                    String responseStr = response.body().string();
                    JSONObject json = new JSONObject(responseStr);
                    int status = json.getInt("status");

                    if (status == Constants.RET_OK) {
                        numFranchises = json.getInt("count");

                        if (numFranchises == 0) {
                            failureReason = "No Franchises Managed by this User." +
                                                 "Please contact Administrator";
                            return false;
                        }
                        franchises = json.getJSONArray("locations");
                        response.body().close();
                        return true;
                    } else {
                        String reason = json.getString("reason");
                        failureReason = "Invalid response received(" + reason + ")." +
                                         " Please contact Administrator";
                        response.body().close();
                        return false;

                    }

                } else {
                    failureReason = "Invalid response received. Please contact Administrator";

                    response.body().close();
                    return false;
                }

            } catch (IOException | JSONException e) {
                failureReason = "Unknown error. Please try after sometime" + e.toString();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getFranchisesTask = null;
            showProgress(false);

            if (success) {
               inflateFranchiseList(franchises,numFranchises);
            } else {
                Helper.displayNotification(getApplicationContext(),failureReason,true);

            }
        }

        @Override
        protected void onCancelled() {
            getFranchisesTask = null;
            showProgress(false);
        }

        private Request buildGetFranchiseRequest(int userId) throws JSONException {
            String url = Constants.GET_FRANCHISE_URL + "/" + userId;
            return (new Request.Builder()
                    .url(url)
                    .build());


        }
    }

    public class GetEmployeesTask extends AsyncTask<Void,Void, Boolean>{

        private int franchiseId;
        private int numEmployees = 0;
        private OkHttpClient client;
        private String failureReason;
        private JSONArray employees;

        public GetEmployeesTask(int franchiseId){
            this.franchiseId = franchiseId;
            this.client = new OkHttpClient();
            failureReason = "";

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Request request = buildGetEmployeesRequest(franchiseId);

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("RESPONSE", "SPACE DETAILS FOUND");

                    String responseStr = response.body().string();
                    JSONObject json = new JSONObject(responseStr);
                    int status = json.getInt("status");

                    if (status == Constants.RET_OK) {
                        numEmployees = json.getInt("count");

                        if (numEmployees == 0) {
                            failureReason = "No Franchises Managed by this User." +
                                    "Please contact Administrator";
                            return false;
                        }
                        employees = json.getJSONArray("employees");
                        response.body().close();

                        storeEmployees(employees,numEmployees);
                        return true;
                    } else {
                        String reason = json.getString("reason");
                        failureReason = "Invalid response received(" + reason + ")." +
                                " Please contact Administrator";
                        response.body().close();
                        return false;

                    }

                } else {
                    failureReason = "Invalid response received. Please contact Administrator";

                    response.body().close();
                    return false;
                }

            } catch (IOException | JSONException e) {
                failureReason = "Unknown error. Please try after sometime" + e.toString();
                return false;
            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getFranchisesTask = null;
            showProgress(false);

            if (success) {
                Cache.setInCache(context,Constants.CACHE_PREFIX_IS_SETUP_COMPLETE,true,
                                  Constants.TYPE_BOOL);
                Intent intent = new Intent(FranchiseSelectionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Helper.displayNotification(getApplicationContext(),failureReason,true);

            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

        private Request buildGetEmployeesRequest(int franchiseId) throws JSONException {
            String url = Constants.GET_EMPLOYEES_URL + "/" + franchiseId;
            return (new Request.Builder()
                    .url(url)
                    .build());


        }

        private void storeEmployees(JSONArray employees,int count) throws JSONException{

            for(int i = 0; i < count; i++){
                JSONObject franchise = employees.getJSONObject(i).getJSONObject(Constants.EMPLOYEE);
                Employee employee = new Employee();
                employee.setEmployeeId(franchise.getInt(Constants.EMPLOYEE_ID));
                employee.setFirstName(franchise.getString(Constants.EMPLOYEE_FIRST_NAME));
                employee.setMiddleName(franchise.optString(Constants.EMPLOYEE_MIDDLE_NAME,""));
                employee.setLastName(franchise.optString(Constants.EMPLOYEE_LAST_NAME,""));
                employee.setManagerId(franchise.getInt(Constants.EMPLOYEE_MANAGER_ID));
                employee.setRoleId(franchise.getInt(Constants.EMPLOYEE_ROLE_ID));
                employee.setSecret(franchise.getString(Constants.EMPLOYEE_SECRET));

                employee.save();

            }
        }

    }
}
