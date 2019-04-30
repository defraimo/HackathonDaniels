package daniels.hackathon.radio.admin.programsManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.login.User;
import daniels.hackathon.radio.programs.ProgramsData;

public class ProgramsManagerUsersListAdapter extends RecyclerView.Adapter<ProgramsManagerUsersListAdapter.UserList> {
    private List<User> users;
    private Context context;
    private ProgramsData program;

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    public ProgramsManagerUsersListAdapter(List<User> users, ProgramsData program, Context context) {
        this.users = users;
        this.context = context;
        this.program = program;
    }

    @NonNull
    @Override
    public UserList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_in_list_item, viewGroup, false);
        return new UserList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserList holder, int i) {
        User user = users.get(i);

        holder.tvStudentName.setText(user.getUsername());
        holder.program = program;
        holder.user = user;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserList extends RecyclerView.ViewHolder{
        TextView tvStudentName;
        ProgramsData program;
        User user;

        public UserList(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);

            itemView.setOnClickListener(v -> {
                broadcastingUsers.child(program.getVodId()).child(user.getUserID()).setValue(user);
            });
        }
    }
}
