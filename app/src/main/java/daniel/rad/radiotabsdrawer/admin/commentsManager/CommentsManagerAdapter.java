package daniel.rad.radiotabsdrawer.admin.commentsManager;

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

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

public class CommentsManagerAdapter extends RecyclerView.Adapter<CommentsManagerAdapter.CommentsViewHolder> {
    List<String> comments;
    Context context;
    ProgramsData model;

    private DatabaseReference programComments =
            FirebaseDatabase.getInstance()
                    .getReference("ProgramComments");

    public CommentsManagerAdapter(List<String> comments, Context context, ProgramsData model) {
        this.comments = comments;
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comments_manager_item, viewGroup, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int i) {
        String comment = this.comments.get(i);

        holder.tvComment.setText(comment);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.ivDeleteComment.setVisibility(View.VISIBLE);
                holder.ivDeleteComment.setEnabled(true);
                return true;
            }
        });

        holder.itemView.setOnClickListener(v -> {
            holder.ivDeleteComment.setVisibility(View.INVISIBLE);
            holder.ivDeleteComment.setEnabled(false);
        });

        holder.ivDeleteComment.setOnClickListener(v -> {
            programComments.child(model.getVodId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String commentValue = (String) child.getValue();
                        if (commentValue.equals(comment)){
                            programComments.child(model.getVodId()).child(child.getKey()).setValue("");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView tvComment;
        ImageView ivDeleteComment;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivDeleteComment = itemView.findViewById(R.id.ivDeleteComment);

            ivDeleteComment.setEnabled(false);
        }
    }
}
