package adiractech.ams.utils;

import android.os.Environment;

import java.util.TimeZone;

/**
 * Created by root on 13/04/17.
 */

public class Constants {
    public static final String APP_VERSION = "0.0.1";
    public static final String BASE_URL = "https://www.simplypark.in/ams";
    //public static final String BASE_URL = "http://192.168.0.24/ams";
    //public static final String BASE_URL = "http://172.20.10.6/ams";
    public static final String LOGIN_URL = BASE_URL + "/users/appLogin";
    public static final String GET_FRANCHISE_URL = BASE_URL + "/franchises";
    public static final String GET_EMPLOYEES_URL = BASE_URL + "/employees";
    public static final String POST_ENTRY_URL = BASE_URL + "/entry";
    public static final String POST_EXIT_URL = BASE_URL + "/exit";


    /***********************************************************************************************
     *
     *               CACHE RELATED PARAMETERS
     *
     *
     **********************************************************************************************/

    //cache (implemented as Shared Preferences)


    public static final int TYPE_INT = 0;
    public static final int TYPE_BOOL = 1;
    public static final int TYPE_STRING = 2;
    public static final int TYPE_DOUBLE = 3;
    public static final int TYPE_LONG = 4;

    public static final String CACHE_NAME = "adiractech.ams";

    public static final String CACHE_PREFIX_USER_ID = CACHE_NAME+"userid";
    public static final String CACHE_PREFIX_USERNAME = CACHE_NAME+"username";
    public static final String CACHE_PREFIX_PASSWORD = CACHE_NAME+"password";
    public static final String CACHE_PREFIX_IS_LOGGED_IN = CACHE_NAME+"isLoggedIn";
    public static final String CACHE_PREFIX_IS_SETUP_COMPLETE = CACHE_NAME + "isSetupComplete";



    /**********************************************************************************************
     *          RET VALUES
     *
     **********************************************************************************************/

    public static final int RET_OK = 0;
    public static final int RET_NOK = 1;
    public static final int INVALID_TIME = 0;
    public static final int EMPLOYEE_ID_INVALID = 0;

    public static final String ERROR_NO_CONNECTION = "Update Failed. No network Connectivity";
    public static final String ERROR_INVALID_RESPONSE = "Update Failed.Invalid Response from Server";


    /**********************************************************************************************
     *             JSON Objects
     *********************************************************************************************/
    public static final String FRANCHISE = "Franchise";
    public static final String FRANCHISE_NAME = "name";
    public static final String FRANCHISE_ADDRESS = "address";
    public static final String FRANCHISE_ID = "id";

    public static final String EMPLOYEE = "Employee";
    public static final String EMPLOYEE_ID = "id";
    public static final String EMPLOYEE_FIRST_NAME = "first_name";
    public static final String EMPLOYEE_MIDDLE_NAME = "middle_name";
    public static final String EMPLOYEE_LAST_NAME = "last_name";
    public static final String EMPLOYEE_SECRET = "secret";
    public static final String EMPLOYEE_MANAGER_ID = "manager_id";
    public static final String EMPLOYEE_ROLE_ID = "role_id";
    public static final String EMPLOYEE_FRANCHISE_ID = "franchise_id";


    /***********************************************************************************************
     *    BUNDLE
     *
     **********************************************************************************************/

    public static final String BUNDLE_EMPLOYEE_ID = "adiractech.ams.employeeid";
    public static final String BUNDLE_IN_TIME = "adiractech.ams.intime";
    public static final String BUNDLE_OUT_TIME = "adiractech.ams.outtime";
    public static final String BUNDLE_ACTION = "adiractech.ams.action";
    public static final String BUNDLE_ERROR = "adiractech.ams.error";
    public static final String BUNDLE_FILE_NAME = "adiractech.ams.filename";
    public static final String BUNDLE_TIMER_ACTION ="adiractech.ams.TIMER_ACTION";
    public static final String BUNDLE_RESULT = "adiractech.ams.RESULT";
    public static final String BUNDLE_NOTIFICATION = "adiractech.ams.NOTIFICATION";


    /***********************************************************************************************
     *         ACTION
     **********************************************************************************************/
    public static final int ATTENDANCE_ACTION_NA = 0;
    public static final int ATTENDANCE_ACTION_ENTER = 1;
    public static final int ATTENDANCE_ACTION_EXIT = 2;



    public static final String SERVER_ACTION_ENTER = "adiractech.ams.action.ENTER";
    public static final String SERVER_ACTION_EXIT = "adiractech.ams.action.EXIT";
    public static final String SERVER_ACTION_PUSH_TIMER_EXPIRY = "adiractech.ams.action.TIMER_EXPIRY";

    // Defines a custom Intent action
    public static final String SERVER_UPDATION_BROADCAST_RESULTS_ACTION =
            "adiractech.ams.SERVER_UPDATION_BROADCAST_RESULTS";
    public static final String SERVER_UPDATION_BROADCAST_RESULTS_STATUS =
            "adiractech.ams.SERVER_UPDATION_BROADCAST_RESULTS_STATUS";

    public static final String TRIGGER_ACTION_NAME = "adiractech.ams.trigger.ACTION";
    public static final String TIMER_ACTION = "adiractech.ams.TIMER_EXPIRY";
    public static final String ERROR = "adiractech.ams.ERROR";


    public static final int TRIGGER_ACTION_NONE = 0;
    public static final int TRIGGER_ACTION_ENTRY = 3;
    public static final int TRIGGER_ACTION_EXIT = 4;



    public static final int TIMER_ACTIONS_MAX = 2;
    public static final long TIMER_INVALID_TIME = -1;
    public static final int TIMER_STATUS_RUNNING = 1;
    public static final int TIMER_STATUS_NOT_RUNNING = 0;




    public static final int TIMER_INVALID_ACTION = -1;


    /***********************************************************************************************
     *         MISC
     **********************************************************************************************/
    //play services related constants
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("America/New_York");
    public static final int ATTENDANCE_STATUS_NA = 0;
    public static final int ATTENDANCE_STATUS_IN = 1;
    public static final int ATTENDANCE_STATUS_OUT = 2;
    public static final int COUNTDOWN_TIMER_VALUE_IN_MS = 5000;

    public static final String DEFAULT_SAVE_DIRECTORY =
            Environment.getExternalStorageDirectory()+"/employees";


    public static final int PUSH_TIMER_EXPIRY = 120000;




}
