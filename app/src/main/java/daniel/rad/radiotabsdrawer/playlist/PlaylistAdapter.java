package daniel.rad.radiotabsdrawer.playlist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.CreatePlaylistFragment;
import daniel.rad.radiotabsdrawer.playlist.removeProgramsFromPlaylist.RemoveProgramFromPlaylistFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class PlaylistAdapter extends RecyclerView.Adapter<PlayListViewHolder> {
    ArrayList<Playlist> playlists;
    Context context;

    public PlaylistAdapter(ArrayList<Playlist> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.playlist_item, viewGroup, false);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int i) {
        Playlist playlist = playlists.get(i);

        String playlistName = playlist.getName();
        int defaultPic;
        holder.tvPlayList.setText(playlistName);
        if (playlistName.equals("מומלצים")) {
            defaultPic = R.drawable.ic_default_recommended;
        } else if (playlistName.equals("מועדפים")) {
            defaultPic = R.drawable.ic_default_favourite;
        } else {
            defaultPic = R.drawable.ic_default_pic;
        }

        ArrayList<ProgramsData> programsDataList = (ArrayList<ProgramsData>) playlist.getProgramsData();
        holder.ivProfileAll.setImageResource(defaultPic);
        if (programsDataList.size() == 0) {
            holder.ivProfileAll.setImageResource(defaultPic);
            holder.ivProfilePic1.setVisibility(View.INVISIBLE);
            holder.ivProfilePic2.setVisibility(View.INVISIBLE);
            holder.ivProfilePic3.setVisibility(View.INVISIBLE);
            holder.ivProfilePic4.setVisibility(View.INVISIBLE);
        } else if (programsDataList.size() == 1) {
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfileAll.setImageResource(defaultPic);
            } else {
                holder.ivProfileAll.setImageResource(programsDataList.get(0).getProfilePic());
            }
            holder.ivProfilePic1.setVisibility(View.INVISIBLE);
            holder.ivProfilePic2.setVisibility(View.INVISIBLE);
            holder.ivProfilePic3.setVisibility(View.INVISIBLE);
            holder.ivProfilePic4.setVisibility(View.INVISIBLE);
        } else if (programsDataList.size() == 2) {
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic1.setImageResource(defaultPic);
                holder.ivProfilePic4.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic1.setImageResource(programsDataList.get(0).getProfilePic());
                holder.ivProfilePic4.setImageResource(programsDataList.get(0).getProfilePic());
            }
            if (programsDataList.get(1).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic2.setImageResource(defaultPic);
                holder.ivProfilePic3.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic3.setImageResource(programsDataList.get(1).getProfilePic());
                holder.ivProfilePic2.setImageResource(programsDataList.get(1).getProfilePic());
            }
            holder.ivProfileAll.setVisibility(View.INVISIBLE);
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic &&
                    programsDataList.get(1).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfileAll.setVisibility(View.VISIBLE);
                holder.ivProfilePic1.setVisibility(View.INVISIBLE);
                holder.ivProfilePic2.setVisibility(View.INVISIBLE);
                holder.ivProfilePic3.setVisibility(View.INVISIBLE);
                holder.ivProfilePic4.setVisibility(View.INVISIBLE);
            }
        } else if (programsDataList.size() == 3) {
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic1.setImageResource(defaultPic);
                holder.ivProfilePic4.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic1.setImageResource(programsDataList.get(0).getProfilePic());
                holder.ivProfilePic4.setImageResource(programsDataList.get(0).getProfilePic());
            }
            if (programsDataList.get(1).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic2.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic2.setImageResource(programsDataList.get(2).getProfilePic());
            }
            if (programsDataList.get(2).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic3.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic3.setImageResource(programsDataList.get(1).getProfilePic());
            }
            holder.ivProfileAll.setVisibility(View.INVISIBLE);
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic &&
                    programsDataList.get(1).getProfilePic() == R.drawable.ic_default_pic &&
                    programsDataList.get(2).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfileAll.setVisibility(View.VISIBLE);
                holder.ivProfilePic1.setVisibility(View.INVISIBLE);
                holder.ivProfilePic2.setVisibility(View.INVISIBLE);
                holder.ivProfilePic3.setVisibility(View.INVISIBLE);
                holder.ivProfilePic4.setVisibility(View.INVISIBLE);
            }

        } else if (programsDataList.size() >= 4) {
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic1.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic1.setImageResource(programsDataList.get(0).getProfilePic());
            }
            if (programsDataList.get(1).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic2.setImageResource(defaultPic);

            } else {
                holder.ivProfilePic3.setImageResource(programsDataList.get(1).getProfilePic());
            }
            if (programsDataList.get(2).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic3.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic2.setImageResource(programsDataList.get(2).getProfilePic());
            }
            if (programsDataList.get(3).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfilePic4.setImageResource(defaultPic);
            } else {
                holder.ivProfilePic4.setImageResource(programsDataList.get(3).getProfilePic());
            }
            holder.ivProfileAll.setVisibility(View.INVISIBLE);
            if (programsDataList.get(0).getProfilePic() == R.drawable.ic_default_pic &&
                    programsDataList.get(1).getProfilePic() == R.drawable.ic_default_pic &&
                    programsDataList.get(2).getProfilePic() == R.drawable.ic_default_pic &&
                    programsDataList.get(3).getProfilePic() == R.drawable.ic_default_pic) {
                holder.ivProfileAll.setVisibility(View.VISIBLE);
                holder.ivProfilePic1.setVisibility(View.INVISIBLE);
                holder.ivProfilePic2.setVisibility(View.INVISIBLE);
                holder.ivProfilePic3.setVisibility(View.INVISIBLE);
                holder.ivProfilePic4.setVisibility(View.INVISIBLE);
            }
        }

        holder.playlist = playlist;
        holder.context = context;
        holder.playlists = playlists;

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
}

class PlayListViewHolder extends RecyclerView.ViewHolder {

    TextView tvPlayList;
    ImageView ivProfilePic1;
    ImageView ivProfilePic2;
    ImageView ivProfilePic3;
    ImageView ivProfilePic4;
    ImageView ivProfileAll;
    ImageView ivDeletePlaylist;
    Playlist playlist;
    Context context;
    Animation mShakeAnimation;
    ArrayList<Playlist> playlists;
    boolean isDoublePressed = false;

    public PlayListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvPlayList = itemView.findViewById(R.id.tvPlaylist);
        ivProfilePic1 = itemView.findViewById(R.id.ivProfilePic1);
        ivProfilePic2 = itemView.findViewById(R.id.ivProfilePic2);
        ivProfilePic3 = itemView.findViewById(R.id.ivProfilePic3);
        ivProfilePic4 = itemView.findViewById(R.id.ivProfilePic4);
        ivProfileAll = itemView.findViewById(R.id.ivProfileAll);
        ivDeletePlaylist = itemView.findViewById(R.id.ivDeletePlaylist);

        itemView.setOnClickListener(v -> {
            if (isDoublePressed) {
                mShakeAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.stop_shake);
                itemView.startAnimation(mShakeAnimation);
                ivDeletePlaylist.setVisibility(View.GONE);
                isDoublePressed = false;
            } else {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().
                        beginTransaction().
                        addToBackStack("playlistList").
                        replace(R.id.playlist_frame, PlaylistDetailsFragment.newInstance(playlist)).
                        commit();
            }
        });
        itemView.setOnLongClickListener(v -> {
            if (!isDoublePressed) {
                ivDeletePlaylist.setVisibility(View.VISIBLE);
                mShakeAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.shake);
                itemView.startAnimation(mShakeAnimation);
                isDoublePressed = true;
            }
            return true;
        });

        ivDeletePlaylist.setOnClickListener(v -> {
            if (ivDeletePlaylist.getVisibility() == View.VISIBLE) {
                Context context = v.getContext();
                MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.menu_edit_playlists, menuBuilder);
                MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, ivDeletePlaylist);
                optionsMenu.setForceShowIcon(true);

                if (playlist.getName().equals("מועדפים") || playlist.getName().equals("מומלצים")) {
                    menuBuilder.setCallback(new MenuBuilder.Callback() {
                        @Override
                        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.itemAddProgram:
                                    AppCompatActivity activity = (AppCompatActivity) context;
                                    activity.getSupportFragmentManager().
                                            beginTransaction().
                                            addToBackStack("AddProgramToPlaylist").
                                            replace(R.id.playlist_frame, CreatePlaylistFragment.newInstance(playlist, playlists,false)).
                                            commit();
                                    return true;
                                case R.id.itemRemoveProgram:
                                    activity = (AppCompatActivity) context;
                                    activity.getSupportFragmentManager().
                                            beginTransaction().
                                            addToBackStack("removeProgramFromPlaylist").
                                            replace(R.id.playlist_frame, RemoveProgramFromPlaylistFragment.newInstance(playlist, playlists)).
                                            commit();
                                    return true;
                                case R.id.itemRemoveList:
                                    new AlertDialog.Builder(context).
                                            setTitle("לא ניתן למחוק רשימה זו").
                                            setPositiveButton("הבנתי", (dialog, which) -> {
                                                mShakeAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.stop_shake);
                                                itemView.startAnimation(mShakeAnimation);
                                                ivDeletePlaylist.setVisibility(View.GONE);
                                                isDoublePressed = false;
                                            }).show();
                                    return true;
                            }
                            return false;
                        }
                        @Override
                        public void onMenuModeChange(MenuBuilder menuBuilder) {
                        }
                    });
                } else {
                    menuBuilder.setCallback(new MenuBuilder.Callback() {
                        @Override
                        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.itemAddProgram:
                                    AppCompatActivity activity = (AppCompatActivity) context;
                                    activity.getSupportFragmentManager().
                                            beginTransaction().
                                            addToBackStack("addProgramToPlaylist").
                                            replace(R.id.playlist_frame, CreatePlaylistFragment.newInstance(playlist, playlists,false)).
                                            commit();
                                    return true;
                                case R.id.itemRemoveProgram:
                                    activity = (AppCompatActivity) context;
                                    activity.getSupportFragmentManager().
                                            beginTransaction().
                                            addToBackStack("removeProgramFromPlaylist").
//                                            replace(R.id.playlist_frame, PlaylistDetailsFragment.newInstance(playlist, playlists, true)).
                                            replace(R.id.playlist_frame, RemoveProgramFromPlaylistFragment.newInstance(playlist, playlists)).
                                            commit();
                                    return true;
                                case R.id.itemRemoveList:
                                    new AlertDialog.Builder(context).
                                            setTitle("האם אתה בטוח שתרצה להסיר רשימה זו?").
                                            setPositiveButton("כן", (dialog, which) -> {
                                                Toast.makeText(context, "הרשימה הוסרה!", Toast.LENGTH_SHORT).show();
                                                playlists.remove(playlist);
                                                new PlaylistsJsonWriter(playlists, context).execute();
                                                mShakeAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.stop_shake);
                                                itemView.startAnimation(mShakeAnimation);
                                                ivDeletePlaylist.setVisibility(View.GONE);
                                                isDoublePressed = false;
                                            }).setNegativeButton("לא", (dialog, which) -> {
                                        mShakeAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.stop_shake);
                                        itemView.startAnimation(mShakeAnimation);
                                        ivDeletePlaylist.setVisibility(View.GONE);
                                        isDoublePressed = false;
                                    }).show();

                                    return true;
                            }
                            return false;
                        }
                        @Override
                        public void onMenuModeChange(MenuBuilder menuBuilder) {
                        }
                    });
                }
                optionsMenu.show();
            }
        });
    }
}