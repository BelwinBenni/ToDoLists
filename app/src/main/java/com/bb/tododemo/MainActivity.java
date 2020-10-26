package com.bb.tododemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MaterialButton btnCompleted, btnToday, btnFuture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personal();


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_personal:
                personal();
                break;
            case R.id.menu_work:
                work();
                break;
            default:
                Toast.makeText(this, "Unexpected Error with menu", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews(String title) {
        btnCompleted = findViewById(R.id.btnCompleted);
        btnToday = findViewById(R.id.btnToday);
        btnFuture = findViewById(R.id.btnFuture);

        MaterialToolbar mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        if (title.equals("Personal Tasks")) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar1));
        } else {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar2));
        }
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.personalToolbarText));
        mainToolbar.setSubtitleTextColor(getResources().getColor(R.color.personalToolbarText));

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_personal);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.getDateInstance().format(calendar.getTime());
        mainToolbar.setSubtitle(today);
        mainToolbar.setTitle(title);
    }


    private void personal() {
        initViews("Personal Tasks");

        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalTasksActivity.class);
                intent.putExtra("buttonValue", 1);
                startActivity(intent);
            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalTasksActivity.class);
                intent.putExtra("buttonValue", 2);
                startActivity(intent);
            }
        });

        btnFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalTasksActivity.class);
                intent.putExtra("buttonValue", 3);
                startActivity(intent);
            }
        });
    }


    private void work() {
        initViews("Work Appointments");

        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkTasksActivity.class);
                intent.putExtra("buttonValue", 1);
                startActivity(intent);
            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkTasksActivity.class);
                intent.putExtra("buttonValue", 2);
                startActivity(intent);
            }
        });

        btnFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkTasksActivity.class);
                intent.putExtra("buttonValue", 3);
                startActivity(intent);
            }
        });
    }

}