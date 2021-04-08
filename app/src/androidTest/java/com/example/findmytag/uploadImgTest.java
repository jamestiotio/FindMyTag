package com.example.findmytag;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

public class uploadImgTest {
    String locationInputError1 = "Please fill in Location 1 Name";
    String locationInputError2 = "Please fill in Location 2 Name";
    String imgError1 = "Please upload Location 1 Floor Plan";
    String imgError2 = "Please upload Location 2 Floor Plan";

    String location1INPUT = "Location 1";
    String location2INPUT = "Location 2";

    @Rule
    //public ActivityScenarioRule<LocationActivity> mLocationActivityRule = new ActivityScenarioRule<>(LocationActivity.class);
    public IntentsTestRule<LocationActivity> mLoginActivityActivityTestRule = new IntentsTestRule<>(LocationActivity.class);
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule .grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);
    //public IntentsTestRule<LocationActivity> mLoginActivityActivityTestRule =
     //       new IntentsTestRule<>(LocationActivity.class);
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
// testing upload function
    //testing no input
    @Test
    public void noInput() throws InterruptedException {
        //ActivityScenario locationActivity = ActivityScenario.launch(LocationActivity.class);
        // check if upload button exists and perform click
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_upload)).perform(click());

        // check if popup window displayed after click
        onView(withId(R.id.img_popup_window)).check(matches(isDisplayed()));
        //check if popup window confirm button displayed
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).perform(click());
        onView(withText(locationInputError1)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        //Thread.sleep(3000);
    }
    //testing with only location 1 text view input
    @Test
    public void inputLocation1txt() throws InterruptedException {
        //ActivityScenario locationActivity = ActivityScenario.launch(LocationActivity.class);
        // check if upload button exists and perform click
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_upload)).perform(click());

        // check if popup window displayed after click
        onView(withId(R.id.img_popup_window)).check(matches(isDisplayed()));

        //add locationName1 input
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(clearText());
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(typeText(location1INPUT));

        //check if popup window confirm button displayed
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).perform(click());
        onView(withText(imgError1)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        //Thread.sleep(3000);
    }
    //testing with location 1 text view input and img 1 uploaded
    @Test
    public void inputLocationimg1() throws InterruptedException {
        // check if upload button exists and perform click
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_upload)).perform(click());

        // check if popup window displayed after click
        onView(withId(R.id.img_popup_window)).check(matches(isDisplayed()));

        //add locationName1 input
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(clearText());
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(typeText(location1INPUT));

        //add location img
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        Instrumentation.ActivityResult activityResult = galleryPick();

        intending(expectedIntent).respondWith(activityResult);

        onView(withId(R.id.uploadPopup_upload_btn)).perform(scrollTo());
        onView(withId(R.id.uploadPopup_upload_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn)).perform(click());

        //check if popup window confirm button displayed
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).perform(click());
        onView(withText(locationInputError2)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        //Thread.sleep(3000);
    }


    //testing with location 1 and 2 text view input and img 1 uploaded
    @Test
    public void inputLocation2txt() throws InterruptedException {
        // check if upload button exists and perform click
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_upload)).perform(click());

        // check if popup window displayed after click
        onView(withId(R.id.img_popup_window)).check(matches(isDisplayed()));

        //add locationName1 input
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(clearText());
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(typeText(location1INPUT));

        //add location img
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        Instrumentation.ActivityResult activityResult = galleryPick();

        intending(expectedIntent).respondWith(activityResult);

        onView(withId(R.id.uploadPopup_upload_btn)).perform(scrollTo());
        onView(withId(R.id.uploadPopup_upload_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn)).perform(click());

        //add locationName2 input
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(scrollTo());
        onView(withId(R.id.uploadPopup_upload_locationName2)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(click());
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(clearText());
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(typeText(location2INPUT));

        //check if popup window confirm button displayed
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).perform(click());
        onView(withText(imgError2)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        //Thread.sleep(3000);
    }
    //testing with location 1 and 2 text view input and img 1 and 2 uploaded
    @Test
    public void inputLocationimg2() throws InterruptedException {
        // check if upload button exists and perform click
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_upload)).perform(click());

        // check if popup window displayed after click
        onView(withId(R.id.img_popup_window)).check(matches(isDisplayed()));

        //add locationName1 input
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(clearText());
        onView(withId(R.id.uploadPopup_upload_locationName)).perform(typeText(location1INPUT));

        //add location img
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        Instrumentation.ActivityResult activityResult = galleryPick();

        intending(expectedIntent).respondWith(activityResult);


        onView(withId(R.id.uploadPopup_upload_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn)).perform(click());

        //add locationName2 input
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(scrollTo());
        onView(withId(R.id.uploadPopup_upload_locationName2)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(click());
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(clearText());
        onView(withId(R.id.uploadPopup_upload_locationName2)).perform(typeText(location2INPUT));

        //add location img 2
        onView(withId(R.id.uploadPopup_upload_btn2)).perform(scrollTo());
        onView(withId(R.id.uploadPopup_upload_btn2)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn2)).perform(click());

        //check if popup window confirm button displayed
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadPopup_upload_btn_confirm)).perform(click());

        //check if layout returned to location activity
        onView(withId(R.id.locationActivity)).check(matches(isDisplayed()));

        //Thread.sleep(3000);
    }



    //image picking function
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