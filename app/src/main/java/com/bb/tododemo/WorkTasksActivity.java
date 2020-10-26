package com.bb.tododemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkTasksActivity extends AppCompatActivity {

    FirebaseTasksRecyclerAdapter recyclerAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference work = firebaseFirestore.collection("work");

    String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.
            SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            recyclerAdapter.deleteItem(viewHolder.getAdapterPosition());
            recyclerAdapter.notifyItemRemoved(position);
            Toast.makeText(WorkTasksActivity.this, "Task deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    };

    private MaterialToolbar workToolbar;
    private RecyclerView workRecyclerView;
    private FloatingActionButton floatingActionButtonWork;

    private void initViews() {
        workToolbar = findViewById(R.id.workToolbar);
        workRecyclerView = findViewById(R.id.workRecyclerView);

        workToolbar.setTitle("Official Tasks");
        workToolbar.setSubtitle(today);
        setSupportActionBar(workToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButtonWork = findViewById(R.id.floatingActionButtonWork);
        floatingActionButtonWork.setVisibility(View.VISIBLE);
        floatingActionButtonWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkTasksActivity.this, AddTasksActivity.class);
                intent.putExtra("from", 2);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_tasks);

        initViews();

        Bundle extra = getIntent().getExtras();

        switch (extra.getInt("buttonValue")) {
            case 1:
                completed();
                break;
            case 2:
                today();
                break;
            case 3:
                future();
                break;
            default:
                Toast.makeText(this, "Work Button Selection error", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void future() {

        workToolbar.setTitle("Tasks to be Finished");

        Query query = work.orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("finished", false);
        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();

        recyclerAdapter = new FirebaseTasksRecyclerAdapter(options);
        workRecyclerView.setAdapter(recyclerAdapter);
        workRecyclerView.setLayoutManager(new LinearLayoutManager(WorkTasksActivity.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(workRecyclerView);

    }

    private void today() {

        workToolbar.setTitle("Tasks to Focus On Today");

        Query query = work.whereEqualTo("date", today)
                .orderBy("finished");
        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();


        recyclerAdapter = new FirebaseTasksRecyclerAdapter(options);
        workRecyclerView.setAdapter(recyclerAdapter);
        workRecyclerView.setLayoutManager(new LinearLayoutManager(WorkTasksActivity.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(workRecyclerView);

    }

    private void completed()  {

        workToolbar.setTitle("Finished Tasks");
        floatingActionButtonWork.setVisibility(View.GONE);

        Query query = work.orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("finished", true);

        FirestoreRecyclerOptions<Tasks>options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();

        recyclerAdapter = new FirebaseTasksRecyclerAdapter(options);
        workRecyclerView.setAdapter(recyclerAdapter);
        workRecyclerView.setLayoutManager(new LinearLayoutManager(WorkTasksActivity.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(workRecyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}