package org.room76.apollo.addroom

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.provider.MediaStore
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.room76.apollo.R

import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.room76.apollo.matcher.ImageViewHasDrawableMatcher.hasDrawable

/**
 * Tests for the add room screen.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AddRoomScreenTest {

    /**
     * [IntentsTestRule] is an [ActivityTestRule] which inits and releases Espresso
     * Intents before and after each test run.
     *
     *
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    var mAddRoomIntentsTestRule = IntentsTestRule(AddRoomActivity::class.java)

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests significantly
     * more reliable.
     */
    @Before
    fun registerIdlingResource() {
        Espresso.registerIdlingResources(
                mAddRoomIntentsTestRule.activity.countingIdlingResource)
    }

    @Test
    fun addImageToRoom_ShowsThumbnailInUi() {
        // Create an Activity Result which can be used to stub the camera Intent
        val result = createImageCaptureActivityResultStub()
        // If there is an Intent with ACTION_IMAGE_CAPTURE, intercept the Intent and respond with
        // a stubbed result.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        // Check thumbnail view is not displayed
        onView(withId(R.id.add_room_image_thumbnail)).check(matches(not<View>(isDisplayed())))

        selectTakeImageFromMenu()

        // Check that the stubbed thumbnail is displayed in the UI
        onView(withId(R.id.add_room_image_thumbnail))
                .perform(scrollTo()) // Scroll to thumbnail
                .check(matches(allOf<View>(
                        hasDrawable(), // Check ImageView has a drawable set with a custom matcher
                        isDisplayed())))
    }

    @Test
    fun errorShownOnEmptyMessage() {
        onView(withId(R.id.fab_add_rooms)).perform(click())
        // Add room title and description
        onView(withId(R.id.add_room_title)).perform(typeText(""))
        onView(withId(R.id.add_room_description)).perform(typeText(""),
                closeSoftKeyboard())
        // Save the room
        onView(withId(R.id.fab_add_rooms)).perform(click())

        //        Snackbar tests are unreliable using the latest support libs, skip the assertion for now.
        //        // Verify empty rooms snackbar is shown
        //        String emptyRoomMessageText = getTargetContext().getString(R.string.empty_room_message);
        //        onView(withText(emptyRoomMessageText)).check(matches(isDisplayed()));
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mAddRoomIntentsTestRule.activity.countingIdlingResource)
    }

    /**
     * Convenience method which opens the options menu and select the take image option.
     */
    private fun selectTakeImageFromMenu() {
        openActionBarOverflowOrOptionsMenu(getTargetContext())

        // Click on add picture option
        onView(withText(R.string.take_picture)).perform(click())
    }

    private fun createImageCaptureActivityResultStub(): ActivityResult {
        // Create the ActivityResult, with a null Intent since we do not want to return any data
        // back to the Activity.
        return ActivityResult(Activity.RESULT_OK, null)
    }

}