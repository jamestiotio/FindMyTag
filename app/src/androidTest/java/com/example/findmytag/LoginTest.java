package com.example.findmytag;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class LoginTest {

    @Rule
    public ActivityScenarioRule<Login> mSigninTestRul = new ActivityScenarioRule<>(Login.class);
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String correctEmail = "Darren@gmail.com";
    private String correctPassword = "123456";

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.Email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void emptyEmailANDPassword() {
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.Email)).check(matches(hasErrorText("Email is Required.")));
    }

    @Test
    public void emptyEmail() {
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText(correctPassword), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.Email)).check(matches(hasErrorText("Email is Required.")));
    }

    @Test
    public void emptyPassword() {
        onView(withId(R.id.Email)).perform(clearText());
        onView(withId(R.id.Email)).perform(typeText(correctEmail), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Password is Required.")));
    }

    //password length less than 6 gives error
    @Test
    public void PasswordLength() {
        onView(withId(R.id.Email)).perform(clearText());
        onView(withId(R.id.Email)).perform(typeText(correctEmail));
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Password Must be >= 6 " +
                "Characters")));
    }

    @Test
    public void LoginSuccess() throws InterruptedException {
        onView(withId(R.id.Email)).perform(clearText());
        onView(withId(R.id.Email)).perform(typeText(correctEmail));
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText(correctPassword), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        //sleep to allow firebase to load
        Thread.sleep(2000);
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()));
    }

}