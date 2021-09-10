package com.test.news

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.rule.ActivityTestRule
import com.test.news.features.login.presentation.LoginActivity
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.fail


class LoginInstrumentedTest {

    @get:Rule
    var activityTestRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Test
    fun shouldOpenLoginScreen() {
            onView(withId(R.id.editTextUserName)).check(matches(isDisplayed()));
            onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
    }

    @Test
    fun shouldLoginWithInvalidCredentials() {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(INVALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(INVALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        onView(withId(R.id.editTextUserName)).check(matches(hasErrorText(WRONG_USER_NAME)));
    }

    @Test
    fun shouldLoginWithValidCredentials() {
        validLogin()

        assertNewsActivity();
    }

    @Test
    fun shouldLOpenNewsActivityAfterInitialLogin() {
        validLogin()

        activityTestRule.finishActivity();
        activityTestRule.launchActivity(Intent())

        assertNewsActivity()
    }

    @Test
    fun shouldLoadNewsActivity() {
        validLogin()
        assertNewsActivity()
    }

    @Test
    fun noNetworkTest() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable")

        validLogin()

        assertNewsActivity()

        val textView2 = onView(
            Matchers.allOf(
                withId(R.id.textViewError), withText(FAILED_TO_LOAD_NEWS),
                withParent(withParent(withId(R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText(FAILED_TO_LOAD_NEWS)))

        //TODO implement assertion for retry button;

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable")

        //TODO remove after implementation of retry button check
        fail("no retry button")
    }

    fun assertNewsActivity() = run {
        val textView = onView(
            Matchers.allOf(
                withText(NEWS),
                withParent(
                    Matchers.allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText(NEWS)))
    }

    fun validLogin() = run {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
    }

    companion object {
        private const val VALID_USER_NAME = "user1"
        private const val VALID_USER_PASSWORD = "password"
        private const val INVALID_USER_NAME = "invalidUser"
        private const val INVALID_USER_PASSWORD = "invalidPassword"
        private const val WRONG_USER_NAME="Wrong user name"
        private const val FAILED_TO_LOAD_NEWS="Failed to load news"
        private const val NEWS="News"

    }
}
