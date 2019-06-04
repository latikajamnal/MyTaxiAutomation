package com.mytaxi.android_demo.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.utils.EspressoIdlingResource;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.runner.RunWith;

import cucumber.api.java.Before;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity;

    @Before("@main-feature")
    public void setUp() throws Exception {
        // Before test case execution.
        mActivityRule.launchActivity(new Intent());
        mActivity = mActivityRule.getActivity();

        // Add idling to network calls.
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        // Grant location permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.ACCESS_FINE_LOCATION");
        }
    }

    @Given("^I am on main screen$")
    public void i_am_on_main_screen() throws Throwable {
        assertNotNull(mActivity);
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
    }

    @When("^I enter (.*) on search box$")
    public void i_enter_keyword_on_search_box(String keyword) throws Throwable {
        onView(withId(R.id.textSearch)).perform(typeText(keyword));
        closeSoftKeyboard();
    }

    @Then("^I should see drivers on autocomplete list$")
    public void i_should_see_drivers_on_autocomple_list() throws Throwable {
        // Check that suggestions are displayed.
        onView(withText("Sara Christensen"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withText("Sarah Scott"))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @After("@main-feature")
    public void tearDown() throws Exception {
        // After test case execution.
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
        mActivityRule.finishActivity();
    }
}
