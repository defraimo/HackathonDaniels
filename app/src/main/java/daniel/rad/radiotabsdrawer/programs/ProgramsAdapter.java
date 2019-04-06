package daniel.rad.radiotabsdrawer.programs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daniel.rad.radiotabsdrawer.DrawerActivity;
import daniel.rad.radiotabsdrawer.MediaPlayerFragment;
import daniel.rad.radiotabsdrawer.PassingProgramsNames;
import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.myMediaPlayer.service.players.MediaPlayerAdapter;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ProgramViewHolder> {
    private MediaPlayerFragment playerFragment;
    private List<ProgramsData> programs;
    private Context context;

    private ProgramAdapterInterface programAdapterInterface;

    public ProgramsAdapter(List<ProgramsData> programs, Context context ,ProgramAdapterInterface programAdapterInterface) {
        playerFragment = new MediaPlayerFragment();
        this.programs = programs;
        this.context = context;
        this.programAdapterInterface = programAdapterInterface;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View programView = inflater.inflate(R.layout.program_item, viewGroup, false);

        ProgramViewHolder holder = new ProgramViewHolder(programView,programAdapterInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        ProgramsData program = programs.get(position);

        holder.tvProgramName.setText(program.getProgramName());
        holder.tvPName.setText(program.getStudentName());
        holder.ivProfilePic.setImageResource(program.getProfilePic());
        holder.program = program;
        holder.mediaPlayerFragment = playerFragment;
        //todo: replace ImageResource with and image from Facebook
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    class ProgramViewHolder extends RecyclerView.ViewHolder {
        MediaPlayerFragment mediaPlayerFragment;
        ImageView ivProfilePic;
        TextView tvPName;
        TextView tvProgramName;
        ImageView ivPlayProgram;
        ProgramsData program;
//        int programIndex = getAdapterPosition();

        public ProgramViewHolder(@NonNull View itemView, final ProgramAdapterInterface programAdapterInterface) {
            super(itemView);
            this.ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            this.tvPName = itemView.findViewById(R.id.tvChosenStudentName);
            this.tvProgramName = itemView.findViewById(R.id.tvChosenProgramName);
            this.ivPlayProgram = itemView.findViewById(R.id.ivPlayProgram);

//            globalMediaPlayer = new GlobalMediaPlayer(itemView.getContext(),ivPlayProgram,tvProgramName,tvPName);

            itemView.setOnClickListener((v) -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().
                        beginTransaction().
                        addToBackStack("chosenProgram").
                        replace(R.id.programsFrame, ChosenProgramFragment.newInstance(program)).
                        commit();
            });

            ivPlayProgram.setOnClickListener(v -> {
//                mediaPlayerFragment.initBnPlay(itemView.getRootView());
//                mediaPlayerFragment.playFunction();
//                mediaPlayerFragment.playChosenPrograms();

//                FragmentManager fragmentManager = fragment.getChildFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                ProgramListFragment fragment = new ProgramListFragment();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("programIndex", program);
//                fragment.setArguments(bundle);
//                fragment.getArgs();

                programAdapterInterface.onItemClicked(program);
            });
        }
    }
    public interface ProgramAdapterInterface{
        void onItemClicked(ProgramsData chosenProgram);
    }
}
