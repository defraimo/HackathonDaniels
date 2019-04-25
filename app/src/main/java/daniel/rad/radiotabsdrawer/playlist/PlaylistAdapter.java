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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.playlist.chosenPlaylist.CreatePlaylistFragment;
import daniel.rad.radiotabsdrawer.playlist.removeProgramsFromPlaylist.RemoveProgramFromPlaylistFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PlaylistAdapter extends RecyclerView.Adapter<PlayListViewHolder> {
    ArrayList<Playlist> playlists;
    Context context;

    boolean pic1;
    boolean pic2;
    boolean pic3;
    boolean pic4;
    boolean allPic;
    int defaultPic;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

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
        holder.tvPlayList.setText(playlistName);

        defaultPicChooser(playlistName);

        ArrayList<ProgramsData> programsDataList = (ArrayList<ProgramsData>) playlist.getProgramsData();
        holder.ivProfileAll.setImageResource(defaultPic);

        playlistPics(holder, programsDataList);

        holder.playlist = playlist;
        holder.context = context;
        holder.playlists = playlists;
    }

    private void defaultPicChooser(String playlistName) {
        if (playlistName.contains("מומלצי")) {
            defaultPic = R.drawable.ic_default_recommended;
        } else if (playlistName.equals("מועדפים")) {
            defaultPic = R.drawable.ic_default_favourite;
        } else {
            defaultPic = R.drawable.ic_default_pic;
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    private void playlistPics(PlayListViewHolder holder,ArrayList<ProgramsData> programsDataList) {
        if (programsDataList.size() == 0){
            holder.ivProfileAll.setVisibility(View.VISIBLE);
            holder.ivProfilePic1.setVisibility(View.INVISIBLE);
            holder.ivProfilePic2.setVisibility(View.INVISIBLE);
            holder.ivProfilePic3.setVisibility(View.INVISIBLE);
            holder.ivProfilePic4.setVisibility(View.INVISIBLE);

            holder.ivProfileAll.setImageResource(defaultPic);
        }
        else if (programsDataList.size() == 1) {
            holder.pbLoadingAllPic.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(0).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingAllPic.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfileAll);
                allPic = true;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    holder.pbLoadingAllPic.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfileAll.setImageResource(defaultPic);
                    allPic = false;
                }
            });
            holder.ivProfileAll.setVisibility(View.VISIBLE);
            holder.ivProfilePic1.setVisibility(View.INVISIBLE);
            holder.ivProfilePic2.setVisibility(View.INVISIBLE);
            holder.ivProfilePic3.setVisibility(View.INVISIBLE);
            holder.ivProfilePic4.setVisibility(View.INVISIBLE);
        } else if (programsDataList.size() == 2){
            holder.pbLoadingPic1.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(0).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic1.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic1);
                pic1 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic1.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic1.setImageResource(defaultPic);
                    pic1 = false;
                }
            });

            holder.pbLoadingPic4.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(0).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic4.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic4);
                pic4 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic4.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic4.setImageResource(defaultPic);
                    pic4 = false;
                }
            });

            holder.pbLoadingPic2.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(1).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic2.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic2);
                pic2 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic2.setVisibility(View.INVISIBLE);
                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic2.setImageResource(defaultPic);
                    pic2 = false;
                }
            });

            holder.pbLoadingPic3.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(1).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic3.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic3);
                pic3 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic3.setVisibility(View.INVISIBLE);
                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic3.setImageResource(defaultPic);
                    pic3 = false;
                }
            });

            holder.ivProfileAll.setVisibility(View.INVISIBLE);
            holder.ivProfilePic1.setVisibility(View.VISIBLE);
            holder.ivProfilePic2.setVisibility(View.VISIBLE);
            holder.ivProfilePic3.setVisibility(View.VISIBLE);
            holder.ivProfilePic4.setVisibility(View.VISIBLE);
        }
        else if (programsDataList.size() == 3){
            holder.pbLoadingPic1.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(0).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic1.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic1);
                pic1 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic1.setVisibility(View.INVISIBLE);
                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic1.setImageResource(defaultPic);
                    pic1 = false;
                }
            });

            holder.pbLoadingPic4.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(0).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic4.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic4);
                pic4 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic4.setVisibility(View.INVISIBLE);
                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic4.setImageResource(defaultPic);
                    pic4 = false;
                }
            });

            holder.pbLoadingPic2.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(1).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic2.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic2);
                pic2 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic2.setVisibility(View.INVISIBLE);
                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic2.setImageResource(defaultPic);
                    pic2 = false;
                }
            });

            holder.pbLoadingPic3.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(2).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic3.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic3);
                pic3 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic3.setVisibility(View.INVISIBLE);
                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic3.setImageResource(defaultPic);
                    pic3 = false;
                }
            });

            holder.ivProfileAll.setVisibility(View.INVISIBLE);
            holder.ivProfilePic1.setVisibility(View.VISIBLE);
            holder.ivProfilePic2.setVisibility(View.VISIBLE);
            holder.ivProfilePic3.setVisibility(View.VISIBLE);
            holder.ivProfilePic4.setVisibility(View.VISIBLE);
        }
        else if (programsDataList.size() >= 4){
            holder.pbLoadingPic1.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(0).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic1.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic1);
                pic1 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic1.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic1.setImageResource(defaultPic);
                    pic1 = false;
                }
            });

            holder.pbLoadingPic4.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(3).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic4.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic4);
                pic4 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic4.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic4.setImageResource(defaultPic);
                    pic4 = false;
                }
            });

            holder.pbLoadingPic2.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(1).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic2.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic2);
                pic2 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic2.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic2.setImageResource(defaultPic);
                    pic2 = false;
                }
            });

            holder.pbLoadingPic3.setVisibility(View.VISIBLE);
            storageRef.child("images/" + programsDataList.get(2).getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                holder.pbLoadingPic3.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(holder.ivProfilePic3);
                pic3 = true;
                allPic = false;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.pbLoadingPic3.setVisibility(View.INVISIBLE);

                    defaultPicChooser(holder.playlist.getName());
                    holder.ivProfilePic3.setImageResource(defaultPic);
                    pic3 = false;
                }
            });

            holder.ivProfileAll.setVisibility(View.INVISIBLE);
            holder.ivProfilePic1.setVisibility(View.VISIBLE);
            holder.ivProfilePic2.setVisibility(View.VISIBLE);
            holder.ivProfilePic3.setVisibility(View.VISIBLE);
            holder.ivProfilePic4.setVisibility(View.VISIBLE);
        }
    }
    private void playlistPicLocal(@NonNull PlayListViewHolder holder, ArrayList<ProgramsData> programsDataList) {
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
    ProgressBar pbLoadingPic1;
    ProgressBar pbLoadingPic2;
    ProgressBar pbLoadingPic3;
    ProgressBar pbLoadingPic4;
    ProgressBar pbLoadingAllPic;
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
        pbLoadingPic1 = itemView.findViewById(R.id.pbLoadingPic1);
        pbLoadingPic2 = itemView.findViewById(R.id.pbLoadingPic2);
        pbLoadingPic3 = itemView.findViewById(R.id.pbLoadingPic3);
        pbLoadingPic4 = itemView.findViewById(R.id.pbLoadingPic4);
        pbLoadingAllPic = itemView.findViewById(R.id.pbLoadingAllPic);
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

                if (playlist.getName().equals("מועדפים") || playlist.getName().contains("מומלצי")) {
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
                                            replace(R.id.playlist_frame, RemoveProgramFromPlaylistFragment.newInstance(playlist)).
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
                                            replace(R.id.playlist_frame, RemoveProgramFromPlaylistFragment.newInstance(playlist)).
                                            commit();
                                    return true;
                                case R.id.itemRemoveList:
                                    new AlertDialog.Builder(context).
                                            setTitle("האם אתה בטוח שתרצה להסיר רשימה זו?").
                                            setPositiveButton("כן", (dialog, which) -> {
                                                Toast.makeText(context, "הרשימה הוסרה", Toast.LENGTH_SHORT).show();
                                                new PlaylistsJsonWriter(playlist, context , PlaylistsJsonWriter.DELETE_PLAYLIST).execute();

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