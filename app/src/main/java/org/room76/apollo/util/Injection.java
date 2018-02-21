package org.room76.apollo.util;

import org.room76.apollo.model.FirebaseDataRoomRepository;
import org.room76.apollo.model.LocalTrackRepository;
import org.room76.apollo.model.Repository;
import org.room76.apollo.model.Room;
import org.room76.apollo.model.RoomRepositories;
import org.room76.apollo.model.RoomsServiceApiImpl;
import org.room76.apollo.model.Track;

/**
 * Enables injection of production implementations for
 * {@link Repository} at compile time. Dagger analog
 */
public final class Injection {

    private static Repository<Track> mTrackRepository = new LocalTrackRepository();
    private static Repository<Room> mRoomRepository = new FirebaseDataRoomRepository();

    static {
        mTrackRepository.refreshData();
        mRoomRepository.refreshData();
    }

    public static Repository<Room> provideRoomsRepository() {
        return RoomRepositories.getFirebaseRepoInstance();
    }

    public static Repository<Track> provideTrackRepository() {
        return mTrackRepository;
    }
}

