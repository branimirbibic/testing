package com.test.news.features.login.presentation


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.test.news.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Intent
import android.provider.Settings
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import android.net.wifi.WifiManager
import androidx.test.core.app.ApplicationProvider


@LargeTest
@RunWith(AndroidJUnit4::class)
class NoNetworkTest {

    @Rule
    @JvmField
    var mActivityTestRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun noNetworkTest() {

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable")

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editTextUserName),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutUserName),
                        0
                    ),
                    0
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("user1"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editTextPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.inputLayoutPassword),
                        0
                    ),
                    0
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("password"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.buttonLogin), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())


        val textView = onView(
            allOf(
                withText("News"),
                withParent(
                    allOf(
                        withId(R.id.action_bar),
                        withParent(withId(R.id.action_bar_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("News")))

        val textView2 = onView(
            allOf(
                withId(R.id.textViewError), withText("Failed to load news"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Failed to load news")))

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable")
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
