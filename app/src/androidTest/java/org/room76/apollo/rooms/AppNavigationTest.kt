package org.room76.apollo.rooms

import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.widget.DrawerLayout
import android.view.Gravity

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.room76.apollo.R

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions.open
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.contrib.DrawerMatchers.isOpen
import android.support.test.espresso.contrib.NavigationViewActions.navigateTo
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

/**
 * Tests for the [DrawerLayout] layout component in [RoomsActivity] which manages
 * navigation within the app.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {

    /**
     * [ActivityTestRule] is a JUnit [@Rule][Rule] to launch your activity under test.
     *
     *
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    var mActivityTestRule = ActivityTestRule(RoomsActivity::class.java)

    @Test
    fun clickOnStatisticsNavigationItem_ShowsStatisticsScreen() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start statistics screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.nav_view))

        // Check that statistics Activity was opened.
        val expectedNoStatisticsText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.no_statistics_available)
        //        onView(withId(R.id.no_statistics)).check(matches(withText(expectedNoStatisticsText)));
    }

    @Test
    fun clickOnAndroidHomeIcon_OpensNavigation() {
        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.

        // Open Drawer
        val navigateUpDesc = mActivityTestRule.activity
                .getString(android.support.v7.appcompat.R.string.abc_action_bar_up_description)
        onView(withContentDescription(navigateUpDesc)).perform(click())

        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT))) // Left drawer is open open.
    }

}