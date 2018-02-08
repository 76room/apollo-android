package org.room76.apollo.util;

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

    private static LocalTrackRepository repository = new LocalTrackRepository();

    static {
        repository.refreshData();
    }

    public static Repository<Room> provideRoomsRepository() {
        return RoomRepositories.getInMemoryRepoInstance(new RoomsServiceApiImpl());
    }

    public static Repository<Track> provideTrackRepository() {
        return repository;
    }
}

