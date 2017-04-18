package adiractech.ams.utils;

/**
 * Created by root on 13/04/17.
 */

public class Constants {
    public static final String APP_VERSION = "0.0.1";
    public static final String BASE_URL = "http://192.168.0.24/ams";
    public static final String LOGIN_URL = BASE_URL + "/users/appLogin";
    public static final String GET_FRANCHISE_URL = BASE_URL + "/franchises";
    public static final String GET_EMPLOYEES_URL = BASE_URL + "/employees";


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

    public static final String BUNDLE_EMPLOYEE_ID = "adiratech.ams.employeeid";

    /***********************************************************************************************
     *         MISC
     **********************************************************************************************/
    //play services related constants
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;




}
