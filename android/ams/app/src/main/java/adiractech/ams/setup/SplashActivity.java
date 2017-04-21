package adiractech.ams.setup;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import adiractech.ams.R;
import adiractech.ams.main.CountdownActivity;
import adiractech.ams.main.FaceActivity;
import adiractech.ams.main.MainActivity;
import adiractech.ams.utils.Cache;
import adiractech.ams.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        context = getApplicationContext();

        if(checkPlayServices()) {
            if (!isLoggedIn()) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);


            } else if (!isSetupComplete()) {
                Intent intent = new Intent(this, FranchiseSelectionActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    public boolean isLoggedIn(){
        return (Boolean) Cache.getFromCache(context,Constants.CACHE_PREFIX_IS_LOGGED_IN,
                                            Constants.TYPE_BOOL);
    }

    public boolean isSetupComplete(){
        return (Boolean) Cache.getFromCache(context,Constants.CACHE_PREFIX_IS_SETUP_COMPLETE,
                Constants.TYPE_BOOL);

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.i("checkPlayServices", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void startCountDown(int value){
        new CountDownTimer(value, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }.start();

    }


}
