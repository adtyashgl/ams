package adiractech.ams.setup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import adiractech.ams.R;
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

        if(!isLoggedIn()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }else if(!isSetupComplete()){
            Intent intent = new Intent(this, FranchiseSelectionActivity.class);
            startActivity(intent);
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
}
