package adiractech.ams.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import adiractech.ams.R;
import adiractech.ams.utils.Constants;
import adiractech.ams.utils.Helper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
   private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        String notification = getIntent().getStringExtra(Constants.BUNDLE_NOTIFICATION);

        if(notification != null && !notification.isEmpty()){
            Helper.displayNotification(context,notification,true);
        }

    }

    public void entryButtonHandler(View view){
        Intent intent = new Intent(this,QrActivity.class);
        intent.putExtra(Constants.BUNDLE_ACTION, Constants.ATTENDANCE_ACTION_ENTER);
        startActivity(intent);

    }

    public void exitButtonHandler(View view){
        Intent intent = new Intent(this,QrActivity.class);
        intent.putExtra(Constants.BUNDLE_ACTION, Constants.ATTENDANCE_ACTION_EXIT);
        startActivity(intent);

    }
}
