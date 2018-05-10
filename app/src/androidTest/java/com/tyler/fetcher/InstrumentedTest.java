package com.tyler.fetcher;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<SignInActivity>(SignInActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mWeatherActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private SignInActivity mSignInActivity = null;
    private MainActivity mWeatherActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {

        mSignInActivity = mActivityTestRule.getActivity();
        mWeatherActivity = mWeatherActivityTestRule.getActivity();

    }

    @Test
    public void useAppContext() {
        //Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tyler.weatherfetcher", appContext.getPackageName());
    }

    @Test
    public void testLaunchOfEntryActivity() {

        assertNotNull(mSignInActivity.findViewById(R.id.sign_in_button));
    }

    @Test
    public void testLaunchOfSecondActivity() {

        assertNotNull(mWeatherActivity.findViewById(R.id.temp_tv));

    }

    @Test
    public void testLaunchOfSecondActivityWithButtonClick() {

        //View view = mSignInActivity.findViewById(R.id.sign_in_button);

        //onView(withId(R.id.sign_in_button)).perform(click());

        //Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        //assertNotNull(secondActivity);

    }

    @After
    public void tearDown() throws Exception {

        mSignInActivity = null;

    }
}