package org.room76.apollo.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Main entry point for accessing rooms data.
 */
public interface Repository<T> {

    void updateItem(T item);

    interface LoadCallback<T> {
        void onLoaded(List<T> items);
    }

    interface GetCallback<T> {
        void onLoaded(T item);
    }

    void getItems(@NonNull LoadCallback<T> callback);

    void getItem(@NonNull String roomId, @NonNull GetCallback<T> callback);

    void saveItem(@NonNull T item);

    void refreshData();

    boolean contains(T item);

}
