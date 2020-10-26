package com.bb.tododemo;


import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class FirebaseTasksRecyclerAdapter extends FirestoreRecyclerAdapter<Tasks, FirebaseTasksRecyclerAdapter.ViewHolder> {

    public FirebaseTasksRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Tasks> options) {
        super(options);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Tasks model) {
        holder.tasklistTopic.setText(model.getTopic());
        holder.tasklistNotes.setText(model.getNotes());
        holder.tasklistDateToDo.setText(model.getDate());
        holder.isFinishedCheckbox.setChecked(model.isFinished());
        holder.tasklistTime.setText(model.getTime());

        holder.isFinishedCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position != RecyclerView.NO_POSITION){
                    final boolean newValue = !model.isFinished();
                    getSnapshots().getSnapshot(position).getReference().update("finished", newValue)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (newValue) {
                                        Toast.makeText(holder.isFinishedCheckbox.getContext(), "Marked as Completed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.isFinishedCheckbox.getContext(), "Marked as incomplete", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list_view, parent, false);
        FirebaseTasksRecyclerAdapter.ViewHolder holder = new FirebaseTasksRecyclerAdapter.ViewHolder(view);
        return holder;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tasklistTopic, tasklistNotes, tasklistDateToDo, tasklistTime;
        private CheckBox isFinishedCheckbox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tasklistTopic = itemView.findViewById(R.id.tasklistTopic);
            tasklistNotes = itemView.findViewById(R.id.tasklistNotes);
            tasklistDateToDo = itemView.findViewById(R.id.tasklistDateToDo);
            isFinishedCheckbox = itemView.findViewById(R.id.isFinishedCheckbox);
            tasklistTime = itemView.findViewById(R.id.tasklistTime);
        }
    }
}
