package org.room76.apollo.rooms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.room76.apollo.R;
import org.room76.apollo.addroom.AddRoomActivity;
import org.room76.apollo.model.Room;
import org.room76.apollo.roomdetail.RoomDetailActivity;
import org.room76.apollo.util.Injection;

import java.util.ArrayList;
import java.util.List;

/**
 * Display a grid of {@link Room}s
 */
public class RoomsFragment extends Fragment implements RoomsContract.View {

    private static final int REQUEST_ADD_ROOM = 1;

    private RoomsContract.UserActionsListener mActionsListener;

    private RoomsAdapter mListAdapter;

    public RoomsFragment() {
        // Requires empty public constructor
    }

    public static RoomsFragment newInstance() {
        return new RoomsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new RoomsAdapter(new ArrayList<Room>(0), mItemListener);
        mActionsListener = new RoomsPresenter(Injection.provideRoomsRepository(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadRooms(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If a room was successfully added, show snackbar
        if (REQUEST_ADD_ROOM == requestCode && Activity.RESULT_OK == resultCode) {
            Snackbar.make(getView(), getString(R.string.successfully_saved_room_message),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rooms_list);
        recyclerView.setAdapter(mListAdapter);

//        int numColumns = getContext().getResources().getInteger(R.integer.num_rooms_columns);

        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_rooms);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.addNewRoom();
            }
        });

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mActionsListener.loadRooms(true);
            }
        });
        return root;
    }

    /**
     * Listener for clicks on rooms in the RecyclerView.
     */
    RoomItemListener mItemListener = new RoomItemListener() {
        @Override
        public void onRoomClick(Room clickedRoom) {
            mActionsListener.openRoomDetails(clickedRoom);
        }
    };

    @Override
    public void setProgressIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showRooms(List<Room> rooms) {
        mListAdapter.replaceData(rooms);
    }

    @Override
    public void showAddRoom() {
        Intent intent = new Intent(getContext(), AddRoomActivity.class);
        startActivityForResult(intent, REQUEST_ADD_ROOM);
    }

    @Override
    public void showRoomDetailUi(String roomId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), RoomDetailActivity.class);
        intent.putExtra(RoomDetailActivity.EXTRA_ROOM_ID, roomId);
        startActivity(intent);
    }


    private static class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

        private List<Room> mRooms;
        private RoomItemListener mItemListener;

        public RoomsAdapter(List<Room> rooms, RoomItemListener itemListener) {
            setList(rooms);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View roomView = inflater.inflate(R.layout.component_item_room, parent, false);

            return new ViewHolder(roomView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Room room = mRooms.get(position);

            viewHolder.title.setText(room.getTitle());
            viewHolder.description.setText(room.getDescription());
        }

        public void replaceData(List<Room> rooms) {
            setList(rooms);
            notifyDataSetChanged();
        }

        private void setList(List<Room> rooms) {
            mRooms = rooms;
        }

        @Override
        public int getItemCount() {
            return mRooms.size();
        }

        public Room getItem(int position) {
            return mRooms.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;

            public TextView description;
            private RoomItemListener mItemListener;

            public ViewHolder(View itemView, RoomItemListener listener) {
                super(itemView);
                mItemListener = listener;
                title = itemView.findViewById(R.id.room_detail_title);
                description = itemView.findViewById(R.id.room_detail_description);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Room room = getItem(position);
                mItemListener.onRoomClick(room);

            }
        }
    }

    public interface RoomItemListener {

        void onRoomClick(Room clickedRoom);
    }

}