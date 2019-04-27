package daniel.rad.radiotabsdrawer.playlist.chosenPlaylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import de.hdodenhof.circleimageview.CircleImageView;


public class CreatePlaylistAdapter extends RecyclerView.Adapter<CreatePlaylistAdapter.CreatePlaylistViewHolder> {
    List<ProgramsData> programsDataList;
    Context context;
    ArrayList<Boolean> isSelectedArr;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private CreatePlaylistAdapterInterface createPlaylistAdapterInterface;

    public CreatePlaylistAdapter(List<ProgramsData> programsDataList, Context context, CreatePlaylistAdapterInterface createPlaylistAdapterInterface) {
        this.programsDataList = programsDataList;
        this.context = context;
        this.createPlaylistAdapterInterface = createPlaylistAdapterInterface;
        isSelectedArr = new ArrayList<>();

        for (int i = 0; i < programsDataList.size(); i++) {
            isSelectedArr.add(false);
        }
        System.out.println(isSelectedArr);
    }

    @NonNull
    @Override
    public CreatePlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chose_program_to_playlist_item, viewGroup, false);

        CreatePlaylistViewHolder holder = new CreatePlaylistViewHolder(view, createPlaylistAdapterInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreatePlaylistViewHolder holder, int position) {
        ProgramsData programsData = programsDataList.get(position);

        if(isSelectedArr.get(position)){
            holder.tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
        }else{
            holder.tbCheck.setBackgroundResource(R.drawable.ic_check);
        }

        holder.tvProgramName.setText(programsData.getProgramName());

//        holder.ivProfilePic.setImageResource(programsData.getProfilePic());
        storageRef.child("images/" + programsDataList.get(position).getVodId()).
                getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context).load(uri).into(holder.ivProfilePic);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.ivProfilePic.setImageResource(programsData.getProfilePic());
            }
        });
        holder.programsData = programsData;

        holder.tbCheck.setOnClickListener(v -> {
            if (holder.tbCheck.isChecked()) {
                holder.tbCheck.setBackgroundResource(R.drawable.ic_radio_button_checked);
                isSelectedArr.set(position, true);
                createPlaylistAdapterInterface.passProgram(true,programsData);
            } else if (!holder.tbCheck.isChecked()) {
                holder.tbCheck.setBackgroundResource(R.drawable.ic_check);
                createPlaylistAdapterInterface.passProgram(false,programsData);
                isSelectedArr.set(position, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programsDataList.size();
    }

    class CreatePlaylistViewHolder extends RecyclerView.ViewHolder {

        TextView tvProgramName;
        CircleImageView ivProfilePic;
        ToggleButton tbCheck;
        ProgramsData programsData;


        public CreatePlaylistViewHolder(@NonNull View itemView, final CreatePlaylistAdapterInterface createPlaylistAdapterInterface) {
            super(itemView);
            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tbCheck = itemView.findViewById(R.id.tbCheck);

        }
    }
    public interface CreatePlaylistAdapterInterface {
        void passProgram(boolean toAdd, ProgramsData programsData);
    }
}