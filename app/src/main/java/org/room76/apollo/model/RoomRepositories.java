package org.room76.apollo.model;

import android.support.annotation.NonNull;

public final class RoomRepositories {

    private RoomRepositories() {
        // no instance
    }

    private static Repository repository = null;

    public synchronized static Repository<Room> getInMemoryRepoInstance(@NonNull RoomsServiceApi roomsServiceApi) {
        if (null == repository) {
            repository = new InMemoryRoomsRepository(roomsServiceApi);
        }
        return repository;
    }
}