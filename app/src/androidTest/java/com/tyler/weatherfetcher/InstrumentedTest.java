package com.tyler.weatherfetcher;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityTestRule = new ActivityTestRule<SignInActivity>(SignInActivity.class);

    private SignInActivity mSignInActivity = null;

    @Before
    public void setUp() throws Exception {

        mSignInActivity = mActivityTestRule.getActivity();

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tyler.weatherfetcher", appContext.getPackageName());
    }

    @Test
    public void testLaunch() {

        View view = mSignInActivity.findViewById(R.id.sign_in_button);

        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {

        mSignInActivity = null;

    }
}