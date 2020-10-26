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

public class PersonalTasksActivity extends AppCompatActivity {

    FirebaseTasksRecyclerAdapter recyclerAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference personal = firebaseFirestore.collection("personal");

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
            Toast.makeText(PersonalTasksActivity.this, "Task deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    };

    private MaterialToolbar personalToolbar;
    private RecyclerView personalRecyclerView;
    private FloatingActionButton floatingActionButton;


    private void initViews() {

        personalRecyclerView = findViewById(R.id.personalRecyclerView);

        personalToolbar = findViewById(R.id.personalToolbar);
        personalToolbar.setTitle("Personal Tasks");
        personalToolbar.setSubtitle(today);
        setSupportActionBar(personalToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalTasksActivity.this, AddTasksActivity.class);
                intent.putExtra("from", 1);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_tasks);

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
                Toast.makeText(this, "Personal Button Selection error", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void future() {

        personalToolbar.setTitle("Tasks to be Finished");

        Query query = personal.orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("finished", false);

        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();

        recyclerAdapter = new FirebaseTasksRecyclerAdapter(options);
        personalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personalRecyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(personalRecyclerView);

    }

    private void today() {

        personalToolbar.setTitle("Tasks to Focus On Today");

        Query query = personal.whereEqualTo("date", today)
                .orderBy("finished");
        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();

        recyclerAdapter = new FirebaseTasksRecyclerAdapter(options);
        personalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personalRecyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(personalRecyclerView);

    }

    private void completed() {

        personalToolbar.setTitle("Finished Tasks");
        floatingActionButton.setVisibility(View.GONE);

        Query query = personal.orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("finished", true);

        FirestoreRecyclerOptions<Tasks> options = new FirestoreRecyclerOptions.Builder<Tasks>()
                .setQuery(query, Tasks.class)
                .build();

        recyclerAdapter = new FirebaseTasksRecyclerAdapter(options);
        personalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personalRecyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(personalRecyclerView);

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

