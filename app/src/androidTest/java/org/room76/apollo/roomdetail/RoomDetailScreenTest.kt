package org.room76.apollo.roomdetail

import android.app.Activity
import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.room76.apollo.R
import org.room76.apollo.model.Room

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.AllOf.allOf
import org.room76.apollo.matcher.ImageViewHasDrawableMatcher.hasDrawable

/**
 * Tests for the rooms screen, the main screen which contains a list of all rooms.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class RoomDetailScreenTest {

    /**
     * [ActivityTestRule] is a JUnit [@Rule][Rule] to launch your activity under test.
     *
     *
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     *
     *
     *
     * Sometimes an [Activity] requires a custom start [Intent] to receive data
     * from the source Activity. ActivityTestRule has a feature which let's you lazily start the
     * Activity under test, so you can control the Intent that is used to start the target Activity.
     */
    @Rule
    var mRoomDetailActivityTestRule = ActivityTestRule(RoomDetailActivity::class.java, true /* Initial touch mode  */,
            false /* Lazily launch activity */)

    /**
     * Setup your test fixture with a fake room id. The [RoomDetailActivity] is started with
     * a particular room id, which is then loaded from the service API.
     *
     *
     *
     * Room that this test runs hermetically and is fully isolated using a fake implementation of
     * the service API. This is a great way to make your tests more reliable and faster at the same
     * time, since they are isolated from any outside dependencies.
     */
    @Before
    fun intentWithStubbedRoomId() {
        // Add a room stub to the fake service api layer.
        //        RoomsServiceApiImpl.saveRoom(ROOM);

        // Lazily start the Activity from the ActivityTestRule this time to inject the start Intent
        val startIntent = Intent()
        startIntent.putExtra(RoomDetailActivity.EXTRA_ROOM_ID, ROOM.id)
        mRoomDetailActivityTestRule.launchActivity(startIntent)

        registerIdlingResource()
    }

    @Test
    @Throws(Exception::class)
    fun roomDetails_DisplayedInUi() {
        // Check that the room title, description and image are displayed
        onView(withId(R.id.room_detail_title)).check(matches(withText(ROOM_TITLE)))
        onView(withId(R.id.room_detail_description)).check(matches(withText(ROOM_DESCRIPTION)))
        onView(withId(R.id.room_detail_image)).check(matches(allOf<View>(
                hasDrawable(),
                isDisplayed())))
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mRoomDetailActivityTestRule.activity.countingIdlingResource)
    }

    /**
     * Convenience method to register an IdlingResources with Espresso. IdlingResource resource is
     * a great way to tell Espresso when your app is in an idle state. This helps Espresso to
     * synchronize your test actions, which makes tests significantly more reliable.
     */
    private fun registerIdlingResource() {
        Espresso.registerIdlingResources(
                mRoomDetailActivityTestRule.activity.countingIdlingResource)
    }

    companion object {

        private val ROOM_TITLE = "ATSL"

        private val ROOM_DESCRIPTION = "Rocks"

        private val ROOM_IMAGE = "file:///android_asset/atsl-logo.png"

        /**
         * [Room] stub that is added to the fake service API layer.
         */
        private val ROOM = Room(ROOM_TITLE, ROOM_DESCRIPTION, ROOM_IMAGE)
    }
}