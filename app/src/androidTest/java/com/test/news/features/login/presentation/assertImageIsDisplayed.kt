package com.test.news.features.login.presentation


import androidx.test.espresso.DataInteraction
import androidx.test.espresso.ViewInteraction
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
//import androidx.test.espresso.c .RecyclerViewActions.actionOnItemAtPosition
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*

import com.test.news.R

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`

@LargeTest
@RunWith(AndroidJUnit4::class)
class assertImageIsDisplayed {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginActivityTest() {
        val appCompatEditText = onView(
allOf(withId(R.id.editTextUserName),
childAtPosition(
childAtPosition(
withId(R.id.inputLayoutUserName),
0),
0)))
        appCompatEditText.perform(scrollTo(), replaceText("user1"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
allOf(withId(R.id.editTextPassword),
childAtPosition(
childAtPosition(
withId(R.id.inputLayoutPassword),
0),
0)))
        appCompatEditText2.perform(scrollTo(), replaceText("password"), closeSoftKeyboard())

        val appCompatButton = onView(
allOf(withId(R.id.buttonLogin), withText("Login"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
3)))
        appCompatButton.perform(scrollTo(), click())
        Thread.sleep(6000);

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))

        }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

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
