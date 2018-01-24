package org.room76.apollo.rooms;

import android.support.annotation.NonNull;

import org.room76.apollo.model.Room;
import org.room76.apollo.model.RoomsRepository;
import org.room76.apollo.util.EspressoIdlingResource;

import java.util.List;



/**
 * Listens to user actions from the UI ({@link RoomsFragment}), retrieves the data and updates the
 * UI as required.
 */
public class RoomsPresenter implements RoomsContract.UserActionsListener {

    private final RoomsRepository mRoomsRepository;
    private final RoomsContract.View mRoomsView;

    public RoomsPresenter(
            @NonNull RoomsRepository roomsRepository, @NonNull RoomsContract.View roomsView) {
        mRoomsRepository = roomsRepository;
        mRoomsView = roomsView;
    }

    @Override
    public void loadRooms(boolean forceUpdate) {
        mRoomsView.setProgressIndicator(true);
        if (forceUpdate) {
            mRoomsRepository.refreshData();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mRoomsRepository.getRooms(new RoomsRepository.LoadRoomsCallback() {
            @Override
            public void onRoomsLoaded(List<Room> rooms) {
                EspressoIdlingResource.decrement(); // Set app as idle.
                mRoomsView.setProgressIndicator(false);
                mRoomsView.showRooms(rooms);
            }
        });
    }

    @Override
    public void addNewRoom() {
        mRoomsView.showAddRoom();
    }

    @Override
    public void openRoomDetails(@NonNull Room requestedRoom) {
        mRoomsView.showRoomDetailUi(requestedRoom.getId());
    }

}
