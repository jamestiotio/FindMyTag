package com.example.findmytag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.findmytag.MappingFragment;
import com.example.findmytag.wifi.WiFiActivity;
import com.example.findmytag.MainActivity;
import com.example.findmytag.wifi.WiFiDataManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class WifiDataManagerTest {
    @Rule
    public ActivityScenarioRule<MainActivity> wifiTestRule = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testScanWifi() throws InterruptedException {
        final WiFiDataManager[] wiFiDataManager = new WiFiDataManager[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                wiFiDataManager[0] = new WiFiDataManager(activity);
                wiFiDataManager[0].scanWifi();
            }
        });
        Thread.sleep(5000);
        assertTrue(wiFiDataManager[0].getsorteddata().isEmpty());
        assertTrue(!wiFiDataManager[0].gets().isEmpty());
    }

    @Test
    public void testScanWifiSwitchActivity() throws InterruptedException {
        final WiFiDataManager[] wiFiDataManager = new WiFiDataManager[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                wiFiDataManager[0] = new WiFiDataManager(activity);
                wiFiDataManager[0].scanWifi();
                activity.startActivity(new Intent(activity, Login.class));
            }
        });
        Thread.sleep(5000);
        assertTrue(wiFiDataManager[0].getsorteddata().isEmpty());
        assertTrue(!wiFiDataManager[0].gets().isEmpty());
    }
    @Test
    public void testScanWifiInAnotherPage() throws InterruptedException {
        final WiFiDataManager[] wiFiDataManager = new WiFiDataManager[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                wiFiDataManager[0] = new WiFiDataManager(activity);
                wiFiDataManager[0].scanWifi();
                activity.startActivity(new Intent(activity, LocationActivity.class));
            }
        });
        Thread.sleep(5000);
        assertTrue(wiFiDataManager[0].getsorteddata().isEmpty());
        assertTrue(!wiFiDataManager[0].gets().isEmpty());
    }
    @Test
    public void testScanWifiDisabled() throws InterruptedException {
        final WiFiDataManager[] wiFiDataManager = new WiFiDataManager[1];
        ActivityScenario scenario = wifiTestRule.getScenario();
        scenario.onActivity(new ActivityScenario.ActivityAction() {
            @Override
            public void perform(Activity activity) {
                WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
                wiFiDataManager[0] = new WiFiDataManager(activity);
                wiFiDataManager[0].scanWifi();
            }
        });
        Thread.sleep(5000);
        assertTrue(wiFiDataManager[0].getsorteddata().isEmpty());
        assertTrue(wiFiDataManager[0].gets().isEmpty());
    }

}
