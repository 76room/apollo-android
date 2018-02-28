package org.room76.apollo.rooms;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.room76.apollo.R;
import org.room76.apollo.addroom.AddRoomActivity;
import org.room76.apollo.model.Room;
import org.room76.apollo.roomdetail.RoomDetailActivity;
import org.room76.apollo.signin.SignInState;
import org.room76.apollo.util.CircleTransform;
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

    private ActivityOptions mOptions;

    protected FloatingActionButton mFab;

    private boolean isFiltered;

    public RoomsFragment() {
        // Requires empty public constructor
    }

    public static RoomsFragment newInstance(boolean filter) {
        RoomsFragment f = new RoomsFragment();
        Bundle b = new Bundle();
        b.putBoolean("my", filter);
        f.setArguments(b);
        return f;
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
        mFab = getActivity().findViewById(R.id.fab_add_rooms);

        mFab.setImageResource(R.drawable.ic_add);
        mFab.setOnClickListener(new View.OnClickListener() {
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

        isFiltered = getArguments().getBoolean("my");
        if (!isFiltered) mFab.setVisibility(View.GONE);
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
        if (isFiltered) {
            List<Room> filtered = new ArrayList<>();
            for (Room item: rooms) {
                if (item.containsUser(SignInState.getInstance().getUser().getUid())) {
                    filtered.add(item);
                }
            }
            rooms = filtered;
        }
        mListAdapter.replaceData(rooms);
    }

    @Override
    public void showAddRoom() {
        if (SignInState.getInstance().getUser() == null) {
            Toast.makeText(getContext(),"Please sigh in to add rooms",Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), AddRoomActivity.class);
            startActivityForResult(intent, REQUEST_ADD_ROOM);
        }

    }

    @Override
    public void showRoomDetailUi(String roomId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), RoomDetailActivity.class);
        intent.putExtra(RoomDetailActivity.EXTRA_ROOM_ID, roomId);
        if (mOptions == null) {
            startActivity(intent);
        } else {
            startActivity(intent, mOptions.toBundle());
            mOptions = null;
        }
    }


    private class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
        public static final int ITEM_TYPE_FULL = 0;
        public static final int ITEM_TYPE_WITHOUT_ROOM_IMAGE = 1;

        private List<Room> mRooms;
        private RoomItemListener mItemListener;

        public RoomsAdapter(List<Room> rooms, RoomItemListener itemListener) {
            setList(rooms);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == ITEM_TYPE_WITHOUT_ROOM_IMAGE) {
                View roomView = inflater.inflate(R.layout.component_item_room_no_image, parent, false);
                return new NoImageViewHolder(roomView, mItemListener);
            } else {
                View roomView = inflater.inflate(R.layout.component_item_room_full, parent, false);
                return new FullViewHolder(roomView, mItemListener);
            }

        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            final int itemType = getItemViewType(position);
            final Room room = mRooms.get(position);
            viewHolder.title.setText(room.getTitle());
            viewHolder.mAddButton.setBackground(mActionsListener.contains(room)?
                    getContext().getDrawable(R.drawable.ic_remove) :
                    getContext().getDrawable(R.drawable.ic_add_to_list));

            if (room.getAuthor() != null && room.getAuthor().getPhotoUrl() != null) {
                Glide.with(viewHolder.itemView.getContext())
                        .load(room.getAuthor().getPhotoUrl())
                        .centerCrop()
                        .transform(new CircleTransform(viewHolder.itemView.getContext()))
                        .into(viewHolder.authorImage);
            }

            if (itemType == ITEM_TYPE_FULL) {
                Glide.with(viewHolder.itemView.getContext())
                        .load(room.getImageUrl())
                        .into(((FullViewHolder) viewHolder).roomImage);

                if (room.isOpen()) {
                    ((FullViewHolder)viewHolder).isOpen.setBackgroundResource(R.drawable.ic_lock_open);
                } else {
                    ((FullViewHolder)viewHolder).isOpen.setBackgroundResource(R.drawable.ic_lock_outline);
                }
            } else {
                ((NoImageViewHolder)viewHolder).description.setText(room.getDescription());
            }
            viewHolder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scaleClickAnimation(view);
                    mActionsListener.addToRoom(room);
                    viewHolder.mAddButton.setBackground(mActionsListener.contains(room)?
                            view.getContext().getDrawable(R.drawable.ic_remove) :
                            view.getContext().getDrawable(R.drawable.ic_add_to_list));
                }
            });
            viewHolder.mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scaleClickAnimation(view);
                }
            });
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

        @Override
        public int getItemViewType(int position) {
            if (mRooms.get(position).getImageUrl() == null) {
                return ITEM_TYPE_WITHOUT_ROOM_IMAGE;
            } else {
                return ITEM_TYPE_FULL;
            }
        }

        public Room getItem(int position) {
            return mRooms.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;
            public ImageButton authorImage , mAddButton, mShareButton;

            private RoomItemListener mItemListener;

            public ViewHolder(View itemView, RoomItemListener listener) {
                super(itemView);
                mItemListener = listener;
                title = itemView.findViewById(R.id.room_detail_title);
                authorImage = itemView.findViewById(R.id.room_author_image);
                mShareButton = itemView.findViewById(R.id.share);
                itemView.findViewById(R.id.location).setOnClickListener(this);
                mAddButton = itemView.findViewById(R.id.add);
                authorImage.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.location:
                        //TODO
                        break;
                    default:
                        int position = getAdapterPosition();
                        Room room = getItem(position);
                        mItemListener.onRoomClick(room);
                }
            }
        }

        public class NoImageViewHolder extends ViewHolder {
            public TextView description;

            public NoImageViewHolder(View itemView, RoomItemListener listener) {
                super(itemView, listener);
                description = itemView.findViewById(R.id.room_detail_description);
                description.setOnClickListener(this);
            }
        }

        public class FullViewHolder extends ViewHolder {
            public ImageView roomImage;
            public ImageView isOpen;

            public FullViewHolder(View itemView, RoomItemListener listener) {
                super(itemView, listener);
                roomImage = itemView.findViewById(R.id.room_detail_image);
                roomImage.setOnClickListener(this);
                isOpen = itemView.findViewById(R.id.room_is_open);
            }

            @Override
            public void onClick(View v) {
                Pair<View, String>[] pairs = new Pair[]{
                        Pair.create(roomImage, getString(R.string.room_image_description)),
                };
                mOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),pairs);
                super.onClick(v);
            }
        }

    }

    public interface RoomItemListener {
        void onRoomClick(Room clickedRoom);
    }

    public void update(){
        mListAdapter.notifyDataSetChanged();
    }

    private void scaleClickAnimation(final View view){
        view.animate()
                .scaleX(1.4f)
                .scaleY(1.4f)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setScaleX(1);
                        view.setScaleY(1);
                    }
                })
                .start();
    }

}
