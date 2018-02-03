package org.room76.apollo.rooms

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.room76.apollo.model.Room
import org.room76.apollo.model.RoomsRepository

import java.util.ArrayList

import org.mockito.Matchers.any
import org.mockito.Mockito.verify
import org.room76.apollo.model.FirebaseUserMock

/**
 * Unit tests for the implementation of [RoomsPresenter]
 */
class RoomsPresenterTest {

    @Mock
    private val mRoomsRepository: RoomsRepository? = null

    @Mock
    private val mRoomsView: RoomsContract.View? = null

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private val mLoadRoomsCallbackCaptor: ArgumentCaptor<RoomsRepository.LoadRoomsCallback>? = null

    private var mRoomsPresenter: RoomsPresenter? = null

    @Before
    fun setupRoomsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        mRoomsPresenter = RoomsPresenter(mRoomsRepository!!, mRoomsView!!)
    }

    @Test
    fun loadRoomsFromRepositoryAndLoadIntoView() {
        // Given an initialized RoomsPresenter with initialized rooms
        // When loading of Rooms is requested
        mRoomsPresenter!!.loadRooms(true)

        // Callback is captured and invoked with stubbed rooms
        verify<RoomsRepository>(mRoomsRepository).getRooms(mLoadRoomsCallbackCaptor!!.capture())
        mLoadRoomsCallbackCaptor.value.onRoomsLoaded(ROOMS)

        // Then progress indicator is hidden and rooms are shown in UI
        val inOrder = Mockito.inOrder(mRoomsView)
        inOrder.verify<RoomsContract.View>(mRoomsView).setProgressIndicator(true)
        inOrder.verify<RoomsContract.View>(mRoomsView).setProgressIndicator(false)
        verify<RoomsContract.View>(mRoomsView).showRooms(ROOMS)
    }

    @Test
    fun clickOnFab_ShowsAddsRoomUi() {
        // When adding a new room
        mRoomsPresenter!!.addNewRoom()

        // Then add room UI is shown
        verify<RoomsContract.View>(mRoomsView).showAddRoom()
    }

    @Test
    fun clickOnRoom_ShowsDetailUi() {
        // Given a stubbed room
        val requestedRoom = Room(FirebaseUserMock("User"), "Details Requested", "For this room", true)

        // When open room details is requested
        mRoomsPresenter!!.openRoomDetails(requestedRoom)

        // Then room detail UI is shown
        verify<RoomsContract.View>(mRoomsView).showRoomDetailUi(any(String::class.java))
    }

    companion object {

        private val ROOMS = ArrayList<Room>()

        init {
            ROOMS.add(Room(FirebaseUserMock("User"),"Title1", "Description1", true))
            ROOMS.add(Room(FirebaseUserMock("User"),"Title2", "Description2", true))
        }

        private val EMPTY_ROOMS = ArrayList<Room>(0)
    }
}
