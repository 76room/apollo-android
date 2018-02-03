package org.room76.apollo.model

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.ArrayList

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.mockito.Matchers.any
import org.mockito.Matchers.eq
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
class InMemoryRoomsRepositoryTest {

    private var mRoomsRepository: InMemoryRoomsRepository? = null

    @Mock
    private val mServiceApi: RoomsServiceApiImpl? = null

    @Mock
    private val mGetRoomCallback: RoomsRepository.GetRoomCallback? = null

    @Mock
    private val mLoadRoomsCallback: RoomsRepository.LoadRoomsCallback? = null

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private val mRoomsServiceCallbackCaptor: ArgumentCaptor<RoomsServiceApi.RoomsServiceCallback<List<Room>>>? = null

    @Before
    fun setupRoomsRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        ROOMS.add(Room(FirebaseUserMock("User"), "Title1", "Description1", true, mIsEmpty))
        ROOMS.add(Room(FirebaseUserMock("User"), "Title2", "Description2", true, mIsEmpty))

        // Get a reference to the class under test
        mRoomsRepository = InMemoryRoomsRepository(mServiceApi!!)
    }

    @Test
    fun getRooms_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the rooms repository
        twoLoadCallsToRepository(mLoadRoomsCallback!!)

        // Then rooms where only requested once from Service API
        verify<RoomsServiceApiImpl>(mServiceApi).getAllRooms(any(RoomsServiceApi.RoomsServiceCallback::class.java))
    }

    @Test
    fun invalidateCache_DoesNotCallTheServiceApi() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadRoomsCallback!!)

        // When data refresh is requested
        mRoomsRepository!!.refreshData()
        mRoomsRepository!!.getRooms(mLoadRoomsCallback) // Third call to API

        // The rooms where requested twice from the Service API (Caching on first and third call)
        verify<RoomsServiceApiImpl>(mServiceApi, times(2)).getAllRooms(any(RoomsServiceApi.RoomsServiceCallback::class.java))
    }

    @Test
    fun getRooms_requestsAllRoomsFromServiceApi() {
        // When rooms are requested from the rooms repository
        mRoomsRepository!!.getRooms(mLoadRoomsCallback!!)

        // Then rooms are loaded from the service API
        verify<RoomsServiceApiImpl>(mServiceApi).getAllRooms(any(RoomsServiceApi.RoomsServiceCallback::class.java))
    }

    @Test
    fun saveRoom_savesRoomToServiceAPIAndInvalidatesCache() {
        // Given a stub room with title and description
        val newRoom = Room(FirebaseUserMock("User"), ROOM_TITLE, "Some Room Description", true, mIsEmpty)

        // When a room is saved to the rooms repository
        mRoomsRepository!!.saveRoom(newRoom)

        // Then the rooms cache is cleared
        assertThat(mRoomsRepository!!.mCachedRooms, `is`(nullValue()))
    }

    @Test
    fun getRoom_requestsSingleRoomFromServiceApi() {
        // When a room is requested from the rooms repository
        mRoomsRepository!!.getRoom(ROOM_TITLE, mGetRoomCallback!!)

        // Then the room is loaded from the service API
        verify<RoomsServiceApiImpl>(mServiceApi).getRoom(eq(ROOM_TITLE), any(RoomsServiceApi.RoomsServiceCallback::class.java))
    }

    /**
     * Convenience method that issues two calls to the rooms repository
     */
    private fun twoLoadCallsToRepository(callback: RoomsRepository.LoadRoomsCallback) {
        // When rooms are requested from repository
        mRoomsRepository!!.getRooms(callback) // First call to API

        // Use the Mockito Captor to capture the callback
        verify<RoomsServiceApiImpl>(mServiceApi).getAllRooms(mRoomsServiceCallbackCaptor!!.capture())

        // Trigger callback so rooms are cached
        mRoomsServiceCallbackCaptor.value.onLoaded(ROOMS)

        mRoomsRepository!!.getRooms(callback) // Second call to API
    }

    companion object {

        private val ROOM_TITLE = "title"

        private val ROOMS = ArrayList<Room>()
    }

}