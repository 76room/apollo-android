package org.room76.apollo.model;

import android.support.annotation.NonNull;

public class RoomRepositories {

    private RoomRepositories() {
        // no instance
    }

    private static RoomsRepository repository = null;

    public synchronized static RoomsRepository getInMemoryRepoInstance(@NonNull RoomsServiceApi roomsServiceApi) {
        if (null == repository) {
            repository = new InMemoryRoomsRepository(roomsServiceApi);
        }
        return repository;
    }
}