package com.example.findmytag;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public boolean matchesSafely(Root item) {
        int type = item.getWindowLayoutParams().get().type;
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            IBinder windowToken = item.getDecorView().getWindowToken();
            IBinder appToken = item.getDecorView().getApplicationWindowToken();
            if (windowToken == appToken) {
                //return true;
            }
        }


        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }
}
