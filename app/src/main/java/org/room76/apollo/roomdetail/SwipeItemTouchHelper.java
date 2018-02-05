package org.room76.apollo.roomdetail;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

/**
 * Created by a.zatsepin on 03/02/2018.
 */

public class SwipeItemTouchHelper  extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public SwipeItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((RoomDetailFragment.TrackAdapter.TrackViewHolder) viewHolder).mForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((RoomDetailFragment.TrackAdapter.TrackViewHolder) viewHolder).mForeground;
        final View backgroundView = ((RoomDetailFragment.TrackAdapter.TrackViewHolder) viewHolder).mBackground;
        
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((RoomDetailFragment.TrackAdapter.TrackViewHolder) viewHolder).mForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((RoomDetailFragment.TrackAdapter.TrackViewHolder) viewHolder).mForeground;
        final View backgroundView = ((RoomDetailFragment.TrackAdapter.TrackViewHolder) viewHolder).mBackground;

        if ((dX <= backgroundView.getWidth()/2&&dX>0) || (-dX <= backgroundView.getWidth()/2&&dX<0)) {
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
