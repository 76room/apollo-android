package org.room76.apollo.addroom

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.room76.apollo.model.Room
import org.room76.apollo.model.RoomsRepository

import java.io.IOException

import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.mockito.Matchers.contains
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**
 * Unit tests for the implementation of [AddRoomPresenter].
 */
class AddRoomPresenterTest {

    @Mock
    private val mRoomsRepository: RoomsRepository? = null

    @Mock
    private val mAddRoomView: AddRoomContract.View? = null

    private var mAddRoomsPresenter: AddRoomPresenter? = null

    @Before
    fun setupAddRoomPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        mAddRoomsPresenter = AddRoomPresenter(mRoomsRepository!!, mAddRoomView!!)
    }

    @Test
    fun saveRoomToRepository_showsSuccessMessageUi() {
        // When the presenter is asked to save a room
        mAddRoomsPresenter!!.saveRoom("New Room Title", "Some Room Description")

        // Then a room is,
        verify<RoomsRepository>(mRoomsRepository).saveRoom(any(Room::class.java)) // saved to the model
        verify<AddRoomContract.View>(mAddRoomView).showRoomsList() // shown in the UI
    }

    @Test
    fun saveRoom_emptyRoomShowsErrorUi() {
        // When the presenter is asked to save an empty room
        mAddRoomsPresenter!!.saveRoom("", "")

        // Then an empty not error is shown in the UI
        verify<AddRoomContract.View>(mAddRoomView).showEmptyRoomError()
    }

    @Ignore
    @Test
    @Throws(IOException::class)
    fun takePicture_CreatesFileAndOpensCamera() {
        // When the presenter is asked to take an image
        mAddRoomsPresenter!!.takePicture()

        // Then an image file is created snd camera is opened
        //        verify(mImageFile).create(anyString(), anyString());
        //        verify(mImageFile).getPath();
        //        verify(mAddRoomView).openCamera(anyString());
    }

    @Ignore
    @Test
    fun imageAvailable_SavesImageAndUpdatesUiWithThumbnail() {
        // Given an a stubbed image file
        val imageUrl = "path/to/file"
        //        when(mImageFile.exists()).thenReturn(true);
        //        when(mImageFile.getPath()).thenReturn(imageUrl);

        // When an image is made available to the presenter
        mAddRoomsPresenter!!.imageAvailable()

        // Then the preview image of the stubbed image is shown in the UI
        verify<AddRoomContract.View>(mAddRoomView).showImagePreview(contains(imageUrl))
    }

    @Ignore
    @Test
    fun imageAvailable_FileDoesRoomxistShowsErrorUi() {
        // Given the image file does not exist
        //        when(mImageFile.exists()).thenReturn(false);

        // When an image is made available to the presenter
        mAddRoomsPresenter!!.imageAvailable()

        // Then an error is shown in the UI and the image file is deleted
        verify<AddRoomContract.View>(mAddRoomView).showImageError()
        //        verify(mImageFile).delete();
    }

    @Ignore
    @Test
    fun noImageAvailable_ShowsErrorUi() {
        // When the presenter is notified that image capturing failed
        mAddRoomsPresenter!!.imageCaptureFailed()

        // Then an error is shown in the UI and the image file is deleted
        verify<AddRoomContract.View>(mAddRoomView).showImageError()
        //        verify(mImageFile).delete();
    }

}
