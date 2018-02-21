package com.coderobot.portalliteapiproject;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.coderobot.portalliteapi.PortalLiteApi;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PortalLiteApi.MyBinder mBinder = null;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            log("onServiceConnected");
            mBinder = (PortalLiteApi.MyBinder) service;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
        bindService(new Intent(this, PortalLiteApi.class), mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
        unbindService(mConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mBinder.test();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

}
