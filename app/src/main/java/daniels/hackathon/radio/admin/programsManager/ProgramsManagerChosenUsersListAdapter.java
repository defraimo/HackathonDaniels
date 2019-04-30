package daniels.hackathon.radio.admin.programsManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import daniels.hackathon.radio.R;
import daniels.hackathon.radio.login.User;
import daniels.hackathon.radio.programs.ProgramsData;

public class ProgramsManagerChosenUsersListAdapter extends RecyclerView.Adapter<ProgramsManagerChosenUsersListAdapter.UserList> {
    private List<User> users;
    private Context context;
    private ProgramsData program;

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    public ProgramsManagerChosenUsersListAdapter(List<User> users, ProgramsData program, Context context) {
        this.users = users;
        this.context = context;
        this.program = program;
    }

    @NonNull
    @Override
    public UserList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chosen_user_in_list_item, viewGroup, false);
        return new UserList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserList holder, int i) {
        User user = users.get(i);

        holder.tvStudentName.setText(user.getUsername());
        holder.user = user;
        holder.program = program;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserList extends RecyclerView.ViewHolder{
        TextView tvStudentName;
        ImageView ivDelete;
        User user;
        ProgramsData program;
        boolean delete = false;

        public UserList(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ivDelete.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            itemView.setOnClickListener(v -> {
                if (ivDelete.getVisibility() == View.VISIBLE){
                    ivDelete.setVisibility(View.INVISIBLE);
                }
            });

            ivDelete.setOnClickListener(v -> {
                if (ivDelete.getVisibility() == View.VISIBLE){
                    delete = true;
                    broadcastingUsers.child(program.getVodId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if (delete) {
                                    User u = child.getValue(User.class);
                                    if (u.getEmail().equals(user.getEmail())) {
                                        child.getRef().removeValue();
                                        delete = false;
                                        ivDelete.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
