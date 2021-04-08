package com.example.findmytag;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.app.Activity.RESULT_OK;
import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

public class imgPopupWindowTest {

    @Rule
    public IntentsTestRule<imgPopupWindow> mLoginActivityActivityTestRule = new IntentsTestRule<>(imgPopupWindow.class);
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule .grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void uploadBtn1() throws InterruptedException {
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        Instrumentation.ActivityResult activityResult = galleryPick();

        intending(expectedIntent).respondWith(activityResult);

        onView(withId(R.id.uploadPopup_upload_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn)).perform(click());

        intended(expectedIntent);
    }

    @Test
    public void uploadBtn2() throws InterruptedException {
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        Instrumentation.ActivityResult activityResult = galleryPick();

        intending(expectedIntent).respondWith(activityResult);

        //scroll to imageview so can see afterwards
        onView(withId(R.id.uploadPopup_upload_imgView2)).perform(scrollTo());
        onView(withId(R.id.uploadPopup_upload_btn2)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn2)).perform(click());

        intended(expectedIntent);
    }

    private Instrumentation.ActivityResult galleryPick(){
        Resources resources = ApplicationProvider.getApplicationContext().getResources();
        Uri imgUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        +resources.getResourcePackageName(R.drawable.ic_launcher_background)+"/"
                        +resources.getResourceTypeName(R.drawable.ic_launcher_background)+"/"
                        +resources.getResourceEntryName(R.drawable.ic_launcher_background)
        );

        Intent resultIntent = new Intent();
        resultIntent.setData(imgUri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK,resultIntent);
    }
}