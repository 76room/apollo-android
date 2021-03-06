package org.room76.apollo.roomdetail

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.room76.apollo.model.Room

import org.mockito.Matchers.eq
import org.mockito.Mockito.verify
import org.room76.apollo.model.Repository
import org.room76.apollo.model.User

/**
 * Unit tests for the implementation of [RoomDetailPresenter]
 */
class RoomsDetailPresenterTest {

    @Mock
    private val mRoomsRepository: Repository<Room>? = null

    @Mock
    private val mRoomDetailView: RoomDetailContract.View? = null

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private val mGetRoomCallbackCaptor: ArgumentCaptor<Repository.GetCallback<Room>>? = null

    private var mRoomsDetailsPresenter: RoomDetailPresenter? = null

    @Before
    fun setupRoomsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        mRoomsDetailsPresenter = RoomDetailPresenter(mRoomsRepository!!, mRoomDetailView!!)
    }

    @Test
    fun getRoomFromRepositoryAndLoadIntoView() {
        // Given an initialized RoomDetailPresenter with stubbed room
        val room = Room(User("User"),TITLE_TEST, DESCRIPTION_TEST, true)

        // When rooms presenter is asked to open a room
        mRoomsDetailsPresenter!!.openRoom(room.id)

        // Then room is loaded from model, callback is captured and progress indicator is shown
        verify<Repository<Room>>(mRoomsRepository).getItem(eq(room.id), mGetRoomCallbackCaptor!!.capture())

        verify<RoomDetailContract.View>(mRoomDetailView).setProgressIndicator(true)

        // When room is finally loaded
        mGetRoomCallbackCaptor.value.onLoaded(room) // Trigger callback

        // Then progress indicator is hidden and title and description are shown in UI
        verify<RoomDetailContract.View>(mRoomDetailView).setProgressIndicator(false)
        verify<RoomDetailContract.View>(mRoomDetailView).showTitle(TITLE_TEST)
        verify<RoomDetailContract.View>(mRoomDetailView).showDescription(DESCRIPTION_TEST)

    }

    @Test
    fun getUnknownRoomFromRepositoryAndLoadIntoView() {
        // When loading of a room is requested with an invalid room ID.
        mRoomsDetailsPresenter!!.openRoom(INVALID_ID)

        // Then room with invalid id is attempted to load from model, callback is captured and
        // progress indicator is shown.

        verify<RoomDetailContract.View>(mRoomDetailView).setProgressIndicator(true)

        verify<Repository<Room>>(mRoomsRepository).getItem(eq(INVALID_ID), mGetRoomCallbackCaptor!!.capture())

        // When room is finally loaded
        mGetRoomCallbackCaptor.value.onLoaded(null) // Trigger callback

        // Then progress indicator is hidden and missing room UI is shown

        verify<RoomDetailContract.View>(mRoomDetailView).setProgressIndicator(false)
        verify<RoomDetailContract.View>(mRoomDetailView).showMissingRoom()

    }

    companion object {

        val INVALID_ID = "INVALID_ID"

        val TITLE_TEST = "title"

        val DESCRIPTION_TEST = "description"
    }
}
