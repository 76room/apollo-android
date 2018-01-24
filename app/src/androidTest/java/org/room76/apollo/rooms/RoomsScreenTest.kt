package org.room76.apollo.rooms

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.room76.apollo.R

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.scrollTo
import android.support.test.espresso.core.deps.guava.base.Preconditions.checkArgument
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf

/**
 * Tests for the rooms screen, the main screen which contains a grid of all rooms.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class RoomsScreenTest {

    /**
     * [ActivityTestRule] is a JUnit [@Rule][Rule] to launch your activity under test.
     *
     *
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    var mRoomsActivityTestRule = ActivityTestRule(RoomsActivity::class.java)

    /**
     * A custom [Matcher] which matches an item in a [RecyclerView] by its text.
     *
     *
     *
     * View constraints:
     *
     *  * View must be a child of a [RecyclerView]
     *
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private fun withItemText(itemText: String): Matcher<View> {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(item: View): Boolean {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                        withText(itemText)).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA RV with text " + itemText)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun clickAddRoomButton_opensAddRoomUi() {
        // Click on the add room button
        onView(withId(R.id.fab_add_rooms)).perform(click())

        // Check if the add room screen is displayed
        onView(withId(R.id.add_room_title)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(Exception::class)
    fun addRoomToRoomsList() {
        val newRoomTitle = "Espresso"
        val newRoomDescription = "UI testing for Android"

        // Click on the add room button
        onView(withId(R.id.fab_add_rooms)).perform(click())

        // Add room title and description
        // Type new room title
        onView(withId(R.id.add_room_title)).perform(typeText(newRoomTitle), closeSoftKeyboard())
        onView(withId(R.id.add_room_description)).perform(typeText(newRoomDescription),
                closeSoftKeyboard()) // Type new room description and close the keyboard

        // Save the room
        onView(withId(R.id.fab_add_rooms)).perform(click())

        // Scroll rooms list to added room, by finding its description
        onView(withId(R.id.rooms_list)).perform(
                scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(newRoomDescription))))

        // Verify room is displayed on screen
        onView(withItemText(newRoomDescription)).check(matches(isDisplayed()))
    }

}