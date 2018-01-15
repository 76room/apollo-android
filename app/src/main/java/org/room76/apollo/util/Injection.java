package org.room76.apollo.util;

import org.room76.apollo.model.RoomRepositories;
import org.room76.apollo.model.RoomsRepository;
import org.room76.apollo.model.RoomsServiceApiImpl;

/**
 * Enables injection of production implementations for
 * {@link RoomsRepository} at compile time. Dagger analog
 */
public class Injection {

    public static RoomsRepository provideRoomsRepository() {
        return RoomRepositories.getInMemoryRepoInstance(new RoomsServiceApiImpl());
    }
}
